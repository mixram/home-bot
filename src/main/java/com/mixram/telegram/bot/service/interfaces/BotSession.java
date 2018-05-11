package com.mixram.telegram.bot.service.interfaces;

/**
 * Interface for Telegram bot`s session.
 *
 * @author mixram on 2018-04-25.
 * @since 0.1.0.0
 */
public interface BotSession {

    void setOptions(BotOptions options);

    void setToken(String token);

    void setCallback(LongPollingExecutor callback);

    /**
     * Starts the bot.
     *
     * @since 0.1.0.0
     */
    void start();

    /**
     * Stops the bot.
     *
     * @since 0.1.0.0
     */
    void stop();

    /**
     * Check if the bot is running.
     *
     * @return true - the bot is running, false - otherwise.
     *
     * @since 0.1.0.0
     */
    boolean isRunning();

}
