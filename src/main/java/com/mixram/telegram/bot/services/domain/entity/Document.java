package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mixram on 2021-02-16.
 * @since 1.8.8.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    /**
     * Identifier for this file, which can be used to download or reuse the file.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("file_id")
    private String fileId;
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or
     * reuse the file.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("file_unique_id")
    private String fileUniqueId;
    /**
     * Optional. Original filename as defined by sender.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("file_name")
    private String fileName;
    /**
     * Optional. MIME type of the file as defined by sender.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("mime_type")
    private String mimeType;
    /**
     * Optional. File size.
     *
     * @since 1.8.8.0
     */
    @JsonProperty("file_size")
    private Integer fileSize;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
