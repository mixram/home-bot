package com.mixram.telegram.bot.utils.htmlparser;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mixram on 2019-04-15.
 * @since ...
 */
@Log4j2
abstract class HtmlPageShopParser implements HtmlPageParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final String oldPriceClassName;
    private final String newPriceClassName;
    private final String productAvailableSelectorName;
    private final String productAvailableTextName;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    public HtmlPageShopParser(@NotNull String oldPriceClassName,
                              @NotNull String newPriceClassName,
                              @NotNull String productAvailableSelectorName,
                              @NotNull String productAvailableTextName) {
        this.oldPriceClassName = oldPriceClassName;
        this.newPriceClassName = newPriceClassName;
        this.productAvailableSelectorName = productAvailableSelectorName;
        this.productAvailableTextName = productAvailableTextName;

        Validate.notBlank(this.oldPriceClassName, "oldPriceClassName is not specified!");
        Validate.notBlank(this.newPriceClassName, "oldPriceClassName is not specified!");
        Validate.notBlank(this.productAvailableSelectorName, "oldPriceClassName is not specified!");
        Validate.notBlank(this.productAvailableTextName, "oldPriceClassName is not specified!");
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

            Elements oldPriceElements = doc.getElementsByClass(oldPriceClassName);
            if (!oldPriceElements.isEmpty()) {
                data.setProductOldPrice(parsePrice(oldPriceElements));
            }

            Elements salePriceElements = doc.getElementsByClass(newPriceClassName);
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

    private boolean checkPresence(Elements presenceElements) {
        Pattern pattern = Pattern.compile(productAvailableTextName.toUpperCase());
        Matcher matcher = pattern.matcher(presenceElements.first().text().trim().toUpperCase());

        return matcher.matches();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 0.2.0.0
     */
    protected abstract String getProductName(Document doc);

    protected abstract BigDecimal parsePrice(Elements elements);

    // </editor-fold>

}
