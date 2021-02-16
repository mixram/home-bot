package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.services.domain.TelegramApiEntity;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mixram on 2019-04-12.
 * @since ...
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMessage implements TelegramApiEntity {

    /**
     * Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     *
     * @since 0.1.3.0
     */
    @JsonProperty("chat_id")
    private String chatId;
    /**
     * Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername).
     *
     * @since 1.8.8.0
     */
    @JsonProperty("from_chat_id")
    private String fromChatId;
    /**
     * Unique identifier of the target user (in case of kicking user).
     */
    @JsonProperty("user_id")
    private String userId;
    /**
     * Unique identifier of the target message (in case of message deleting).
     */
    @JsonProperty("message_id")
    private String messageId;
    /**
     * Date when the user will be unbanned, unix time. If user is banned for more than 366 days or less than 30 seconds
     * from the current time they are considered to be banned forever.
     */
    @JsonProperty("until_date")
    private Long untilDate;
    /**
     * Text of the message to be sent.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("text")
    private String text;
    /**
     * Optional. Send <a href="https://core.telegram.org/bots/api#markdown-style">Markdown</a> or
     * <a href="https://core.telegram.org/bots/api#html-style">HTML</a>, if you want Telegram apps to show bold, italic,
     * fixed-width text or inline URLs in your bot's message.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("parse_mode")
    private String parseMode;
    /**
     * Optional. Disables link previews for links in this message.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("disable_web_page_preview")
    private Boolean disableWebPagePreview;
    /**
     * Optional. Sends the message silently. Users will receive a notification with no sound.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("disable_notification")
    private Boolean disableNotification;
    /**
     * Optional. If the message is a reply, ID of the original message.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("reply_to_message_id")
    private Long replyToMessageId;
    /**
     * Optional. Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to
     * remove reply keyboard or to force a reply from the user.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("reply_markup")
    private Object replyMarkup;
    /**
     * A JSON-serialized object for new user permissions.
     *
     * @since 1.8.5.0
     */
    @JsonProperty("permissions")
    private ChatPermissions permissions;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
