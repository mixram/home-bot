package com.mixram.telegram.bot.service.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.service.interfaces.TelegramApiResponseEntity;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An incoming chat entity.
 *
 * @author mixram on 2018-04-25.
 * @apiNote official description is by <a href="https://core.telegram.org/bots/api#chat">link</a>.
 * @since 0.1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseEntity implements TelegramApiResponseEntity {

    /**
     * Unique identifier for this chat.<br>
     * This number may be greater than 32 bits and some programming languages may have difficulty/silent defects in
     * interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe
     * for storing this identifier.
     *
     * @since 0.1.0.0
     */
    @JsonProperty("id")
    private Long chatId;
    /**
     * Type of chat, can be either “private”, “group”, “supergroup” or “channel”.
     *
     * @since 0.1.0.0
     */
    @JsonProperty("type")
    private String type;
    /**
     * Title, for supergroups, channels and group chats.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("title")
    private String title;
    /**
     * Username, for private chats, supergroups and channels if available.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("username")
    private String userName;
    /**
     * First name of the other party in a private chat.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("first_name")
    private String firstName;
    /**
     * Last name of the other party in a private chat.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("last_name")
    private String lastName;
    /**
     * True if a group has ‘All Members Are Admins’ enabled.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("all_members_are_administrators")
    private Boolean allMembersAreAdministrators;
    /**
     * Chat photo. Returned only in getChat.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("photo")
    private ChatPhotoResponseEntity photo;
    /**
     * Description, for supergroups and channel chats. Returned only in getChat.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("description")
    private String description;
    /**
     * Chat invite link, for supergroups and channel chats. Returned only in getChat.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("invite_link")
    private String inviteLink;
    /**
     * Pinned message, for supergroups and channel chats. Returned only in getChat.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("pinned_message")
    private MessageResponseEntity pinnedMessage;
    /**
     * For supergroups, name of group sticker set. Returned only in getChat.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("sticker_set_name")
    private String stickerSetName;
    /**
     * True, if the bot can change the group sticker set. Returned only in getChat.
     *
     * @apiNote the parameter is optional!
     * @since 0.1.0.0
     */
    @JsonProperty("can_set_sticker_set")
    private Boolean canSetStickerSet;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
