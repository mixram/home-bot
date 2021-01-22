package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Data;

import java.util.List;

/**
 * @author mixram on 2021-01-22.
 * @since 1.8.5.0
 */
@Data
public class CASData {

    @JsonProperty("ok")
    private boolean isBaned;
    @JsonProperty("result")
    private CASResultData data;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }


    @Data
    public static class CASResultData {

        @JsonProperty("offenses")
        private Integer offenses;
        @JsonProperty("messages")
        private List<String> messages;
        @JsonProperty("time_added")
        private String timeAdded;

        @Override
        public String toString() {
            return JsonUtil.toJson(this);
        }
    }
}
