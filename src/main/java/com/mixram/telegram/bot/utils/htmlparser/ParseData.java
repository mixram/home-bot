package com.mixram.telegram.bot.utils.htmlparser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author mixram on 2019-04-09.
 * @since 0.1.1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParseData {

    private String pageTitle;
    private String productName;
    private String productUrl;
    private BigDecimal productOldPrice;
    private BigDecimal productSalePrice;
}
