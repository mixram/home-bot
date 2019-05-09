package com.mixram.telegram.bot.utils.htmlparser.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Data;

/**
 * @author mixram on 2019-05-02.
 * @since 1.4.2.0
 */
@Data
public class ParseDataSettings {

    @JsonProperty("type")
    private PlasticType type;
    @JsonProperty("commonUrl")
    private String commonUrl;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
