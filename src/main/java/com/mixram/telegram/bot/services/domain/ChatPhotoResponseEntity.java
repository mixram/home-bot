package com.mixram.telegram.bot.services.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An incoming photo entity.
 *
 * @author mixram on 2019-03-29.
 * @apiNote official description is by <a href="https://core.telegram.org/bots/api#chatphoto">link</a>.
 * @since 0.1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatPhotoResponseEntity implements TelegramApiResponseEntity {

    /**
     * Unique file identifier of small (160x160) chat photo. This file_id can be used only for photo download.
     *
     * @since 0.1.0.0
     */
    @JsonProperty("small_file_id")
    private String smallFileId;
    /**
     * Unique file identifier of big (640x640) chat photo. This file_id can be used only for photo download.
     *
     * @since 0.1.0.0
     */
    @JsonProperty("big_file_id")
    private String bigFileId;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
