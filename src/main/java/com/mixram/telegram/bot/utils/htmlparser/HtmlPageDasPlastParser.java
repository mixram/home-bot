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
 * @author mixram on 2019-05-10.
 * @since 1.4.3.0
 */
@Log4j2
@Service
public class HtmlPageDasPlastParser extends HtmlPageShopParser {

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
    public HtmlPageDasPlastParser(
            @Value("${parser.dasplast.search-names.main-element-class.name}") String mainElementClassName,
            @Value("${parser.dasplast.search-names.old-price-selector.name}") String oldPriceSelectorName,
            @Value("${parser.dasplast.search-names.new-price-selector.name}") String newPriceSelectorName,
            @Value("${parser.dasplast.search-names.product-name-selector.name}") String productNameSelectorName,
            @Value("${parser.dasplast.search-names.product-name-href-attr.name}") String productNameHrefAttrName,
            @Value("${parser.dasplast.search-names.product-presence-class.name}") String productAvailableClassName,
            @Value("${parser.dasplast.search-names.product-presence-pattern.name}") String productAvailableTextName) {
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
     * @since 1.4.3.0
     */
    @Override
    protected PlasticType parseType(Element plastic,
                                    PlasticType type) {
        return type;
    }

    /**
     * @since 1.4.3.0
     */
    @Override
    protected String parseTitle(Element plastic,
                                String title) {
        return title;
    }

    /**
     * @since 1.4.3.0
     */
    @Override
    protected BigDecimal parseOldPrice(Element plastic) {
        return super.parsePrice(oldPriceSelectorName, plastic);
    }

    /**
     * @since 1.4.3.0
     */
    @Override
    protected BigDecimal parseSalePrice(Element plastic) {
        return super.parsePrice(newPriceSelectorName, plastic);
    }

    /**
     * @since 1.4.3.0
     */
    @Override
    protected String parseProductName(Element plastic) {
        return super.doParseProductName(plastic, productNameSelectorName);
    }

    /**
     * @since 1.4.3.0
     */
    @Override
    protected String parseProductUrl(Element plastic) {
        return super.parseProductWithSelectorAndAttr(plastic, productNameSelectorName, productNameHrefAttrName);
    }

    /**
     * @since 1.4.3.0
     */
    @Override
    protected BigDecimal parseProductDiscountPercent(Element plastic) {
        return super.doParseProductDiscountPercent(plastic);
    }

    /**
     * @since 1.4.3.0
     */
    @Override
    protected boolean parseInStock(Element plastic) {
        return super.doParseInStock(plastic, productAvailableClassName, productAvailableTextName);
    }

    // </editor-fold>
}
