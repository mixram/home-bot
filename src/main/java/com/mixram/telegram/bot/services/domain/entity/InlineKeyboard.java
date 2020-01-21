package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mixram on 2020-01-20.
 * @since 1.7.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InlineKeyboard {

    @JsonProperty("inline_keyboard")
    private List<List<Key>> inlineKeyboard;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Key {

        @JsonProperty("callback_data")
        private String callbackData;
        @JsonProperty("text")
        private String text;
    }
}
