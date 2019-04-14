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
    private Integer replyToMessageId;
    /**
     * Optional. Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     * instructions to remove reply keyboard or to force a reply from the user.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("reply_markup")
    private Object replyMarkup;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
