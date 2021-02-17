package com.mixram.telegram.bot.services.services.bot;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mixram.telegram.bot.services.domain.entity.*;
import com.mixram.telegram.bot.services.domain.enums.Command;
import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.services.domain.enums.WorkType;
import com.mixram.telegram.bot.services.modules.DiscountsOn3DPlasticModule;
import com.mixram.telegram.bot.services.modules.Module3DPlasticDataSearcher;
import com.mixram.telegram.bot.services.services.antibot.AntiBot;
import com.mixram.telegram.bot.services.services.bot.entity.LazyActionData;
import com.mixram.telegram.bot.services.services.bot.entity.MessageData;
import com.mixram.telegram.bot.services.services.bot.enums.LazyAction;
import com.mixram.telegram.bot.services.services.bot.enums.PlasticPresenceState;
import com.mixram.telegram.bot.services.services.lazyaction.LazyActionLogic;
import com.mixram.telegram.bot.services.services.market.MarketLogic;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
import com.mixram.telegram.bot.utils.AsyncHelper;
import com.mixram.telegram.bot.utils.CustomMessageSource;
import com.mixram.telegram.bot.utils.DateTimeUtils;
import com.mixram.telegram.bot.utils.META;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent.SOMETHING_WRONG_MESSAGE;


/**
 * @author mixram on 2019-04-10.
 * @since 1.4.1.0
 */
@Log4j2
@Component
public class Bot3DComponentImpl implements Bot3DComponent {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String PRIVATE_CHAT_NAME = "private";
    private static final String GROUP_CHAT_NAME = "group";
    private static final String SUPER_GROUP_CHAT_NAME = "supergroup";

    private static final String SALES_PATTERN_STRING = "^/SALES_.*";
    private static final String OTHER_COMMANDS_PATTERN_STRING = "^/START.*|^/INFO.*";
    private static final String TEST_COMMANDS_PATTERN_STRING = "^/TEST.*";
    private static final String CAS_PATTERN_STRING = "^/CAS_MSG_.*";
    private static final Pattern SALES_PATTERN = Pattern.compile(SALES_PATTERN_STRING);
    private static final Pattern OTHER_PATTERN = Pattern.compile(OTHER_COMMANDS_PATTERN_STRING);
    private static final Pattern TEST_PATTERN = Pattern.compile(TEST_COMMANDS_PATTERN_STRING);
    private static final Pattern CAS_PATTERN = Pattern.compile(CAS_PATTERN_STRING);

    private static final String NO_WORK_WITH_SHOP = "telegram.bot.message.no-work-with-shop";
    private static final String NO_DATA_FOR_SHOP = "telegram.bot.message.no-data-for-shop";
    private static final String NO_DISCOUNTS = "telegram.bot.message.no-discounts";
    private static final List<String> MISUNDERSTANDING_MESSAGES = ImmutableList.of(
            "telegram.bot.message.misunderstanding-1",
            "telegram.bot.message.misunderstanding-2",
            "telegram.bot.message.misunderstanding-3",
            "telegram.bot.message..misunderstanding-4"
    );
    private static final String NO_PRIVATE_CHAT_MESSAGE = "telegram.bot.message.no-private-chat";
    private static final String NO_GROUP_CHAT_MESSAGE = "telegram.bot.message.no-group-chat";
    private static final String CONCRETE_GROUP_MESSAGE = "telegram.bot.message.concrete-group";
    private static final String START_ANSWER_MESSAGE = "telegram.bot.message.start-answer";
    private static final String INFO_ANSWER_MESSAGE = "telegram.bot.message.info-answer";
    private static final String INFO_ANSWER_ALL_MESSAGE = "telegram.bot.message.info-answer.all";
    private static final String USER_CALL_MESSAGE = "telegram.bot.message.user-call";
    private static final String NEW_USER_MESSAGE = "telegram.bot.message.new-user";
    private static final String USER_LEFT_GROUP_MESSAGE = "telegram.bot.message.user-left-group";
    public static final String SHOP_MESSAGE_PART_MESSAGE = "telegram.bot.message.shop-message.part";
    private static final String SHORT_DISCOUNT_PART_MESSAGE = "telegram.bot.message.discount.short.part";
    private static final String FULL_DISCOUNT_PART_MESSAGE = "telegram.bot.message.discount.full.part";
    private static final String FULL_DISCOUNT_OTHER_MESSAGE = "telegram.bot.message.discount.full.other";
    private static final String SHORT_MESSAGE_LEGEND_MESSAGE = "telegram.bot.message.short-message-legend";
    private static final String NEW_CHAT_MEMBERS_HELLO_MESSAGE = "telegram.bot.message.new-chat-members-hello";
    private static final String NEW_CHAT_MEMBERS_HELLO_MESSAGE_V2 = "telegram.bot.message.new-chat-members-hello-v2";
    private static final String VERSION_UPDATE_MESSAGE = "telegram.bot.message.version-info";

    private final Integer maxQuantity;
    private final Random random;
    private final WorkType workType;
    private final String adminEmail;
    private final String fleaMarket;
    private final String infoTable;
    private final String wikiUrl;
    private final String pinnedMessage;
    private final String pinnedMessage2;
    private final boolean versionInform;
    private final boolean antiBotIsOn;
    private final boolean casIsOn;
    private final boolean marketIsOn;

    private final Module3DPlasticDataSearcher searcher;
    private final TelegramAPICommunicationComponent communicationComponent;
    private final LazyActionLogic lazyActionLogic;
    private final MarketLogic marketLogic;
    private final AsyncHelper asyncHelper;
    private final CustomMessageSource messageSource;
    private final AntiBot antiBot;
    private final META meta;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CommandHolder {

        /**
         * Command to execute.
         */
        private Command command;
        /**
         * true - need full message content, false - need short message content.
         */
        private boolean full;
        /**
         * Some needed data for the command.
         */
        private Object data;

        @Override
        public String toString() {
            return JsonUtil.toJson(this);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class PlasticPresenceDto {

        private PlasticPresenceState presenceState;
        private String mainUrl;

        @Override
        public String toString() {
            return JsonUtil.toJson(this);
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public Bot3DComponentImpl(@Value("${bot.settings.other.max-quantity-for-full-view}") Integer maxQuantity,
                              @Value("${bot.settings.work-with}") WorkType workType,
                              @Value("${bot.settings.admin-email}") String adminEmail,
                              @Value("${bot.settings.flea-market}") String fleaMarket,
                              @Value("${bot.settings.info-table}") String infoTable,
                              @Value("${bot.settings.wiki-url}") String wikiUrl,
                              @Value("${bot.settings.pinned-message}") String pinnedMessage,
                              @Value("${bot.settings.pinned-message2}") String pinnedMessage2,
                              @Value("${bot.settings.other.inform-about-version}") boolean versionInform,
                              @Value("${bot.settings.other.anti-bot-is-on}") boolean antiBotIsOn,
                              @Value("${bot.settings.other.cas-is-on}") boolean casIsOn,
                              @Value("${bot.settings.other.market-logic-is-on}") boolean marketIsOn,
                              @Qualifier("discountsOn3DPlasticDataCacheComponent") Module3DPlasticDataSearcher searcher,
                              META meta,
                              TelegramAPICommunicationComponent communicationComponent,
                              AsyncHelper asyncHelper,
                              CustomMessageSource messageSource,
                              AntiBot antiBot,
                              LazyActionLogic lazyActionLogic,
                              MarketLogic marketLogic) {
        this.maxQuantity = maxQuantity;
        this.workType = workType;
        this.adminEmail = adminEmail;
        this.fleaMarket = fleaMarket;
        this.infoTable = infoTable;
        this.wikiUrl = wikiUrl;
        this.pinnedMessage = pinnedMessage;
        this.pinnedMessage2 = pinnedMessage2;
        this.versionInform = versionInform;
        this.antiBotIsOn = antiBotIsOn;
        this.casIsOn = casIsOn;
        this.marketIsOn = marketIsOn;

        this.searcher = searcher;
        this.communicationComponent = communicationComponent;
        this.asyncHelper = asyncHelper;
        this.messageSource = messageSource;
        this.antiBot = antiBot;
        this.lazyActionLogic = lazyActionLogic;
        this.marketLogic = marketLogic;
        this.meta = meta;

        this.random = new Random();
    }

    @PostConstruct
    public void init() {
        if (!versionInform) {
            log.info("Reminder about version updates is switched off!");

            return;
        }
        log.info("{}#remindVersion() is started!", DiscountsOn3DPlasticModule.class :: getSimpleName);

        try {
            String message = messageSource.getMessage(VERSION_UPDATE_MESSAGE, META.DEFAULT_LOCALE);
            MessageData data = MessageData.builder()
                                          .message(message)
                                          .build();

            meta.settings.forEach((groupId, settings) -> {
                if (settings.getVersionInform()) {
                    communicationComponent.sendMessageToChat(groupId, data);
                }
            });
        } catch (Exception ex) {
            log.warn("", ex);
        }
    }

    // </editor-fold>

    @Override
    public MessageData proceedUpdate(Update update) {
        Validate.notNull(update, "Update is not specified!");

        log.debug("UPDATE: {}", () -> update);

        Locale locale = META.DEFAULT_LOCALE;

        CallbackQuery callbackQuery = update.getCallbackQuery();
        if (callbackQuery != null) {
            MessageData messageData = proceedCallBackV2(callbackQuery, locale);

            Long chatId = callbackQuery.getMessage().getChat().getChatId();
            saveToLazyActions(messageData, chatId, LazyAction.DELETE, meta.settings.get(chatId).getHelloMessageDeleteTime());
            checkCAS(callbackQuery);

            return messageData;
        }

        Message message = update.getMessage();
        if (message == null) {
            message = update.getEditedMessage();
            if (message == null) {
                log.warn("No 'Message' in 'Update' structure! {}", () -> update);

                return null;
            }
        }

        //        Locale locale = message.getUser() == null || message.getUser().getLanguageCode() == null ? META.DEFAULT_LOCALE :
        //                        new Locale(message.getUser().getLanguageCode());

        MessageData workCheckMessage = checkMayWorkWith(message, locale);
        if (workCheckMessage != null) {
            return workCheckMessage;
        }

        MessageData newChatMembersMessage = checkNewChatMembers(message, META.DEFAULT_LOCALE);
        if (newChatMembersMessage != null) {
            infoAdmin(update);

            if (newChatMembersMessage.getDoIfAntiBot() == null) {
                Long chatId = message.getChat().getChatId();
                saveToLazyActions(newChatMembersMessage, chatId, LazyAction.DELETE, meta.settings.get(chatId).getHelloMessageDeleteTime());
            }

            return newChatMembersMessage;
        }

        MessageData leftChatMember = checkIfUserHasLeftGroup(message, META.DEFAULT_LOCALE);
        if (leftChatMember != null) {
            proceedUserHasLeftStuff(message);

            return leftChatMember;
        }

        doMarketLogic(message);

        if (noNeedToAnswer(message)) {
            return null;
        }

        final CommandHolder command;
        try {
            command = defineCommand(message.getText());
        } catch (Exception e) {
            log.warn(String.format("Error in command defining: %s!", message.getText()), e);

            return prepareMisunderstandingMessage(locale);
        }

        infoAdmin(update);

        //        if (isPrivate(message.getChat().getType())) {
        //            saveNewUser();
        //        }

        return prepareAnswerWithCommand(command, message, locale);
    }

    /**
     * @since 1.4.1.0
     */
    public String prepareMessageForShopToSendString(Data3DPlastic plastic,
                                                    Shop3D shop,
                                                    Command command,
                                                    boolean full,
                                                    boolean onlyDiscounts,
                                                    boolean noDataText,
                                                    Locale locale) {
        return plastic == null || CollectionUtils.isEmpty(plastic.getData()) ?
               noDataText ? messageSource.getMessage(NO_DATA_FOR_SHOP, locale) : null :
               doPrepareMessageToSendString(command, full, onlyDiscounts, plastic, shop, locale);
    }

    /**
     * @since 1.4.1.0
     */
    public String prepareMessageForShopsToSendString(boolean full,
                                                     boolean onlyDiscounts,
                                                     boolean noDataText,
                                                     Locale locale) {
        StringBuilder builder = new StringBuilder();
        for (Shop3D shop : Shop3D.values()) {
            Data3DPlastic plastic = searcher.search(shop);
            String messageToSendStringTemp =
                    prepareMessageForShopToSendString(plastic, shop, Command.getByShop(shop), full, onlyDiscounts,
                                                      noDataText, locale);

            if (StringUtils.isNotBlank(messageToSendStringTemp) && !NO_DATA_FOR_SHOP.equals(messageToSendStringTemp)) {
                String shopUrl = plastic == null || CollectionUtils.isEmpty(plastic.getData()) ? null :
                                 plastic.getData().get(0).getShopUrl();
                builder.append(
                        messageSource.getMessage(SHOP_MESSAGE_PART_MESSAGE, locale, shopUrl, shop.getNameAlt(),
                                                 messageToSendStringTemp));
            }
        }

        if (builder.length() > 0) {
            builder.append(messageSource.getMessage(SHORT_MESSAGE_LEGEND_MESSAGE, locale,
                                                    getDiscountText(PlasticPresenceState.DISCOUNT),
                                                    getDiscountText(PlasticPresenceState.IN_STOCK),
                                                    getDiscountText(PlasticPresenceState.NOT_IN_STOCK)));
        }

        return builder.toString();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.8.8.0
     */
    private void doMarketLogic(@Nonnull Message message) {
        try {
            if (!marketIsOn) return;

            final Long chatId = message.getChat().getChatId();
            final Long messageId = message.getMessageId();
            final BotSettings botSettings = meta.settings.get(chatId);

            if (botSettings.getEnableMarketLogic() == null || !botSettings.getEnableMarketLogic()) return;

            if (message.getMediaGroupId() != null) {
                marketLogic.saveMessageToRedisForPostponedLazyAction(message);

                log.info("Message {} has media group id. Will be postponed.", messageId);

                return;
            }

            if (marketLogic.isAdvertisement(message)) {
                marketLogic.doIfAdvertisement(chatId, messageId);

                return;
            }

            marketLogic.doIfNotAdvertisement(chatId, messageId);
        } catch (Exception e) {
            log.warn("", e);
        }
    }

    /**
     * @since 1.8.5.0
     */
    private void checkCAS(CallbackQuery query) {
        if (!casIsOn) return;

        asyncHelper.doAsync((Supplier<Void>) () -> {
            try {
                final Long chatId = query.getMessage().getChat().getChatId();
                final BotSettings botSettings = meta.settings.get(chatId);
                if (botSettings == null || CollectionUtils.isEmpty(botSettings.getInfoChats())) {
                    log.warn("There are no BotSettings for chat " + chatId + "!");

                    return null;
                }

                final User user = query.getUser();
                final Long id = user.getId();

                final CASData data = antiBot.checkCAS(user.getId());
                final CASData.CASResultData casData = data.getData();

                if (data.isBaned()) {
                    final String message = "Новый пользователь (" + id + ", " + user.getUsername() + ", " + user.getFirstName() +
                            " " + user.getLastName() + "):\n" +
                            "📌 нарушений: " + (casData == null || casData.getOffenses() == null ? 0 : casData.getOffenses()) + ";\n" +
                            "📌 когда забанили: " + (casData == null || casData.getTimeAdded() == null ? "нет данных" : casData.getTimeAdded()) + ";\n\n" +
                            "Чтобы посмотреть бан-сообщения - кликай: /cas_msg_" + id;
                    final MessageData messageData = MessageData.builder()
                                                               .message(message)
                                                               .build();

                    botSettings.getInfoChats().forEach(c -> communicationComponent.sendMessageToChat(c, messageData));
                }
            } catch (Exception e) {
                log.warn("CAS exception!", e);
            }

            return null;
        });
    }

    /**
     * @since 1.8.2.0
     */
    private void saveToLazyActions(MessageData messageData,
                                   Long chatId,
                                   LazyAction lazyAction,
                                   Integer actionTime) {
        saveToLazyActions(messageData, chatId, null, lazyAction, actionTime);
    }

    /**
     * @since 1.8.2.0
     */
    private void saveToLazyActions(MessageData messageData,
                                   Long chatId,
                                   Long messageId,
                                   LazyAction lazyAction,
                                   Integer actionTime) {
        Consumer<Long> saver = lazyActionLogic.createSaver(
                new LazyActionData(chatId, messageId, lazyAction, LocalDateTime.now().plusSeconds(actionTime)));
        messageData.setDoIfLazyAction(Lists.newArrayList(saver));
    }

    /**
     * @since 1.7.0.0
     * @deprecated use {@link Bot3DComponentImpl#proceedCallBackV2(CallbackQuery, Locale)} instead since 1.8.3.0.
     */
    private MessageData proceedCallBack(CallbackQuery callbackQuery,
                                        Locale locale) {
        if (META.NOT_A_BOT_TEXT.equalsIgnoreCase(callbackQuery.getData())) {
            //TODO: need to rebuild logic when "not_a_bot_text" will not be the one
            antiBot.proceedCallBack(callbackQuery);

            return welcomeNewChatMember(callbackQuery.getUser(),
                                        meta.settings.get(callbackQuery.getMessage().getChat().getChatId())
                                                     .getWelcomeNewUserMessage(),
                                        locale);
        }

        throw new UnsupportedOperationException(
                String.format("Unexpected callback data: '%s'!", callbackQuery.getData()));
    }

    /**
     * @since 1.8.3.0
     */
    private MessageData proceedCallBackV2(CallbackQuery callbackQuery,
                                          Locale locale) {
        antiBot.proceedCallBack(callbackQuery);

        return welcomeNewChatMember(callbackQuery.getUser(),
                                    meta.settings.get(callbackQuery.getMessage().getChat().getChatId())
                                                 .getWelcomeNewUserMessage(),
                                    locale);
    }

    /**
     * @since 1.4.1.0
     */
    private MessageData checkNewChatMembers(Message message,
                                            Locale locale) {
        List<User> newChatMembers = checkMessageForNewMembers(message);
        if (CollectionUtils.isEmpty(newChatMembers)) {
            return null;
        }

        Long chatId = message.getChat().getChatId();
        BotSettings settings = meta.settings.get(chatId);

        //TODO: need to rebuild in order to be able to check more then one new user at the same time
        return needAntiBotCheck(settings, newChatMembers, message) ?
               checkBotNewChatMembers(newChatMembers, chatId, message.getMessageId(), locale) :
               welcomeNewChatMembers(newChatMembers, settings.getWelcomeNewUserMessage(), locale);
    }

    /**
     * @since 1.8.2.0
     */
    private boolean needAntiBotCheck(BotSettings settings,
                                     List<User> newChatMembers,
                                     Message message) {
        return antiBotIsOn && settings.getEnableAntiBot() && newChatMembers.size() == 1 &&
                incomeByInviteLink(message.getUser(), newChatMembers.get(0));
    }

    /**
     * @since 1.8.1.0
     */
    private MessageData checkIfUserHasLeftGroup(Message message,
                                                Locale locale) {
        User leftChatMember = checkMessageIfUserHasLeftGroup(message);
        if (leftChatMember == null) {
            return null;
        }

        return MessageData.builder()
                          .toAdmin(true)
                          .message(messageSource.getMessage(USER_LEFT_GROUP_MESSAGE, locale,
                                                            JsonUtil.toPrettyJson(message.getChat()),
                                                            JsonUtil.toPrettyJson(leftChatMember),
                                                            prepareDateTime(message.getTimestamp())))
                          .build();
    }

    /**
     * @since 1.8.1.0
     */
    private void proceedUserHasLeftStuff(Message message) {
        communicationComponent.removeMessageFromChat(String.valueOf(message.getChat().getChatId()),
                                                     String.valueOf(message.getMessageId()));
    }

    /**
     * @since 1.8.1.0
     */
    private User checkMessageIfUserHasLeftGroup(Message message) {
        return message.getLeftChatMember();
    }

    /**
     * @since 1.7.0.0
     */
    private boolean incomeByInviteLink(User user,
                                       User newUser) {
        if (user == null || newUser == null) {
            return true;
        }

        return user.getId().equals(newUser.getId());
    }

    /**
     * @since 1.7.0.0
     */
    private MessageData checkBotNewChatMembers(List<User> newChatMembers,
                                               Long chatId,
                                               Long userIncomeMessageId,
                                               Locale locale) {
        return antiBot.checkUser(newChatMembers, chatId, userIncomeMessageId, locale);
    }

    /**
     * @since 1.7.0.0
     */
    private MessageData welcomeNewChatMembers(List<User> newChatMembers,
                                              String mainMessageCode,
                                              Locale locale) {
        StringBuilder builder = new StringBuilder();
        newChatMembers.forEach(u -> builder.append("<a href=\"tg://user?id=").append(u.getId()).append("\">").append(
                u.getFirstName()).append("</a>").append(", "));

        return MessageData.builder()
                          .message(messageSource.getMessage(mainMessageCode, locale, builder.toString()))
                          .build();
    }

    /**
     * @since 1.7.0.0
     */
    private MessageData welcomeNewChatMember(User newChatMember,
                                             String mainMessageCode,
                                             Locale locale) {
        String message = String.format("<a href=\"tg://user?id=%s\">%s</a>, ", newChatMember.getId(),
                                       newChatMember.getFirstName());

        return MessageData.builder()
                          .message(messageSource.getMessage(mainMessageCode, locale, message))
                          .build();
    }

    /**
     * @since 1.6.1.1
     */
    private List<User> checkMessageForNewMembers(Message message) {
        List<User> newChatMembers =
                Optional.ofNullable(message.getNewChatMembers()).orElse(Lists.newArrayListWithExpectedSize(0)).stream()
                        .filter(u -> !u.getIsBot())
                        .collect(Collectors.toList());

        return CollectionUtils.isEmpty(newChatMembers) ? null : newChatMembers;
    }

    /**
     * @since 1.4.0.0
     */
    private MessageData checkMayWorkWith(Message message,
                                         Locale locale) {
        Chat chat = message.getChat();
        BotSettings settings = meta.settings.get(chat.getChatId());
        switch (workType) {
            case P:
                return isPrivate(chat.getType()) ? null : prepareNoPrivateChatMessage(locale);
            case G:
                return isGroup(chat.getType()) ? (settings == null ? prepareConcreteGroupChatMessage(locale) : null) :
                       prepareNoGroupChatMessage(locale);
            case B:
                return isGroup(chat.getType()) ? (settings == null ? prepareConcreteGroupChatMessage(locale) : null) :
                       null;
            default:
                throw new UnsupportedOperationException(String.format("Unexpected work type: '%s'!", workType));
        }
    }

    /**
     * @since 1.4.0.0
     */
    private boolean isPrivate(String type) {
        return PRIVATE_CHAT_NAME.equalsIgnoreCase(type);
    }

    /**
     * @since 1.4.0.0
     */
    private boolean isGroup(String type) {
        return GROUP_CHAT_NAME.equalsIgnoreCase(type) || SUPER_GROUP_CHAT_NAME.equalsIgnoreCase(type);
    }

    /**
     * @since 1.4.0.0
     */
    private MessageData prepareNoPrivateChatMessage(Locale locale) {
        return MessageData.builder()
                          .message(messageSource.getMessage(NO_PRIVATE_CHAT_MESSAGE, locale))
                          .toResponse(false)
                          .toAdmin(false)
                          .leaveChat(true)
                          .build();
    }

    /**
     * @since 1.4.0.0
     */
    private MessageData prepareNoGroupChatMessage(Locale locale) {
        return MessageData.builder()
                          .message(messageSource.getMessage(NO_GROUP_CHAT_MESSAGE, locale))
                          .toResponse(false)
                          .toAdmin(false)
                          .leaveChat(true)
                          .build();
    }

    /**
     * @since 1.4.0.0
     */
    private MessageData prepareConcreteGroupChatMessage(Locale locale) {
        return MessageData.builder()
                          .message(messageSource.getMessage(CONCRETE_GROUP_MESSAGE, locale, adminEmail))
                          .toResponse(false)
                          .toAdmin(false)
                          .leaveChat(true)
                          .build();
    }

    /**
     * @since 1.3.2.0
     */
    private MessageData prepareStartAnswer(Locale locale) {
        return MessageData.builder()
                          .message(messageSource.getMessage(START_ANSWER_MESSAGE, locale))
                          .toAdmin(false)
                          .toResponse(false)
                          .userResponse(true)
                          .build();
    }

    /**
     * @since 1.4.0.0
     */
    private MessageData prepareInfoAnswer(String mainMessageId,
                                          Locale locale) {
        return MessageData.builder()
                          .message(messageSource.getMessage(mainMessageId, locale,
                                                            System.getProperty("product.version.full")))
                          .toAdmin(false)
                          .toResponse(false)
                          .userResponse(false)
                          .build();
    }

    /**
     * @since 1.4.1.0
     */
    private MessageData prepareInfoAnswerAll(Locale locale) {
        return MessageData.builder()
                          .message(messageSource.getMessage(INFO_ANSWER_ALL_MESSAGE, locale, fleaMarket, infoTable,
                                                            wikiUrl, pinnedMessage, pinnedMessage2,
                                                            System.getProperty("product.version.full")))
                          .toAdmin(false)
                          .toResponse(false)
                          .userResponse(false)
                          .build();
    }

    /**
     * @since 1.3.0.0
     */
    private void infoAdmin(Update update) {
        asyncHelper.doAsync((Supplier<Void>) () -> {
            doInfoAdmin(update);

            return null;
        });
    }

    /**
     * @since 1.3.0.0
     */
    private void doInfoAdmin(Update update) {
        try {
            Message message = update.getMessage();

            List<User> users = checkMessageForNewMembers(message);

            User user = message.getUser();
            if (user != null && communicationComponent.getAdmins()
                                                      .contains(user.getId()) && users == null) {
                return;
            }
            Long adminId = message.getChat().getChatId();
            if (communicationComponent.getAdmins().contains(adminId) && users == null) {
                return;
            }

            LocalDateTime ldt = prepareDateTime(message.getTimestamp());
            Locale locale = user == null || user.getLanguageCode() == null ? META.DEFAULT_LOCALE :
                            new Locale(user.getLanguageCode());

            MessageData messageData;
            if (users == null) {
                messageData =
                        MessageData.builder()
                                   .message(messageSource.getMessage(USER_CALL_MESSAGE, locale,
                                                                     JsonUtil.toPrettyJson(message.getChat()),
                                                                     user == null ? "---" : JsonUtil.toPrettyJson(user),
                                                                     message.getText(), ldt))
                                   .build();
            } else {
                messageData =
                        MessageData.builder()
                                   .message(messageSource.getMessage(NEW_USER_MESSAGE, locale,
                                                                     JsonUtil.toPrettyJson(message.getChat()),
                                                                     user == null ? "---" : JsonUtil.toPrettyJson(
                                                                             users),
                                                                     ldt))
                                   .build();
            }

            communicationComponent.sendMessageToAdmin(messageData);
        } catch (Exception e) {
            log.warn("Error ==> infoAdmin", e);
        }
    }

    /**
     * @since 1.8.1.0
     */
    private LocalDateTime prepareDateTime(Long timestamp) {
        return DateTimeUtils.getOperationDate(timestamp);
    }

    /**
     * @since 1.0.0.0
     */
    private MessageData prepareMisunderstandingMessage(Locale locale) {
        return MessageData.builder()
                          .toAdmin(false)
                          .toResponse(false)
                          .message(messageSource.getMessage(
                                  MISUNDERSTANDING_MESSAGES.get(random.nextInt(MISUNDERSTANDING_MESSAGES.size())),
                                  locale))
                          .build();
    }

    /**
     * @since 0.1.3.0
     */
    private boolean noNeedToAnswer(Message message) {
        User user = message.getUser();
        if (user.getIsBot()) {
            log.debug("Message from bot - ignore.");

            return true;
        }

        Chat chat = message.getChat();
        if ((workType == WorkType.G && isPrivate(chat.getType())) || (workType == WorkType.P && isGroup(chat.getType()))) {
            log.debug("Chat type '{}' does not correspond to allowed type '{}'.",
                      chat :: getType,
                      () -> workType);

            return true;
        }

        List<MessageEntity> entities = message.getEntities();
        if (entities != null && entities.get(0).getType().equalsIgnoreCase("bot_command") && entities.get(0).getOffset() == 0) {
            return false;
        }

        log.debug("noNeedToAnswer method ==> deafault behaviour ==> ignore.");

        return true;
    }

    /**
     * @since 0.1.3.0
     */
    private MessageData prepareAnswerWithCommand(CommandHolder commandHolder,
                                                 Message message,
                                                 Locale locale) {
        Validate.notNull(commandHolder, "Command is not specified!");

        Chat chat = message.getChat();
        User user = message.getUser();
        final Command command = commandHolder.getCommand();

        switch (workType) {
            case G:
                if (Command.INFO == command) {
                    if (isGroup(chat.getType())) {
                        Long chatId = chat.getChatId();
                        MessageData messageData =
                                prepareInfoAnswer(meta.settings.get(chatId).getBotInfoMessage(), locale);
                        saveToLazyActionsBotInfo(messageData, chatId, message.getMessageId());

                        return messageData;
                    } else {
                        log.debug("Command '{}' is allowed in group chats bot only!", () -> command);

                        return null;
                    }
                } else if (Command.TEST == command) {
                    if (meta.settings.get(chat.getChatId()).getAdminsPrime().contains(user.getId())) {
                        MessageData messageData = MessageData.builder()
                                                             .message(defineRandomMessageTest(user))
                                                             .replyMarkup(defineRandomKeyTest())
                                                             .toAdmin(false)
                                                             .toResponse(false)
                                                             .userResponse(false)
                                                             .build();
                        saveToLazyActionsTestData(messageData, chat.getChatId(), message.getMessageId());

                        return messageData;
                    }
                    log.debug("Command '{}' is not allowed for not Admin!", () -> command);

                    return null;
                } else if (Command.CAS == command) {
                    final Long chatId = chat.getChatId();
                    if (meta.settings.get(chatId).getInfoChats().contains(chatId)) {
                        String messageCas;
                        try {
                            final CASData.CASResultData casData = antiBot.checkCAS(Long.valueOf((String) commandHolder.getData()))
                                                                         .getData();
                            if (casData == null || CollectionUtils.isEmpty(casData.getMessages())) {
                                messageCas = "Данных по бан-сообщениям нет";
                            } else {
                                int counter = 1;
                                final StringBuilder builder = new StringBuilder();
                                for (String casDataMessage : casData.getMessages()) {
                                    builder.append("<b>Бан-пост #").append(counter++).append("</b>").append("\n");
                                    builder.append("=====================").append("\n");
                                    builder.append(casDataMessage).append("\n");
                                    builder.append("=====================").append("\n\n");
                                }

                                messageCas = builder.toString();
                            }
                        } catch (Exception e) {
                            log.warn("CAS details exception!", e);
                            messageCas = messageSource.getMessage(SOMETHING_WRONG_MESSAGE, locale);
                        }

                        final MessageData messageData = MessageData.builder()
                                                                   .message(messageCas)
                                                                   .build();
                        saveToLazyActions(messageData, chatId, message.getMessageId(), LazyAction.DELETE,
                                          meta.settings.get(chatId).getHelloMessageDeleteTime());
                        saveToLazyActions(messageData, chatId, LazyAction.DELETE, meta.settings.get(chatId).getHelloMessageDeleteTime());

                        return messageData;
                    }
                } else {
                    log.debug("Command '{}' is not allowed in group chats bot!", () -> command);

                    return null;
                }
            case P:
                if (Command.START == command) {
                    if (isPrivate(chat.getType())) {
                        return prepareStartAnswer(locale);
                    } else {
                        log.debug("Command '{}' is allowed in \"tet-a-tet\" bot only!",
                                  () -> command);

                        return null;
                    }
                } else if (Command.INFO == command) {
                    log.debug("Command '{}' is allowed in group chats bot only!",
                              () -> command);

                    return null;
                }
            case B:
//                String messageToSendString;
//
//                if (Command.INFO == command) {
//                    MessageData messageData = prepareInfoAnswerAll(locale);
//                    saveToLazyActionsBotInfo(messageData, chat.getChatId(), message.getMessageId());
//
//                    return messageData;
//                } else if (Command.START == command) {
//                    return prepareStartAnswer(locale);
//                } else if (Command.D_ALL == command) {
//                    messageToSendString = prepareMessageForShopsToSendString(full, false, true, locale);
//                } else {
//                    Shop3D shop = command.getShop();
//                    Data3DPlastic plastic = searcher.search(shop);
//
//                    messageToSendString = prepareMessageForShopToSendString(plastic, shop, command, full, false, true,
//                                                                            locale);
//                }
//
//                if (StringUtils.isBlank(messageToSendString)) {
//                    messageToSendString = messageSource.getMessage(NO_DISCOUNTS, locale);
//                }
//
//                return MessageData.builder()
//                                  .toAdmin(false)
//                                  .toResponse(false)
//                                  .userResponse(WorkType.P == workType)
//                                  .showUrlPreview(false)
//                                  .message(messageToSendString)
//                                  .build();
            default:
                throw new UnsupportedOperationException(String.format("Unexpected work type: '%s'!", workType));
        }
    }

    /**
     * @since 1.8.3.0
     */
    private String defineRandomMessageTest(User user) {
        return String.format("<a href=\"tg://user?id=%s\">%s</a>, якщо Ви людина - натисніть на кнопку 😊",
                             user.getId(),
                             user.getFirstName());
    }

    /**
     * @since 1.8.3.0
     */
    private InlineKeyboard defineRandomKeyTest() {
        List<List<InlineKeyboard.Key>> keyboard = new ArrayList<>(1);
        keyboard.add(Lists.newArrayList(
                new InlineKeyboard.Key("callback_1_button", "First button"),
                new InlineKeyboard.Key("callback_2_button", "Second button")
        ));
        keyboard.add(Lists.newArrayList(
                new InlineKeyboard.Key("callback_3_button", "Third button"),
                new InlineKeyboard.Key("callback_4_button", "Forth button")
        ));

        return InlineKeyboard.builder()
                             .inlineKeyboard(keyboard)
                             .build();
    }

    /**
     * @since 0.1.3.0
     */
    private void saveToLazyActionsBotInfo(MessageData messageData,
                                          Long chatId,
                                          Long messageId) {
        Integer botInfoRequestDeleteTime = meta.settings.get(chatId).getBotInfoRequestDeleteTime();
        saveToLazyActions(messageData, chatId, LazyAction.DELETE, botInfoRequestDeleteTime);
        saveToLazyActions(messageData, chatId, messageId, LazyAction.DELETE, botInfoRequestDeleteTime);
    }

    /**
     * @since 0.1.3.0
     */
    private void saveToLazyActionsTestData(MessageData messageData,
                                           Long chatId,
                                           Long messageId) {
        Integer botInfoRequestDeleteTime = meta.settings.get(chatId).getBotInfoRequestDeleteTime();
        saveToLazyActions(messageData, chatId, LazyAction.DELETE, botInfoRequestDeleteTime);
        saveToLazyActions(messageData, chatId, messageId, LazyAction.DELETE, botInfoRequestDeleteTime);
    }

    /**
     * @since 0.1.3.0
     */
    private String doPrepareMessageToSendString(Command command,
                                                boolean full,
                                                boolean onlyDiscounts,
                                                Data3DPlastic plastic,
                                                Shop3D shop,
                                                Locale locale) {
        String messageToSendString;

        switch (command) {
            case D_3DP:
            case D_3DUA:
            case D_MF:
            case D_U3DF:
            case D_DAS:
            case D_PLEX:
                messageToSendString = full ? prepareAnswerText(plastic, shop, locale) :
                                      prepareAnswerTextShort(plastic, onlyDiscounts, locale);

                break;
            default:
                messageToSendString = messageSource.getMessage(NO_WORK_WITH_SHOP, locale);
        }

        return messageToSendString;
    }

    /**
     * @since 0.1.3.0
     */
    private String prepareAnswerTextShort(Data3DPlastic plastic,
                                          boolean onlyDiscounts,
                                          Locale locale) {
        Map<PlasticType, List<ParseData>> byName =
                plastic.getData().stream()
                       //                       .filter(ParseData :: isInStock)
                       .collect(Collectors.groupingBy(ParseData :: getType, HashMap :: new,
                                                      Collectors.toCollection(ArrayList :: new)));
        Map<PlasticType, PlasticPresenceDto> discountsState =
                byName.entrySet().stream()
                      .collect(Collectors.toMap(Map.Entry :: getKey,
                                                e -> definePlasticState(e.getValue())));

        if (onlyDiscounts) {
            discountsState = discountsState.entrySet().stream()
                                           .filter(e -> PlasticPresenceState.DISCOUNT == e.getValue()
                                                                                          .getPresenceState())
                                           .collect(Collectors.toMap(Map.Entry :: getKey, Map.Entry :: getValue));
        }

        StringBuilder answer = new StringBuilder();
        //        discountsState.entrySet().stream()
        //                      .sorted(Comparator.comparing(o -> o.getValue().getPresenceState()))
        //                      .forEach(s -> answer.append(messageSource.getMessage(SHORT_DISCOUNT_PART_MESSAGE, locale,
        //                                                                           alignText(s.getKey().getName()),
        //                                                                           getDiscountText(s.getValue().getPresenceState()),
        //                                                                           s.getValue().getMainUrl())));

        List<Map.Entry<PlasticType, PlasticPresenceDto>> entryList =
                discountsState.entrySet().stream()
                              .sorted(Comparator.comparing(o -> o.getValue().getPresenceState()))
                              .collect(Collectors.toList());
        for (int i = 0; i < entryList.size(); i++) {
            Map.Entry<PlasticType, PlasticPresenceDto> entry = entryList.get(i);
            answer.append(
                    messageSource.getMessage(SHORT_DISCOUNT_PART_MESSAGE, locale, alignText(entry.getKey().getName()),
                                             getDiscountText(entry.getValue().getPresenceState()),
                                             entry.getValue().getMainUrl()));
            if (i == entryList.size() - 1) {
                answer.append("\n");
            } else if ((i + 1) % 2 == 0) {
                answer.append("\n");
            } else {
                answer.append("   ");
            }
        }

        return answer.toString();
    }

    /**
     * @since 1.0.0.0
     */
    private String alignText(String text) {
        return String.format("%-5s", text);
    }

    /**
     * @since 1.4.3.0
     */
    private String getWhitespaces(String text) {
        String textWithWhitespaces = String.format("%-5s", text);
        return textWithWhitespaces.contains(" ") ? textWithWhitespaces.substring(textWithWhitespaces.indexOf(" ")) : "";
    }

    /**
     * @since 0.1.3.0
     */
    private String getDiscountText(PlasticPresenceState presenceState) {
        switch (presenceState) {
            case DISCOUNT:
                return "✅";
            case IN_STOCK:
                return "❌";
            case NOT_IN_STOCK:
                return "⛔️";
            default:
                throw new UnsupportedOperationException(
                        String.format("Unexpected plastic presence state: '%s'!", presenceState));
        }
    }

    /**
     * @since 0.1.3.0
     */
    private String prepareAnswerText(Data3DPlastic plastic,
                                     Shop3D shop,
                                     Locale locale) {
        List<ParseData> data = plastic.getData();

        StringBuilder answer = new StringBuilder();
        List<PlasticType> borderTypeList = new ArrayList<>(maxQuantity);
        List<String> borderUrlList = new ArrayList<>(maxQuantity);
        for (ParseData datum : data) {
            if (mayUsePlastic(datum) && !borderTypeList.contains(datum.getType())) {
                borderTypeList.add(datum.getType());
                borderUrlList.add(datum.getProductUrl());

                appendMainBody(answer, datum, locale);

                if (borderTypeList.size() == maxQuantity) {
                    appendFinishBody(answer, shop.getUrl(), locale);

                    return answer.toString();
                }
            }
        }

        int counter = borderTypeList.size();
        for (ParseData datum : data) {
            if (mayUsePlastic(datum) && !borderUrlList.contains(datum.getProductUrl())) {
                appendMainBody(answer, datum, locale);

                counter++;
            }

            if (counter == maxQuantity) {
                appendFinishBody(answer, shop.getUrl(), locale);

                break;
            }
        }

        return answer.toString();
    }

    /**
     * @since 1.4.2.0
     */
    private void appendFinishBody(StringBuilder answer,
                                  String url,
                                  Locale locale) {
        answer.append(messageSource.getMessage(FULL_DISCOUNT_OTHER_MESSAGE, locale, url));
    }

    /**
     * @since 1.4.2.0
     */
    private void appendMainBody(StringBuilder answer,
                                ParseData datum,
                                Locale locale) {
        String oldPrice = datum.getProductOldPrice() == null ? "-" : datum.getProductOldPrice().toString();
        String salePrice = datum.getProductSalePrice() == null ? "-" : datum.getProductSalePrice().toString();
        String discountPercent =
                datum.getProductDiscountPercent() == null ? "-" : datum.getProductDiscountPercent().toString();

        answer.append(messageSource.getMessage(FULL_DISCOUNT_PART_MESSAGE, locale, datum.getProductName(), oldPrice,
                                               salePrice, discountPercent, datum.getProductUrl()));
    }

    /**
     * @since 1.2.0.0
     */
    private boolean mayUsePlastic(ParseData datum) {
        return datum.isInStock() && ((datum.getProductOldPrice() != null && datum.getProductSalePrice() != null) || datum
                .getProductDiscountPercent() != null);
    }

    /**
     * @since 1.4.0.0
     */
    private PlasticPresenceDto definePlasticState(List<ParseData> data) {
        Set<PlasticPresenceState> states = new HashSet<>(PlasticPresenceState.values().length);
        data.forEach(d -> states.add(plasticState(d)));

        PlasticPresenceState state;
        if (states.contains(PlasticPresenceState.DISCOUNT)) {
            state = PlasticPresenceState.DISCOUNT;
        } else if (states.contains(PlasticPresenceState.IN_STOCK)) {
            state = PlasticPresenceState.IN_STOCK;
        } else {
            state = PlasticPresenceState.NOT_IN_STOCK;
        }

        return PlasticPresenceDto.builder()
                                 .presenceState(state)
                                 .mainUrl(data.get(0).getCommonUrl())
                                 .build();
    }

    /**
     * @since 1.4.0.0
     */
    private PlasticPresenceState plasticState(ParseData datum) {
        if (!datum.isInStock()) {
            return PlasticPresenceState.NOT_IN_STOCK;
        }

        return (datum.getProductOldPrice() != null && datum.getProductSalePrice() != null) || datum.getProductDiscountPercent() != null ?
               PlasticPresenceState.DISCOUNT : PlasticPresenceState.IN_STOCK;
    }

    /**
     * @since 0.1.3.0
     */
    private boolean hasNoCommand(List<MessageEntity> entities) {
        return entities == null || !entities.get(0).getType().equalsIgnoreCase("bot_command") || entities.get(
                0).getOffset() != 0;
    }

    /**
     * @since 0.1.3.0
     */
    private CommandHolder defineCommand(String text) {
        text = text.toUpperCase();
        if (OTHER_PATTERN.matcher(text).matches()) {
            String commandDataString = parseCommandDataString(text);
            commandDataString = commandDataString.replaceAll("/", "");
            Command command = Command.getByName(commandDataString);
            if (command == null) {
                throw new UnsupportedOperationException(String.format("Unexpected command! '%s'", text));
            }

            return CommandHolder.builder()
                                .command(command)
                                .full(false)
                                .build();
        }
        if (SALES_PATTERN.matcher(text).matches()) {
            String commandDataString = parseCommandDataString(text);
            String[] commandElements = commandDataString.split("_");
            Command command = Command.getByName(commandElements[1].toUpperCase());
            boolean full = commandElements.length == 3 && "f".equalsIgnoreCase(commandElements[2]);

            if (command == null) {
                throw new UnsupportedOperationException(String.format("Unexpected command! '%s'", text));
            }

            return CommandHolder.builder()
                                .command(command)
                                .full(full)
                                .build();
        }
        if (TEST_PATTERN.matcher(text).matches()) {
            String commandDataString = parseCommandDataString(text);
            commandDataString = commandDataString.replaceAll("/", "");
            Command command = Command.getByName(commandDataString);
            if (command == null) {
                throw new UnsupportedOperationException(String.format("Unexpected command! '%s'", text));
            }

            return CommandHolder.builder()
                                .command(command)
                                .full(false)
                                .build();
        }
        if (CAS_PATTERN.matcher(text).matches()) {
            String commandDataString = parseCommandDataString(text);

            return CommandHolder.builder()
                                .command(Command.CAS)
                                .data(commandDataString.substring(commandDataString.lastIndexOf("_") + 1))
                                .full(false)
                                .build();
        }

        throw new UnsupportedOperationException(String.format("Unexpected pattern! '%s'", text));
    }

    /**
     * 1.4.0.0
     */
    private String parseCommandDataString(String text) {
        String commandDataString = text.split(" ")[0];
        if (commandDataString.contains("@")) {
            commandDataString = commandDataString.substring(0, commandDataString.indexOf("@"));
        }

        return commandDataString;
    }

    // </editor-fold>
}

