package com.mixram.telegram.bot.service.interfaces;

import com.mixram.telegram.bot.service.pojo.UpdateResponseEntity;

import java.util.List;

/**
 * Interface for <a href="https://en.wikipedia.org/wiki/Push_technology#Long_polling">LongPooling-style</a> callback.
 *
 * @author mixram on 2018-04-25.
 * @since 0.1.0.0
 */
public interface LongPollingExecutor {

    /**
     * This method is called when receiving updates via GetUpdates method.
     *
     * @param update received update data.
     *
     * @since 0.1.0.0
     */
    void onUpdate(UpdateResponseEntity update);

    /**
     * This method is called when receiving updates via GetUpdates method.
     *
     * @param updates received updates data.
     *
     * @since 0.1.0.0
     */
    void onUpdates(List<UpdateResponseEntity> updates);

    /**
     * To return bot username of the bot.
     *
     * @since 0.1.0.0
     */
    String getBotUsername();

    /**
     * To return bot token of the bot to access Telegram API.
     *
     * @since 0.1.0.0
     */
    String getBotToken();

    /**
     * To get options for the bot.
     *
     * @return options information.
     *
     * @since 0.1.0.0
     */
    BotOptions getOptions();

    /**
     * Called when the BotSession is being closed.
     *
     * @since 0.1.0.0
     */
    default void onClosing() {
    }
}
