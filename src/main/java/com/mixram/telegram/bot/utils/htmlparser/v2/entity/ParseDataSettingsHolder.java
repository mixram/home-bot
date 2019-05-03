package com.mixram.telegram.bot.utils.htmlparser.v2.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mixram on 2019-05-03.
 * @since 1.4.2.0
 */
@Data
public class ParseDataSettingsHolder implements Serializable {

    private static final long serialVersionUID = 0L;

    @JsonProperty("settings")
    private List<ParseDataSettings> settings;
    @JsonProperty("shopUrl")
    private String shopUrl;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
