package com.mixram.telegram.bot.utils.htmlparser.v2;

import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.utils.htmlparser.v2.entity.ParseData;
import com.mixram.telegram.bot.utils.htmlparser.v2.entity.ParseDataSettings;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mixram on 2019-05-03.
 * @since 1.4.2.0
 */
@Log4j2
@Service
public class HtmlPageU3DFParser extends HtmlPageShopParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final String oldPriceSelectorName;
    private final String newPriceSelectorName;
    private final String productNameSelectorName;
    private final String productNameHrefAttrName;
    private final String productAvailableClassName;
    private final String productAvailableTextName;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public HtmlPageU3DFParser(
            @Value("${parser.u3df.search-names.main-element-class.name}") String mainElementClassName,
            @Value("${parser.u3df.search-names.old-price-selector.name}") String oldPriceSelectorName,
            @Value("${parser.u3df.search-names.new-price-selector.name}") String newPriceSelectorName,
            @Value("${parser.u3df.search-names.product-name-selector.name}") String productNameSelectorName,
            @Value("${parser.u3df.search-names.product-name-href-attr.name}") String productNameHrefAttrName,
            @Value("${parser.u3df.search-names.product-presence-class.name}") String productAvailableClassName,
            @Value("${parser.u3df.search-names.product-presence-pattern.name}") String productAvailableTextName) {
        super(mainElementClassName);
        this.oldPriceSelectorName = oldPriceSelectorName;
        this.newPriceSelectorName = newPriceSelectorName;
        this.productNameSelectorName = productNameSelectorName;
        this.productNameHrefAttrName = productNameHrefAttrName;
        this.productAvailableClassName = productAvailableClassName;
        this.productAvailableTextName = productAvailableTextName;
    }


    // </editor-fold>

    @Override
    public List<ParseData> parse(ParseDataSettings parseData) {
        return super.parse(parseData);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.4.2.0
     */
    @Override
    protected PlasticType parseType(Element plastic,
                                    PlasticType type) {
        return type;
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected String parseTitle(Element plastic,
                                String title) {
        return title;
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected BigDecimal parseOldPrice(Element plastic) {
        return parsePrice(oldPriceSelectorName, plastic);
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected BigDecimal parseSalePrice(Element plastic) {
        return parsePrice(newPriceSelectorName, plastic);
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected String parseProductName(Element plastic) {
        String name = null;

        Element element = plastic.selectFirst(productNameSelectorName);
        if (element != null) {
            name = element.text();
        }

        return name;
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected String parseProductUrl(Element plastic) {
        String url = null;

        Element element = plastic.selectFirst(productNameSelectorName);
        if (element != null) {
            Element href = element.getElementsByAttribute(productNameHrefAttrName).first();
            if (href != null) {
                url = href.attr(productNameHrefAttrName);
            }
        }

        return StringUtils.isBlank(url) ? null : url;
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected BigDecimal parseProductDiscountPercent(Element plastic) {
        BigDecimal percent = null;

        BigDecimal oldPrice = parseOldPrice(plastic);
        BigDecimal salePrice = parseSalePrice(plastic);
        if (oldPrice != null && salePrice != null) {
            BigDecimal perc100 = new BigDecimal(100);
            percent = super.byModule(salePrice
                                             .multiply(perc100)
                                             .divide(oldPrice, 1, RoundingMode.HALF_UP)
                                             .subtract(perc100));
        }

        return percent;
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected boolean parseInStock(Element plastic) {
        Elements elements = plastic.getElementsByClass(productAvailableClassName);
        if (!elements.isEmpty()) {
            Pattern pattern = Pattern.compile(productAvailableTextName.toUpperCase());
            Matcher matcher = pattern.matcher(elements.first().text().trim().toUpperCase());

            return matcher.matches();
        }

        return false;
    }

    /**
     * @since 1.4.2.0
     */
    private BigDecimal parsePrice(String selectorName,
                                  Element plastic) {
        BigDecimal price = null;

        Element element = plastic.selectFirst(selectorName);
        if (element != null) {
            String[] split = element.text().split(" ");
            String sumString = split[0].replace(",", ".");
            price = new BigDecimal(sumString);
        }

        return price;
    }

    /**
     * @since 1.4.2.0
     */
    private String parseProductData(String selectorName,
                                    Element plastic) {
        String data = null;

        Element element = plastic.selectFirst(productNameSelectorName);
        if (element != null) {
            Element attr = element.getElementsByAttribute(selectorName).first();
            if (attr != null) {
                data = attr.text();
            }
        }

        return data;
    }

    // </editor-fold>
}
