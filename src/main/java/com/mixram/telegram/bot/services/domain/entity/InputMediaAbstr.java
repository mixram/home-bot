package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.services.domain.InputMedia;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mixram on 2021-02-17.
 * @since 1.8.8.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class InputMediaAbstr implements InputMedia {

    /**
     * File to send (or file ID).
     *
     * @since 1.8.8.0
     */
    @JsonProperty("media")
    private String media;
    /**
     * Optional. Caption of the photo to be sent, 0-1024 characters after entities parsing.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("caption")
    private String caption;
    /**
     * Optional. Mode for parsing entities in the photo caption. See formatting options for more details.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("parse_mode")
    private String parseMode;
    /**
     * Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    public InputMediaAbstr(String media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
