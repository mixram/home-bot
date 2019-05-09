package com.mixram.telegram.bot.utils.htmlparser;

import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseData;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseDataSettings;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mixram on 2019-05-03.
 * @since 1.4.2.0
 */
@Log4j2
@Service
public class HtmlPage3DUAParser extends HtmlPageShopParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final String oldPriceSelectorName;
    private final String newPriceSelectorName;
    private final String productNameSelectorName;
    private final String productNameTitleAttrName;
    private final String productNameHrefAttrName;
    private final String productAvailableClassName;
    private final String productAvailableTextName;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public HtmlPage3DUAParser(
            @Value("${parser.3dua.search-names.main-element-class.name}") String mainElementClassName,
            @Value("${parser.3dua.search-names.old-price-selector.name}") String oldPriceSelectorName,
            @Value("${parser.3dua.search-names.new-price-selector.name}") String newPriceSelectorName,
            @Value("${parser.3dua.search-names.product-name-selector.name}") String productNameSelectorName,
            @Value("${parser.3dua.search-names.product-name-title-attr.name}") String productNameTitleAttrName,
            @Value("${parser.3dua.search-names.product-name-href-attr.name}") String productNameHrefAttrName,
            @Value("${parser.3dua.search-names.product-presence-class.name}") String productAvailableClassName,
            @Value("${parser.3dua.search-names.product-presence-pattern.name}") String productAvailableTextName) {
        super(mainElementClassName);
        this.oldPriceSelectorName = oldPriceSelectorName;
        this.newPriceSelectorName = newPriceSelectorName;
        this.productNameSelectorName = productNameSelectorName;
        this.productNameTitleAttrName = productNameTitleAttrName;
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
        return super.parsePrice(oldPriceSelectorName, plastic);
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected BigDecimal parseSalePrice(Element plastic) {
        return super.parsePrice(newPriceSelectorName, plastic);
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected String parseProductName(Element plastic) {
        return parseProductData(productNameTitleAttrName, plastic);
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected String parseProductUrl(Element plastic) {
        return parseProductData(productNameHrefAttrName, plastic);
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected BigDecimal parseProductDiscountPercent(Element plastic) {
        return super.doParseProductDiscountPercent(plastic);
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected boolean parseInStock(Element plastic) {
        return super.doParseInStock(plastic, productAvailableClassName, productAvailableTextName);
    }

    /**
     * @since 1.4.2.0
     */
    private String parseProductData(String selectorName,
                                    Element plastic) {
        return super.parseProductWithSelectorAndAttr(plastic, productNameSelectorName, selectorName);
    }

    // </editor-fold>
}
