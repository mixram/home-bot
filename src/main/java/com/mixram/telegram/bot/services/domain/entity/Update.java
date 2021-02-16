package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.services.domain.TelegramApiEntity;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An incoming update entity.
 *
 * @author mixram on 2019-03-29.
 * @apiNote official description is by <a href="https://core.telegram.org/bots/api#update">link</a>.
 * @since 0.1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Update implements TelegramApiEntity {

    /**
     * The update`s unique identifier.<br> Update identifiers start from a certain positive number and increase sequentially. This ID
     * becomes especially handy if you’re using Webhooks, since it allows you to ignore repeated updates or to restore the correct update
     * sequence, should they get out of order. If there are no new updates for at least a week, then identifier of the next update will be
     * chosen randomly instead of sequentially.
     *
     * @since 0.1.0.0
     */
    @JsonProperty("update_id")
    private Long updateId;
    /**
     * New incoming message of any kind — text, photo, sticker, etc.<br>
     *
     * @apiNote the parameter is optional!
     * @see <a href="https://core.telegram.org/bots/api#message">Message</a>
     * @since 0.1.0.0
     */
    @JsonProperty("message")
    private Message message;
    /**
     * New version of a message that is known to the bot and was edited.<br>
     *
     * @apiNote the parameter is optional!
     * @see <a href="https://core.telegram.org/bots/api#message">Message</a>
     * @since 1.8.8.0
     */
    @JsonProperty("edited_message")
    private Message editedMessage;
    /**
     * New incoming channel post of any kind — text, photo, sticker, etc.<br>
     *
     * @apiNote the parameter is optional!
     * @see <a href="https://core.telegram.org/bots/api#message">Message</a>
     * @since 1.8.8.0
     */
    @JsonProperty("channel_post")
    private Message channelPost;
    /**
     * New version of a channel post that is known to the bot and was edited.<br>
     *
     * @apiNote the parameter is optional!
     * @see <a href="https://core.telegram.org/bots/api#message">Message</a>
     * @since 1.8.8.0
     */
    @JsonProperty("edited_channel_post")
    private Message editedChannelPost;
    /**
     * This object represents an incoming callback query from a callback button in an inline keyboard.<br>
     *
     * @apiNote the parameter is optional!
     * @see <a href="https://core.telegram.org/bots/api#callbackquery">CallbackQuery</a>
     * @since 1.7.0.0
     */
    @JsonProperty("callback_query")
    private CallbackQuery callbackQuery;

    //TODO: to realize other response types

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
