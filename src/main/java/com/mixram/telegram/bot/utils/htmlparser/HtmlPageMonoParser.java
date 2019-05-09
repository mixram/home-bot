package com.mixram.telegram.bot.utils.htmlparser;

import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseData;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseDataSettings;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mixram on 2019-05-02.
 * @since 1.4.2.0
 */
@Log4j2
@Service
public class HtmlPageMonoParser extends HtmlPageShopParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final String productNameClassName;
    private final String productNameConcreteHolderClassName;
    private final String productNameHrefAttrName;
    private final String productDiscountName;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public HtmlPageMonoParser(
            @Value("${parser.mono.search-names.main-element-class.name}") String mainElementClassName,
            @Value("${parser.mono.search-names.product-name-class.name}") String productNameClassName,
            @Value("${parser.mono.search-names.product-name-concrete-holder-class.name}") String productNameConcreteHolderClassName,
            @Value("${parser.mono.search-names.product-name-href-attr.name}") String productNameHrefAttrName,
            @Value("${parser.mono.search-names.product-discount.name}") String productDiscountName) {
        super(mainElementClassName);
        this.productNameClassName = productNameClassName;
        this.productNameConcreteHolderClassName = productNameConcreteHolderClassName;
        this.productNameHrefAttrName = productNameHrefAttrName;
        this.productDiscountName = productDiscountName;
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
        return null;
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected BigDecimal parseSalePrice(Element plastic) {
        return null;
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected String parseProductName(Element plastic) {
        String name = null;

        Elements productThumb = plastic.getElementsByClass(productNameClassName);
        if (!productThumb.isEmpty()) {
            Elements caption = productThumb.first().getElementsByClass(productNameConcreteHolderClassName);
            if (!caption.isEmpty()) {
                Elements href = caption.first().getElementsByAttribute(productNameHrefAttrName);
                if (!href.isEmpty()) {
                    name = href.first().text();
                }
            }
        }

        return name;
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected String parseProductUrl(Element plastic) {
        String url = null;

        Elements productThumb = plastic.getElementsByClass(productNameClassName);
        if (!productThumb.isEmpty()) {
            Elements caption = productThumb.first().getElementsByClass(productNameConcreteHolderClassName);
            if (!caption.isEmpty()) {
                Elements href = caption.first().getElementsByAttribute(productNameHrefAttrName);
                if (!href.isEmpty()) {
                    Attributes attributes = href.first().attributes();
                    url = attributes.get(productNameHrefAttrName);
                }
            }
        }

        return url;
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected BigDecimal parseProductDiscountPercent(Element plastic) {
        BigDecimal percent = null;

        Elements saleElement = plastic.getElementsByClass(productDiscountName);
        if (!saleElement.isEmpty()) {
            String text = saleElement.first().text();

            percent = StringUtils.isBlank(text) ? null : new BigDecimal(text.substring(1, text.indexOf("%")));
        }

        return percent;
    }

    /**
     * @since 1.4.2.0
     */
    @Override
    protected boolean parseInStock(Element plastic) {
        return true;
    }

    // </editor-fold>
}
