package com.mixram.telegram.bot.utils.htmlparser;

import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author mixram on 2019-04-09.
 * @since 0.1.1.0
 */
@Log4j2
@Service
public class HtmlPage3DPlastParserV2 extends HtmlPageShopParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final String productNameSelectorName;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    public HtmlPage3DPlastParserV2(
            @Value("${parser.3dplast.search-names.old-price-class-name.name}") String oldPriceClassName,
            @Value("${parser.3dplast.search-names.new-price-class-name.name}") String newPriceClassName,
            @Value("${parser.3dplast.search-names.product-name-selector-name.name}") String productNameSelectorName,
            @Value("${parser.3dplast.search-names.product-presence-selector-name.name}") String productAvailableSelectorName,
            @Value("${parser.3dplast.search-names.product-presence-pattern-name.name}") String productAvailableTextName) {
        super(oldPriceClassName, newPriceClassName, productAvailableSelectorName, productAvailableTextName);
        this.productNameSelectorName = productNameSelectorName;
    }

    // </editor-fold>

    /**
     * @since 0.1.0.0
     */
    @Override
    public ParseData parse(ParseData parseData) {
        return super.parse(parseData);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 0.1.0.0
     */
    @Override
    protected BigDecimal parsePrice(Elements elements) {
        String priceText = elements.first().text();
        String[] split = priceText.split(" ");
        String sumString = split[0].replace(",", ".");

        return new BigDecimal(sumString);
    }

    /**
     * @since 0.2.0.0
     */
    protected String getProductName(Document doc) {
        Elements nameElements = doc.select(productNameSelectorName);

        return nameElements.isEmpty() ? null : nameElements.first().text();
    }

    // </editor-fold>
}
