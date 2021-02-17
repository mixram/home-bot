package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mixram on 2021-02-17.
 * @since 1.8.8.0
 */
@Getter
@Setter
public class InputMediaThumbAbstr extends InputMediaAbstr {

    /**
     * Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("thumb")
    private String thumb;


    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
