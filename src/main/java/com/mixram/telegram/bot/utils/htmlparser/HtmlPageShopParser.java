package com.mixram.telegram.bot.utils.htmlparser;

import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseData;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseDataSettings;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mixram on 2019-05-02.
 * @since 1.4.2.0
 */
@Log4j2
abstract class HtmlPageShopParser implements HtmlPageParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final Pattern patternNumeric = Pattern.compile("\\d+");

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
                                //                                .header("accept-encoding", "gzip, deflate, br") //causes parse error in Monofilament
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

    /**
     * @since 1.4.3.0
     */
    protected BigDecimal parsePrice(String selectorName,
                                    Element plastic) {
        BigDecimal price = null;

        Element element = plastic.selectFirst(selectorName);
        if (element != null) {
            Matcher matcher = patternNumeric.matcher(element.text());
            String matched = matcher.find() ? matcher.group(0) : null;

            price = matched == null ? null : new BigDecimal(matched);
        }

        return price;
    }

    /**
     * @since 1.4.3.0
     */
    protected String doParseProductName(Element plastic,
                                        String productNameSelectorName) {
        String name = null;

        Element element = plastic.selectFirst(productNameSelectorName);
        if (element != null) {
            name = element.text();
        }

        return name;
    }

    /**
     * @since 1.4.3.0
     */
    protected String parseProductWithSelectorAndAttr(Element plastic,
                                                     String productNameSelectorName,
                                                     String productNameHrefAttrName) {
        String data = null;

        Element element = plastic.selectFirst(productNameSelectorName);
        if (element != null) {
            Element dataAttr = element.getElementsByAttribute(productNameHrefAttrName).first();
            if (dataAttr != null) {
                data = dataAttr.attr(productNameHrefAttrName);
            }
        }

        return StringUtils.isBlank(data) ? null : data;
    }

    /**
     * @since 1.4.3.0
     */
    protected BigDecimal doParseProductDiscountPercent(Element plastic) {
        BigDecimal percent = null;

        BigDecimal oldPrice = parseOldPrice(plastic);
        BigDecimal salePrice = parseSalePrice(plastic);
        if (oldPrice != null && salePrice != null) {
            BigDecimal perc100 = new BigDecimal(100);
            percent = byModule(salePrice
                                       .multiply(perc100)
                                       .divide(oldPrice, 1, RoundingMode.HALF_UP)
                                       .subtract(perc100));
        }

        return percent;
    }

    /**
     * @since 1.4.3.0
     */
    protected boolean doParseInStock(Element plastic,
                                     String productAvailableClassName,
                                     String productAvailableTextName) {
        Elements elements = plastic.getElementsByClass(productAvailableClassName);
        if (!elements.isEmpty()) {
            Pattern pattern = Pattern.compile(productAvailableTextName.toUpperCase());
            Matcher matcher = pattern.matcher(elements.first().text().trim().toUpperCase());

            return matcher.matches();
        }

        return false;
    }

    /**
     * @since 1.4.3.0
     */
    private BigDecimal byModule(BigDecimal dec) {
        return dec.signum() < 0 ? dec.negate() : dec;
    }

    // </editor-fold>
}
