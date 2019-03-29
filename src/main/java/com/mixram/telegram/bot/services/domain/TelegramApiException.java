package com.mixram.telegram.bot.services.domain;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.0.0
 */
public class TelegramApiException extends RuntimeException {

    public TelegramApiException() {
        super();
    }

    public TelegramApiException(String message) {
        super(message);
    }

    public TelegramApiException(String message,
                                Throwable cause) {
        super(message, cause);
    }

    public TelegramApiException(Throwable cause) {
        super(cause);
    }

    protected TelegramApiException(String message,
                                   Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
