package com.mixram.telegram.bot.utils.htmlparser;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author mixram on 2019-04-09.
 * @since 0.2.0.0
 */
@Log4j2
@Service
public class HtmlPage3DPlastParserV2 implements HtmlPageParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    //

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    //

    // </editor-fold>

    @Override
    public ParseData parse(String url) {
        log.debug("URL to parse: '{}'", () -> url);

        try {
            Document doc = Jsoup.connect(url).get();

            ParseData data = new ParseData();
            data.setPageTitle(doc.title());
            data.setProductUrl(url);

            Elements oldPriceElements = doc.getElementsByClass("b-product-cost__old-price");
            if (!oldPriceElements.isEmpty()) {
                data.setProductOldPrice(parsePrice(oldPriceElements));
            }

            Elements salePriceElements = doc.getElementsByClass("b-product-cost__price");
            if (!salePriceElements.isEmpty()) {
                data.setProductSalePrice(parsePrice(salePriceElements));
            }

            Elements nameElements = doc.select("[data-qaid=\"product_name\"]");
            if (!nameElements.isEmpty()) {
                data.setProductName(nameElements.first().text());
            }

            return data;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Exception in process of page parsing!", e);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 0.1.0.0
     */
    private BigDecimal parsePrice(Elements elements) {
        String oldPriceText = elements.first().text();
        String[] split = oldPriceText.split(" ");
        String sumString = split[0].replace(",", ".");

        return new BigDecimal(sumString);
    }

    // </editor-fold>
}
