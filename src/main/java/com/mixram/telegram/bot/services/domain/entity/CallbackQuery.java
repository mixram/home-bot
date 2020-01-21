package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object represents an incoming callback query from a callback button in an inline keyboard.
 *
 * @author mixram on 2020-01-20.
 * @since 1.7.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallbackQuery {

    /**
     * Unique identifier for this query.
     *
     * @since 1.7.0.0
     */
    @JsonProperty("id")
    private String id;
    /**
     * Optional. Data associated with the callback button. Be aware that a bad client can send arbitrary data in this field.
     *
     * @since 1.7.0.0
     */
    @JsonProperty("data")
    private String data;
    /**
     * Sender.
     *
     * @since 1.7.0.0
     */
    @JsonProperty("from")
    private User user;
    /**
     * Optional. Message with the callback button that originated the query. Note that message content and message date will
     * not be available if the message is too old.
     *
     * @since 1.7.0.0
     */
    @JsonProperty("message")
    private Message message;
    /**
     * Global identifier, uniquely corresponding to the chat to which the message with the callback button was sent.
     *
     * @since 1.7.0.0
     */
    @JsonProperty("chat_instance")
    private Long chatId;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
