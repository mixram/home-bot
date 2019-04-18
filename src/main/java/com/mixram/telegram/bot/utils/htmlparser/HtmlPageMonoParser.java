package com.mixram.telegram.bot.utils.htmlparser;

import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mixram on 2019-04-09.
 * @since 1.1.0.0
 */
@Log4j2
@Service
public class HtmlPageMonoParser extends HtmlPageShopParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final String attributeName;
    private final String productNameSelectorName;
    private final String productAvailableTextName;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public HtmlPageMonoParser(
            @Value("${parser.mono.search-names.old-price-selector-name.name}") String oldPriceClassName,
            @Value("${parser.mono.search-names.new-price-selector-name.name}") String newPriceClassName,
            @Value("${parser.mono.search-names.product-name-selector-name.name}") String productNameSelectorName,
            @Value("${parser.mono.search-names.attr-name.name}") String attributeName,
            @Value("${parser.mono.search-names.product-presence-selector-name.name}") String productAvailableSelectorName,
            @Value("${parser.mono.search-names.product-presence-pattern-name.name}") String productAvailableTextName) {
        super(oldPriceClassName, newPriceClassName, productAvailableSelectorName);
        this.productNameSelectorName = productNameSelectorName;
        this.attributeName = attributeName;
        this.productAvailableTextName = productAvailableTextName;
    }

    // </editor-fold>


    /**
     * @since 1.1.0.0
     */
    @Override
    public ParseData parse(ParseData parseData) {
        return super.parse(parseData);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.1.0.0
     */
    @Override
    protected BigDecimal parsePrice(Elements elements) {
        String priceText = elements.first().attr(attributeName);
        String sumString = priceText.replace(",", ".");

        return new BigDecimal(sumString);
    }

    /**
     * @since 1.1.0.0
     */
    @Override
    protected String getProductName(Document doc) {
        Elements nameElements = doc.select(productNameSelectorName);

        return nameElements.isEmpty() ? null : nameElements.first().attr(attributeName);
    }

    /**
     * @since 1.1.0.0
     */
    @Override
    protected boolean checkPresence(Elements elements) {
        Pattern pattern = Pattern.compile(productAvailableTextName.toUpperCase());
        Matcher matcher = pattern.matcher(elements.first().attr(attributeName).trim().toUpperCase());

        return matcher.matches();
    }

    // </editor-fold>
}
