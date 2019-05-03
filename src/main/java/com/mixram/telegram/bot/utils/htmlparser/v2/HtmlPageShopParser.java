package com.mixram.telegram.bot.utils.htmlparser.v2;

import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.utils.htmlparser.v2.entity.ParseData;
import com.mixram.telegram.bot.utils.htmlparser.v2.entity.ParseDataSettings;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mixram on 2019-05-02.
 * @since 1.4.2.0
 */
@Log4j2
abstract class HtmlPageShopParser implements HtmlPageParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final String mainElementClassName;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    public HtmlPageShopParser(String mainElementClassName) {
        this.mainElementClassName = mainElementClassName;
    }

    // </editor-fold>


    @Override
    public List<ParseData> parse(ParseDataSettings parseData) {
        log.debug("URL to parse: '{}'", () -> parseData);

        try {
            Document doc = Jsoup.connect(parseData.getCommonUrl())
                                .header("accept",
                                        "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                                .header("accept-encoding", "gzip, deflate, br")
                                .header("accept-language", "en-US,en;q=0.9")
                                .header("cache-control", "max-age=0")
                                .header("user-agent",
                                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36")
                                .get();

            List<ParseData> pData = new ArrayList<>();

            Elements plastics = doc.getElementsByClass(mainElementClassName);
            if (!plastics.isEmpty()) {
                for (Element plastic : plastics) {
                    try {
                        ParseData data = new ParseData();
                        data.setType(parseType(plastic, parseData.getType()));
                        data.setPageTitle(parseTitle(plastic, doc.title()));
                        data.setProductOldPrice(parseOldPrice(plastic));
                        data.setProductSalePrice(parseSalePrice(plastic));
                        data.setProductName(parseProductName(plastic));
                        data.setProductUrl(parseProductUrl(plastic));
                        data.setCommonUrl(parseData.getCommonUrl());
                        data.setProductDiscountPercent(parseProductDiscountPercent(plastic));
                        data.setInStock(parseInStock(plastic));

                        pData.add(data);
                    } catch (Exception e) {
                        log.warn("Exception in process of concrete page parsing!", e);
                    }
                }
            }

            log.debug("PARSED: {}", pData);

            return pData;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Exception in process of common page parsing!", e);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    protected abstract PlasticType parseType(Element plastic,
                                             PlasticType type);

    protected abstract String parseTitle(Element plastic,
                                         String title);

    protected abstract BigDecimal parseOldPrice(Element plastic);

    protected abstract BigDecimal parseSalePrice(Element plastic);

    protected abstract String parseProductName(Element plastic);

    protected abstract String parseProductUrl(Element plastic);

    protected abstract BigDecimal parseProductDiscountPercent(Element plastic);

    protected abstract boolean parseInStock(Element plastic);

    protected BigDecimal byModule(BigDecimal dec) {
        return dec.signum() < 0 ? dec.negate() : dec;
    }

    // </editor-fold>
}
