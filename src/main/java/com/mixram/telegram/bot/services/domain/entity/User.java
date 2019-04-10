package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Data;

/**
 * This object represents a Telegram user or bot.
 *
 * @author mixram on 2019-04-10.
 * @since 0.1.2.0
 */
@Data
public class User {

    /**
     * Unique identifier for this user or bot.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("id")
    private Integer id;
    /**
     * True, if this user is a bot.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("is_bot")
    private Boolean isBot;
    /**
     * User‘s or bot’s first name.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("first_name")
    private String firstName;
    /**
     * Optional. User‘s or bot’s last name.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("last_name")
    private String lastName;
    /**
     * Optional. User‘s or bot’s username.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("username")
    private String username;
    /**
     * Optional. <a href="https://en.wikipedia.org/wiki/IETF_language_tag">IETF language tag</a> of the user's language.
     *
     * @since 0.1.2.0
     */
    @JsonProperty("language_code")
    private String languageCode;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
