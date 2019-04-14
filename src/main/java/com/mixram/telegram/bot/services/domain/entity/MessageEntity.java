package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.services.domain.TelegramApiEntity;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mixram on 2019-04-12.
 * @since 0.1.3.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity implements TelegramApiEntity {

    /**
     * Type of the entity. Can be mention (@username), hashtag, cashtag, bot_command, url, email, phone_number,
     * bold (bold text), italic (italic text), code (monowidth string), pre (monowidth block), text_link (for clickable text
     * URLs), text_mention (for users <a href="https://telegram.org/blog/edit#new-mentions">without usernames</a>).
     *
     * @since 0.1.3.0
     */
    @JsonProperty("type")
    private String type;
    /**
     * Offset in UTF-16 code units to the start of the entity.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("offset")
    private Integer offset;
    /**
     * Length of the entity in UTF-16 code units.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("length")
    private Integer length;
    /**
     * Optional. For “text_link” only, url that will be opened after user taps on the text.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("url")
    private String url;
    /**
     * Optional. For “text_mention” only, the mentioned user.
     *
     * @since 0.1.3.0
     */
    @JsonProperty("user")
    private User user;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
