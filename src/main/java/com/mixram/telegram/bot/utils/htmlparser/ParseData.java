package com.mixram.telegram.bot.utils.htmlparser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mixram on 2019-04-09.
 * @since 0.2.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParseData {

    private String pageTitle;
    private String productName;
    private String productUrl;
    private String productPrice;
    private String productSalePrice;
}
