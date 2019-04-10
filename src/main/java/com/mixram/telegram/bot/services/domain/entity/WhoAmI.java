package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Data;

/**
 * @author mixram on 2019-04-10.
 * @since ...
 */
@Data
public class WhoAmI {

    @JsonProperty("ok")
    private Boolean result;
    @JsonProperty("result")
    private WhoAmIResult resultData;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }


    @Data
    public static class WhoAmIResult {

        @JsonProperty("id")
        private String id;
        @JsonProperty("is_bot")
        private Boolean isBot;
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("username")
        private String username;

        @Override
        public String toString() {
            return JsonUtil.toJson(this);
        }
    }
}
