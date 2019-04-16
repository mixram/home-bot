package com.mixram.telegram.bot.utils.htmlparser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author mixram on 2019-04-09.
 * @since 0.1.1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParseData implements Serializable {

    private static final long serialVersionUID = 0L;

    @JsonProperty("title")
    private String pageTitle;
    @JsonProperty("name")
    private String productName;
    @JsonProperty("url")
    private String productUrl;
    @JsonProperty("oldPrice")
    private BigDecimal productOldPrice;
    @JsonProperty("salePrice")
    private BigDecimal productSalePrice;
    @JsonProperty("type")
    private PlasticType type;
    @JsonProperty("inStock")
    private boolean isInStock;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
