package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.services.domain.TelegramApiEntity;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An incoming message entity.
 *
 * @author mixram on 2019-03-29.
 * @apiNote official description is by <a href="https://core.telegram.org/bots/api#message">link</a>.
 * @since 0.1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements TelegramApiEntity {

    /**
     * Unique message identifier inside this chat.
     *
     * @since 0.1.0.0
     */
    @JsonProperty("message_id")
    private Integer messageId;
    /**
     * Optional. Sender, empty for messages sent to channels.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("user")
    private User user;
    /**
     * Date the message was sent in Unix time.
     *
     * @since 0.1.0.0
     */
    @JsonProperty("date")
    private Integer timestamp;
    /**
     * Conversation the message belongs to.
     *
     * @since 0.1.0.0
     */
    @JsonProperty("chat")
    private Chat chat;
    /**
     * Optional. For forwarded messages, sender of the original message.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("forward_from")
    private User forwardFrom;
    /**
     * Optional. For messages forwarded from channels, information about the original channel.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("forward_from_chat")
    private Chat forwardFromChat;
    /**
     * Optional. For messages forwarded from channels, identifier of the original message in the channel.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("forward_from_message_id")
    private Integer forwardFromMessageId;
    /**
     * Optional. For messages forwarded from channels, signature of the post author if present.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("forward_signature")
    private String forwardSignature;
    /**
     * Optional. For forwarded messages, date the original message was sent in Unix time.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("forward_date")
    private Integer forwardDate;
    /**
     * Optional. For replies, the original message. Note that the Message object in this field will not contain further
     * reply_to_message fields even if it itself is a reply.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("reply_to_message")
    private Message replyToMessage;
    /**
     * Optional. Date the message was last edited in Unix time.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("edit_date")
    private Integer editDate;
    /**
     * Optional. The unique identifier of a media message group this message belongs to.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("media_group_id")
    private String mediaGroupId;
    /**
     * Optional. Signature of the post author for messages in channels.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("author_signature")
    private String authorSignature;
    /**
     * For text messages, the actual UTF-8 text of the message, 0-4096 characters.
     *
     * @since 0.1.0.0
     */
    @JsonProperty("text")
    private String text;


    //TODO: to realize other response types

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
