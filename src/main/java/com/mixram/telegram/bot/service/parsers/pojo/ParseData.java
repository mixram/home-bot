package com.mixram.telegram.bot.service.parsers.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pojo for holding parsing results.
 *
 * @author mixram on 2018-05-12.
 * @since 0.1.1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParseData {

    private String pageTitle;
    private String productName;
    private String productUrl;
    private String productPrice;
    private String productSalePrice;
}
