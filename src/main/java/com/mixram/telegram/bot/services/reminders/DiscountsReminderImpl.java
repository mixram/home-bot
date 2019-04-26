package com.mixram.telegram.bot.services.reminders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mixram.telegram.bot.services.domain.DiscountsListener;
import com.mixram.telegram.bot.services.domain.entity.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Command;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.services.modules.DiscountsOn3DPlasticModule;
import com.mixram.telegram.bot.services.modules.Module3DPlasticDataSearcher;
import com.mixram.telegram.bot.services.services.bot.Bot3DComponentImpl;
import com.mixram.telegram.bot.services.services.bot.entity.MessageData;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
import com.mixram.telegram.bot.utils.CustomMessageSource;
import com.mixram.telegram.bot.utils.META;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import com.mixram.telegram.bot.utils.htmlparser.ParseData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mixram on 2019-04-26.
 * @since 1.4.1.0
 */
@Log4j2
@Component
public class DiscountsReminderImpl implements DiscountsReminder, DiscountsListener {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String REMINDER_MESSAGE = "telegram.bot.message.reminder.discounts";
    private static final String NEW_DISCOUNTS_AVAILABLE_MESSAGE = "telegram.bot.message.new-discounts-available";
    private static final String NEW_DISCOUNTS_ERROR_MESSAGE = "telegram.bot.message.new-discounts-error";

    private final Bot3DComponentImpl bot3DComponentImpl;
    private final TelegramAPICommunicationComponent communicationComponent;
    private final CustomMessageSource messageSource;
    private final Module3DPlasticDataSearcher searcher;

    private final Set<String> chatsIds;
    private final boolean enableNewDscountsReminder;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsReminderImpl(
            @Value("${bot.settings.work-with-groups}") String chatsIds,
            @Value("${bot.settings.scheduler.reminders.new-discount.enable}") boolean enableNewDscountsReminder,
            @Qualifier("discountsOn3DPlasticDataCacheComponent") Module3DPlasticDataSearcher searcher,
            Bot3DComponentImpl bot3DComponentImpl,
            TelegramAPICommunicationComponent communicationComponent,
            CustomMessageSource messageSource) {
        this.bot3DComponentImpl = bot3DComponentImpl;
        this.communicationComponent = communicationComponent;
        this.messageSource = messageSource;
        this.searcher = searcher;

        this.chatsIds = JsonUtil.fromJson(chatsIds, new TypeReference<Set<String>>() {});
        this.enableNewDscountsReminder = enableNewDscountsReminder;
    }

    // </editor-fold>


    @Override
    public void remind() {
        String messagePart = bot3DComponentImpl.prepareMessageForShopsToSendString(false, true, META.DEFAULT_LOCALE);

        doSendToChats(REMINDER_MESSAGE, messagePart, META.DEFAULT_LOCALE);
    }

    @Override
    public void discount() {
        if (!enableNewDscountsReminder) {
            log.info("Reminder about new discounts is switched off!");
        }
        log.info("{}#discount() is started!", DiscountsOn3DPlasticModule.class :: getSimpleName);

        Map<Shop3D, Data3DPlastic> oldPlasticsData = getOldPlastics();
        if (oldPlasticsData == null) {
            log.info("No old data about plastics - can not calculate discounts.");

            return;
        }

        Map<Shop3D, Data3DPlastic> newPlasticsData = getPlastics();
        if (newPlasticsData == null) {
            log.info("No new data about plastics - can not calculate discounts.");

            return;
        }

        try {
            Map<Shop3D, List<ParseData>> newPlasticsWithDMap = new HashMap<>(Shop3D.values().length);
            for (Shop3D shop : Shop3D.values()) {
                Data3DPlastic oldPlasticHolder = oldPlasticsData.get(shop);
                Data3DPlastic newPlasticHolder = newPlasticsData.get(shop);

                List<ParseData> oldPlastics = oldPlasticHolder.getData();
                List<ParseData> newPlastics = newPlasticHolder.getData();
                List<ParseData> newPlasticsWithD =
                        newPlastics.stream()
                                   .filter(np -> np.getProductOldPrice() != null)
                                   .filter(np -> {
                                       ParseData pl = oldPlastics.stream()
                                                                 .filter(op -> np.getProductUrl().equals(op.getProductUrl()))
                                                                 .findFirst()
                                                                 .orElse(null);
                                       if (pl == null || pl.getProductOldPrice() == null) {
                                           return true;
                                       }

                                       return np.getProductSalePrice() != null && np.getProductSalePrice().compareTo(
                                               pl.getProductSalePrice()) < 0;

                                   })
                                   .collect(Collectors.toList());

                if (CollectionUtils.isEmpty(newPlasticsWithD)) {
                    continue;
                }

                newPlasticsWithDMap.put(shop, newPlasticsWithD);
            }

            if (newPlasticsWithDMap.isEmpty()) {
                return;
            }

            StringBuilder builder = new StringBuilder();
            newPlasticsWithDMap.forEach((k, v) -> {
                Data3DPlastic plastic = Data3DPlastic.builder()
                                                     .shop(k)
                                                     .data(v)
                                                     .build();
                String mess = bot3DComponentImpl.prepareMessageForShopToSendString(plastic, k, Command.getByShop(k), false,
                                                                                   true, META.DEFAULT_LOCALE);
                builder
                        .append(messageSource.getMessage(Bot3DComponentImpl.SHOP_MESSAGE_PART_MESSAGE, META.DEFAULT_LOCALE,
                                                         k.getName(), mess));
            });

            doSendToChats(NEW_DISCOUNTS_AVAILABLE_MESSAGE, builder.toString(), META.DEFAULT_LOCALE);
        } catch (Exception e) {
            log.warn("New discounts reminder exception!", e);

            try {
                communicationComponent.sendMessageToAdmin(MessageData.builder()
                                                                     .message(messageSource.getMessage(
                                                                             NEW_DISCOUNTS_ERROR_MESSAGE, Locale.ENGLISH))
                                                                     .toAdmin(true)
                                                                     .build());
            } catch (Exception ex) {
                log.warn("", ex);
            }
        }
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.4.1.0
     */
    private void doSendToChats(String mainMessage,
                               String additionalMessage,
                               Locale locale) {
        String message = messageSource.getMessage(mainMessage, locale, additionalMessage);

        MessageData data = MessageData.builder()
                                      .message(message)
                                      .build();

        chatsIds.forEach(c -> communicationComponent.sendMessageToChat(Long.valueOf(c), data));
    }

    /**
     * @since 1.4.1.0
     */
    private Map<Shop3D, Data3DPlastic> getPlastics() {
        Map<Shop3D, Data3DPlastic> plastics = new HashMap<>(Shop3D.values().length);

        for (Shop3D shop : Shop3D.values()) {
            plastics.put(shop, searcher.search(shop));
        }

        return plastics;
    }

    /**
     * @since 1.4.1.0
     */
    private Map<Shop3D, Data3DPlastic> getOldPlastics() {
        Map<Shop3D, Data3DPlastic> plastics = new HashMap<>(Shop3D.values().length);

        for (Shop3D shop : Shop3D.values()) {
            plastics.put(shop, searcher.searchOld(shop));
        }

        return plastics;
    }

    // </editor-fold>
}
