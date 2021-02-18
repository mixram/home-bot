package com.mixram.telegram.bot.services.services.market;

import com.mixram.telegram.bot.services.domain.entity.Message;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author mixram on 2021-02-16.
 * @since 1.8.8.0
 */
public interface MarketLogic {

    /**
     * To do stored lazy action.
     *
     * @since 1.8.8.0
     */
    void doPostponedAction();

    /**
     * To save message to Redis for postponed lazy action.<br> The message will be stored in Redis and processed some later with scheduler.
     * The result of scheduler work will be the message stored in Redis in lazy-actions-collection with
     *
     * @param message message.
     *
     * @since 1.8.8.0
     */
    void saveMessageToRedisForPostponedLazyAction(@Nonnull Message message);

    /**
     * To check if message is an advertisement.
     *
     * @param message message.
     *
     * @return true - message is advertisement, false - otherwise.
     *
     * @since 1.8.8.0
     */
    boolean isAdvertisement(@Nonnull Message message);

    /**
     * To do a bunch of actions if message is an advertisement.
     *
     * @param chatId    chat ID.
     * @param messageId message ID.
     *
     * @since 1.8.8.0
     */
    void doIfAdvertisement(@Nonnull Long chatId,
                           @Nonnull Long messageId);

    /**
     * To do a bunch of actions if messages are an advertisement.
     *
     * @param messages a list of messages.
     *
     * @since 1.8.8.0
     */
    void doIfAdvertisement(@Nonnull List<Message> messages);

    /**
     * To do a bunch of actions if message is not an advertisement.
     *
     * @param chatId    chat ID.
     * @param messageId message ID.
     *
     * @since 1.8.8.0
     */
    void doIfNotAdvertisement(@Nonnull Message message);

    /**
     * To do a bunch of actions if message is not an advertisement.
     *
     * @param messages messages.
     *
     * @since 1.8.8.0
     */
    void doIfNotAdvertisement(@Nonnull List<Message> messages);
}
