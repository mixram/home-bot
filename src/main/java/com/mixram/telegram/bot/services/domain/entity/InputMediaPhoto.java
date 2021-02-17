package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a photo to be sent.
 *
 * @author mixram on 2021-02-17.
 * @see <a href="https://core.telegram.org/bots/api#inputmediaphoto">InputMediaPhoto</a>
 * @since 1.8.8.0
 */
@Getter
@Setter
@NoArgsConstructor
public class InputMediaPhoto extends InputMediaAbstr {

    /**
     * Type of the result, must be photo.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("type")
    private String type = "photo";

    /**
     * Need to be private for variable been immutable.
     */
    private void setType(String type) {
        this.type = type;
    }

    public InputMediaPhoto(String media) {
        super(media);
    }

    public InputMediaPhoto(String media,
                           String caption,
                           String parseMode,
                           List<MessageEntity> captionEntities) {
        super(media, caption, parseMode, captionEntities);
    }

    public InputMediaPhoto(String media,
                           String caption,
                           List<MessageEntity> captionEntities) {
        super(media, caption, null, captionEntities);
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
