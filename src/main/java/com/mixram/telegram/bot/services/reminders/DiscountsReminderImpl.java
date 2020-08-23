package com.mixram.telegram.bot.services.reminders;

import com.google.common.collect.Lists;
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
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseData;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    //    private static final String REMINDER_MESSAGE = "telegram.bot.message.reminder.discounts.v2";
    private static final String NEW_DISCOUNTS_AVAILABLE_MESSAGE = "telegram.bot.message.new-discounts-available";
    //    private static final String NEW_DISCOUNTS_AVAILABLE_MESSAGE = "telegram.bot.message.new-discounts-available.v2";
    private static final String NEW_DISCOUNTS_ERROR_MESSAGE = "telegram.bot.message.new-discounts-error";

    private final Bot3DComponentImpl bot3DComponentImpl;
    private final TelegramAPICommunicationComponent communicationComponent;
    private final CustomMessageSource messageSource;
    private final Module3DPlasticDataSearcher searcher;
    private final META meta;

    private final boolean enableNewDiscountsReminder;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsReminderImpl(
            @Value("${bot.settings.scheduler.reminders.new-discount.enable}") boolean enableNewDiscountsReminder,
            @Qualifier("discountsOn3DPlasticDataCacheComponent") Module3DPlasticDataSearcher searcher,
            META meta,
            Bot3DComponentImpl bot3DComponentImpl,
            TelegramAPICommunicationComponent communicationComponent,
            CustomMessageSource messageSource) {
        this.bot3DComponentImpl = bot3DComponentImpl;
        this.communicationComponent = communicationComponent;
        this.messageSource = messageSource;
        this.searcher = searcher;
        this.meta = meta;

        this.enableNewDiscountsReminder = enableNewDiscountsReminder;
    }

    // </editor-fold>


    @Override
    public void remind() {
        String messagePart = bot3DComponentImpl.prepareMessageForShopsToSendString(false, true, false,
                                                                                   META.DEFAULT_LOCALE);
        if (StringUtils.isNotBlank(messagePart)) {
            doSendRemindersToChats(REMINDER_MESSAGE, messagePart, META.DEFAULT_LOCALE);
        } else {
            log.info("No discounts to remind the chat(s).");
        }
    }

    @Override
    public void discount() {
        if (!enableNewDiscountsReminder) {
            log.info("Reminder about new discounts is switched off!");

            return;
        }
        log.info("{}#discount() is started!", DiscountsOn3DPlasticModule.class::getSimpleName);

        Map<Shop3D, Data3DPlastic> oldPlasticsData = getOldPlastics();
        Map<Shop3D, Data3DPlastic> newPlasticsData = getPlastics();

        try {
            Map<Shop3D, List<ParseData>> newPlasticsWithDMap = new HashMap<>(Shop3D.values().length);
            for (Shop3D shop : Shop3D.values()) {
                Data3DPlastic oldPlasticHolder = oldPlasticsData.get(shop);
                Data3DPlastic newPlasticHolder = newPlasticsData.get(shop);
                List<ParseData> oldPlastics = oldPlasticHolder == null || oldPlasticHolder.getData() == null ?
                        Lists.newArrayListWithExpectedSize(0) : oldPlasticHolder.getData();
                List<ParseData> newPlastics = newPlasticHolder == null || newPlasticHolder.getData() == null ?
                        Lists.newArrayListWithExpectedSize(0) : newPlasticHolder.getData();

                List<ParseData> newPlasticsWithD =
                        newPlastics.stream()
                                   .filter(np -> hasDiscount(np, oldPlastics, shop))
                                   .collect(Collectors.toList());
                if (CollectionUtils.isEmpty(newPlasticsWithD)) {
                    continue;
                }

                newPlasticsWithDMap.put(shop, newPlasticsWithD);
            }

            if (newPlasticsWithDMap.isEmpty()) {
                log.info("No new discounts to inform about");

                return;
            }

            StringBuilder builder = new StringBuilder();
            newPlasticsWithDMap.forEach((k, v) -> {
                Data3DPlastic plastic = Data3DPlastic.builder()
                                                     .shop(k)
                                                     .data(v)
                                                     .build();
                String mess = bot3DComponentImpl.prepareMessageForShopToSendString(plastic, k, Command.getByShop(k),
                                                                                   false,
                                                                                   true, true, META.DEFAULT_LOCALE);
                builder
                        .append(messageSource.getMessage(Bot3DComponentImpl.SHOP_MESSAGE_PART_MESSAGE,
                                                         META.DEFAULT_LOCALE,
                                                         k.getUrl(), k.getNameAlt(), mess));
            });

            doSendNewDiscountsRemindersToChats(NEW_DISCOUNTS_AVAILABLE_MESSAGE, builder.toString(),
                                               META.DEFAULT_LOCALE);
        } catch (Exception e) {
            log.warn("New discounts reminder exception!", e);

            try {
                communicationComponent.sendMessageToAdmin(MessageData.builder()
                                                                     .message(messageSource.getMessage(
                                                                             NEW_DISCOUNTS_ERROR_MESSAGE,
                                                                             Locale.ENGLISH))
                                                                     .toAdmin(true)
                                                                     .build());
            } catch (Exception ex) {
                log.warn("", ex);
            }
        }
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.4.3.2
     */
    private boolean hasDiscount(ParseData newPlastic,
                                List<ParseData> oldPlastics,
                                Shop3D shop) {
        if (Shop3D.SHOP_MONOFILAMENT == shop && newPlastic.getProductDiscountPercent() != null) {
            ParseData pl = oldPlastics.stream()
                                      .filter(op -> newPlastic.getProductUrl().equals(op.getProductUrl()))
                                      .findFirst()
                                      .orElse(null);
            if (pl == null || pl.getProductDiscountPercent() == null) {
                return true;
            }

            return newPlastic.getProductDiscountPercent() != null && newPlastic.getProductDiscountPercent().compareTo(
                    pl.getProductDiscountPercent()) < 0;
        } else if (newPlastic.getProductOldPrice() != null) {
            ParseData pl = oldPlastics.stream()
                                      .filter(op -> newPlastic.getProductUrl().equals(op.getProductUrl()))
                                      .findFirst()
                                      .orElse(null);
            if (pl == null || pl.getProductOldPrice() == null) {
                return true;
            }

            return newPlastic.getProductSalePrice() != null && newPlastic.getProductSalePrice().compareTo(
                    pl.getProductSalePrice()) < 0;
        }

        return false;
    }

    /**
     * @since 1.4.1.0
     */
    private void doSendRemindersToChats(String mainMessage,
                                        String additionalMessage,
                                        Locale locale) {
        MessageData data = prepareMessage(mainMessage, additionalMessage, locale);

        meta.settings.forEach((groupId, settings) -> {
            if (settings.getReminders()) {
                communicationComponent.sendMessageToChat(groupId, data);
            }
        });
    }

    /**
     * @since 1.8.0.0
     */
    private void doSendNewDiscountsRemindersToChats(String mainMessage,
                                                    String additionalMessage,
                                                    Locale locale) {
        MessageData data = prepareMessage(mainMessage, additionalMessage, locale);

        meta.settings.forEach((groupId, settings) -> {
            if (settings.getNewDiscountReminder()) {
                communicationComponent.sendMessageToChat(groupId, data);
            }
        });
    }

    /**
     * @since 1.8.0.0
     */
    private MessageData prepareMessage(String mainMessage,
                                       String additionalMessage,
                                       Locale locale) {
        return MessageData.builder()
                          .message(messageSource.getMessage(mainMessage, locale, additionalMessage))
                          .build();
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
