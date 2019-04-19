package com.mixram.telegram.bot.utils.htmlparser;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author mixram on 2019-04-15.
 * @since ...
 */
@Log4j2
abstract class HtmlPageShopParser implements HtmlPageParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final String oldPriceSelectorName;
    private final String newPriceSelectorName;
    private final String productAvailableSelectorName;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    public HtmlPageShopParser(@NotNull String oldPriceSelectorName,
                              @NotNull String newPriceSelectorName,
                              @NotNull String productAvailableSelectorName) {
        this.oldPriceSelectorName = oldPriceSelectorName;
        this.newPriceSelectorName = newPriceSelectorName;
        this.productAvailableSelectorName = productAvailableSelectorName;

        Validate.notBlank(this.oldPriceSelectorName, "oldPriceClassName is not specified!");
        Validate.notBlank(this.newPriceSelectorName, "oldPriceClassName is not specified!");
        Validate.notBlank(this.productAvailableSelectorName, "oldPriceClassName is not specified!");
    }

    // </editor-fold>


    @Override
    public ParseData parse(ParseData parseData) {
        log.debug("URL to parse: '{}'", () -> parseData);

        try {
            Document doc = Jsoup.connect(parseData.getProductUrl()).get();

            ParseData data = new ParseData();
            data.setType(parseData.getType());
            data.setPageTitle(doc.title());
            data.setProductUrl(parseData.getProductUrl());

            Elements oldPriceElements = doc.select(oldPriceSelectorName);
            if (!oldPriceElements.isEmpty()) {
                data.setProductOldPrice(parsePrice(oldPriceElements));
            }

            Elements salePriceElements = doc.select(newPriceSelectorName);
            if (!salePriceElements.isEmpty()) {
                data.setProductSalePrice(parsePrice(salePriceElements));
            }

            data.setProductName(getProductName(doc));

            Elements presenceElements = doc.select(productAvailableSelectorName);
            if (!presenceElements.isEmpty()) {
                data.setInStock(checkPresence(presenceElements));
            }

            log.debug("PARSED: {}", data);

            return data;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Exception in process of page parsing!", e);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.0.0.0
     */
    protected abstract String getProductName(Document doc);

    /**
     * @since 1.0.0.0
     */
    protected abstract BigDecimal parsePrice(Elements elements);

    /**
     * @since 1.1.0.0
     */
    protected abstract boolean checkPresence(Elements elements);

    // </editor-fold>

}
