package com.mixram.telegram.bot.services.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author mixram on 2019-04-10.
 * @since ...
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResponse {

    @JsonProperty("ok")
    private Boolean result;
    @JsonProperty("result")
    private List<Update> data;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
