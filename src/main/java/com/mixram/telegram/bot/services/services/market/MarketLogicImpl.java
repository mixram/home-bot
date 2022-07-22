package com.mixram.telegram.bot.services.services.market;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mixram.telegram.bot.config.cache.RedisTemplateHelper;
import com.mixram.telegram.bot.services.domain.InputMedia;
import com.mixram.telegram.bot.services.domain.entity.BotSettings;
import com.mixram.telegram.bot.services.domain.entity.InputMediaPhoto;
import com.mixram.telegram.bot.services.domain.entity.Message;
import com.mixram.telegram.bot.services.domain.entity.User;
import com.mixram.telegram.bot.services.services.bot.entity.LazyActionData;
import com.mixram.telegram.bot.services.services.bot.enums.LazyAction;
import com.mixram.telegram.bot.services.services.lazyaction.LazyActionLogic;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
import com.mixram.telegram.bot.utils.META;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author mixram on 2021-02-16.
 * @since 1.8.8.0
 */
@Log4j2
@Component
public class MarketLogicImpl implements MarketLogic {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final Object LOCK = new Object();

    private static final String POSTPONED_MESSAGES_DATA = "postponed_messages_data";
    //    private static final String MARKET_PATTERN_STRING = ".*#(продам|куплю|бронь).*";
    private static final String MARKET_PATTERN_STRING =
            ".*#([пП][рР][оО][дД][аА][мМ]|[кК][уУ][пП][лЛ][юЮ]|[бБ][рР][оО][нН][ьЬ]|[вВ][іІ][дД][дД]?[аА][мМ]).*";
    public static final Pattern MARKET_PATTERN = Pattern.compile(MARKET_PATTERN_STRING, Pattern.DOTALL);

    private final TelegramAPICommunicationComponent communicationComponent;
    private final LazyActionLogic lazyActionLogic;
    private final RedisTemplateHelper redisTemplateHelper;
    private final META meta;

    private final String archiveChatId;


    private static final Comparator<InputMedia> COMPARATOR = (o1, o2) -> {
        if (o1.getCaption() == null && o2.getCaption() != null) return 1;
        if (o1.getCaption() != null && o2.getCaption() == null) return -1;
        if (o1.getCaption() == null && o2.getCaption() == null) return 0;

        return o1.getCaption().compareTo(o2.getCaption());
    };

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public MarketLogicImpl(@Value("${bot.settings.other.market-archive-chat-id}") String archiveChatId,
                           TelegramAPICommunicationComponent communicationComponent,
                           LazyActionLogic lazyActionLogic,
                           RedisTemplateHelper redisTemplateHelper,
                           META meta) {
        this.communicationComponent = communicationComponent;
        this.lazyActionLogic = lazyActionLogic;
        this.redisTemplateHelper = redisTemplateHelper;
        this.meta = meta;

        this.archiveChatId = archiveChatId;
    }

    // </editor-fold>


    @Override
    public void doPostponedAction() {
        synchronized (LOCK) {
            final Map<String, List<Message>> messages = getPostponedMessagesListFromRedis();
            if (messages.isEmpty()) return;

            messages.forEach((mediaGroupId, messagesList) -> {
                final boolean isAdv = messagesList.stream()
                                                  .anyMatch(this :: isAdvertisement);
                if (isAdv) {
//                    messagesList.forEach(m -> doIfAdvertisement(m.getChat().getChatId(), m.getMessageId()));
                    doIfAdvertisement(messagesList);
                } else {
//                    messagesList.forEach(m -> doIfNotAdvertisement(m.getChat().getChatId(), m.getMessageId()));
                    doIfNotAdvertisement(messagesList);
                }
            });

            savePostponedMessagesDataToRedis(Maps.newHashMap());
        }
    }


    @Override
    public void saveMessageToRedisForPostponedLazyAction(@Nonnull Message message) {
        synchronized (LOCK) {
            final Map<String, List<Message>> messages = getPostponedMessagesListFromRedis();

            if (messages.containsKey(message.getMediaGroupId())) {
                messages.get(message.getMediaGroupId()).add(message);
            } else {
                messages.put(message.getMediaGroupId(), Lists.newArrayList(message));
            }

            savePostponedMessagesDataToRedis(messages);
        }
    }

    /**
     * To check if text contains signs, that message is an advertisement.
     *
     * @param message message.
     *
     * @return true - message is an advertisement, false - otherwise.
     *
     * @since 1.8.8.0
     */
    @Override
    public boolean isAdvertisement(@Nonnull Message message) {
        final String text = prepareText(message);

//        return StringUtils.isNotBlank(text) && (
//                text.contains("#продам")
//                        || text.contains("#куплю")
//                        || text.contains("#бронь")
//        );

        return StringUtils.isNotBlank(text) && MARKET_PATTERN.matcher(text).matches();
    }

    @Override
    public void doIfAdvertisement(@Nonnull Long chatId,
                                  @Nonnull Long messageId) {
        try {
            log.info("Message {} is an advertisement, will not be deleted.", messageId);

            lazyActionLogic.removeLazyActionFromRedis(
                    new LazyActionData(chatId, messageId, LazyAction.DELETE, LocalDateTime.now()));
            communicationComponent.forwardMessage(String.valueOf(chatId), archiveChatId, String.valueOf(messageId));
        } catch (Exception e) {
            log.warn("", e);
        }
    }

    @Override
    public void doIfAdvertisement(@Nonnull List<Message> messages) {
        try {
            log.info("Messages are an advertisement, will not be deleted.");

            final LocalDateTime now = LocalDateTime.now();
            final Long chatId = messages.get(0).getChat().getChatId();
            final List<LazyActionData> la = Lists.newArrayListWithExpectedSize(messages.size());
            final List<InputMedia> mg = Lists.newArrayListWithExpectedSize(messages.size());
            messages.forEach(m -> {
                la.add(new LazyActionData(chatId, m.getMessageId(), LazyAction.DELETE, now));
                mg.add(new InputMediaPhoto(m.getPhoto().get(m.getPhoto().size() - 1).getFileId(), prepareCaption(m),
                                           m.getCaptionEntities()));
            });
            mg.sort(COMPARATOR);

            lazyActionLogic.removeLazyActionsFromRedis(la);
            communicationComponent.sendMediaGroup(archiveChatId, mg);
        } catch (Exception e) {
            log.warn("", e);
        }
    }

    @Override
    public void doIfNotAdvertisement(@Nonnull Message message) {
        try {
            final Long chatId = message.getChat().getChatId();
            final BotSettings botSettings = meta.settings.get(chatId);

            if (botSettings.getAdmins() != null && botSettings.getAdmins().contains(message.getUser().getId())) {
                log.info("Message {} is from admin {}_{}. Will not be deleted.",
                         message :: getMessageId,
                         () -> message.getUser().getUsername(),
                         () -> message.getUser().getId());
                return;
            }

            log.info("Message {} is not an advertisement, will be deleted.", message :: getMessageId);
            lazyActionLogic.saveLazyActionToRedis(
                    new LazyActionData(chatId, message.getMessageId(), LazyAction.DELETE,
                                       LocalDateTime.now().plusSeconds(botSettings.getMarketMessageDeleteTime())));
        } catch (Exception e) {
            log.warn("", e);
        }
    }

    @Override
    public void doIfNotAdvertisement(@Nonnull List<Message> messages) {
        try {
            log.info("Messages are not an advertisement, will be deleted.");

            final Long chatId = messages.get(0).getChat().getChatId();
            final BotSettings botSettings = meta.settings.get(chatId);
            final Set<Long> admins = botSettings.getAdmins();
            final LocalDateTime plusSeconds = LocalDateTime.now().plusSeconds(botSettings.getMarketMessageDeleteTime());

            lazyActionLogic.saveLazyActionsToRedis(
                    messages.stream()
                            .filter(m -> admins == null || !admins.contains(m.getUser().getId()))
                            .map(m -> new LazyActionData(chatId, m.getMessageId(), LazyAction.DELETE, plusSeconds))
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            log.warn("", e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.8.8.0
     */
    @Nullable
    private String prepareText(@Nonnull Message message) {
        return message.getText() == null ? message.getCaption() : message.getText();
    }

    /**
     * @since 1.8.8.0
     */
    @Nullable
    private String prepareCaption(@Nonnull Message message) {
        final String text = prepareText(message);
        return text == null ? null : text + "\n\nАвтор: " + prepareUserName(message.getUser());
    }

    /**
     * @since 1.8.8.0
     */
    @Nonnull
    private String prepareUserName(@Nonnull User user) {
        final StringBuilder builder = new StringBuilder();
        if (user.getFirstName() != null) builder.append(user.getFirstName()).append(" ");
        if (user.getLastName() != null) builder.append(user.getLastName());
        if (user.getFirstName() != null || user.getLastName() != null) builder.append(", ");
        if (user.getUsername() != null) builder.append("@").append(user.getUsername()).append(", ");
        builder.append(user.getId()).append(".");

        return builder.toString();
    }

    /**
     * @since 1.8.8.0
     */
    private void savePostponedMessagesDataToRedis(@Nonnull Map<String, List<Message>> postponedMessagesMap) {
        redisTemplateHelper.storePostponedMessagesDataToRedis(postponedMessagesMap, POSTPONED_MESSAGES_DATA);
    }

    /**
     * @since 1.8.8.0
     */
    @Nonnull
    private Map<String, List<Message>> getPostponedMessagesListFromRedis() {
        Map<String, List<Message>> lazyActionDataList = redisTemplateHelper.getPostponedMessagesDataFromRedis(POSTPONED_MESSAGES_DATA);

        return lazyActionDataList == null ? Maps.newHashMap() : lazyActionDataList;
    }

    // </editor-fold>
}
