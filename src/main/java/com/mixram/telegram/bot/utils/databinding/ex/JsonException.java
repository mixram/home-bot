package com.mixram.telegram.bot.utils.databinding.ex;

/**
 * @author mixram on 2017-07-17
 * @since 0.1.0.0
 */
public class JsonException extends RuntimeException {

    public JsonException() {
        super();
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message,
                         Throwable cause) {
        super(message, cause);
    }

    public JsonException(Throwable cause) {
        super(cause);
    }
}
