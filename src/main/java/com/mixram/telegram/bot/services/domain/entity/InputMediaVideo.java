package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a photo to be sent.
 *
 * @author mixram on 2021-02-17.
 * @see <a href="https://core.telegram.org/bots/api#inputmediavideo">InputMediaPhoto</a>
 * @since 1.8.8.0
 */
@Getter
@Setter
@NoArgsConstructor
public class InputMediaVideo extends InputMediaThumbAbstr {

    /**
     * Type of the result, must be photo.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("type")
    private String type = "video";

    //TODO: there are some more implementations: https://core.telegram.org/bots/api#inputmedia

    /**
     * Need to be private for variable been immutable.
     */
    private void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
