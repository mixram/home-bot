package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Data;

/**
 * @author mixram on 2021-02-17.
 * @see <a href="https://core.telegram.org/bots/api#photosize">PhotoSize</a>
 * @since 1.8.8.0
 */
@Data
public class PhotoSize {

    /**
     * Identifier for this file, which can be used to download or reuse the file.
     */
    @JsonProperty("file_id")
    private String fileId;
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or
     * reuse the file.
     */
    @JsonProperty("file_unique_id")
    private String fileUniqueId;
    /**
     * Photo width.
     */
    @JsonProperty("width")
    private Integer width;
    /**
     * Photo height.
     */
    @JsonProperty("height")
    private Integer height;
    /**
     * Optional. File size.
     */
    @JsonProperty("file_size")
    private Long fileSize;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
