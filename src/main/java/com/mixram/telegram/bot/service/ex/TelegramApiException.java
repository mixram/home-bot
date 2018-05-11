package com.mixram.telegram.bot.service.ex;

/**
 * @author mixram on 2018-04-25.
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
