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
 * @since 0.2.0.0
 */
@Log4j2
@Service
public class HtmlPage3DUAParser extends HtmlPageShopParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final String productNameSelectorName2;
    private final String productNameSelectorName1;
    private final String productAvailableTextName;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public HtmlPage3DUAParser(
            @Value("${parser.3dua.search-names.old-price-selector-name.name}") String oldPriceClassName,
            @Value("${parser.3dua.search-names.new-price-selector-name.name}") String newPriceClassName,
            @Value("${parser.3dua.search-names.product-name-selector-name.name}") String productNameSelectorName1,
            @Value("${parser.3dua.search-names.product-name-selector-name.name}") String productNameSelectorName2,
            @Value("${parser.3dua.search-names.product-presence-selector-name.name}") String productAvailableSelectorName,
            @Value("${parser.3dua.search-names.product-presence-pattern-name.name}") String productAvailableTextName) {
        super(oldPriceClassName, newPriceClassName, productAvailableSelectorName);
        this.productNameSelectorName1 = productNameSelectorName1;
        this.productNameSelectorName2 = productNameSelectorName2;
        this.productAvailableTextName = productAvailableTextName;
    }

    // </editor-fold>


    /**
     * @since 0.2.0.0
     */
    @Override
    public ParseData parse(ParseData parseData) {
        return super.parse(parseData);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 0.2.0.0
     */
    @Override
    protected BigDecimal parsePrice(Elements elements) {
        String priceText = elements.first().text();
        String sumString = priceText.replace(",", ".");

        return new BigDecimal(sumString);
    }

    @Override
    protected String getProductName(Document doc) {
        Elements nameElements1 = doc.select(productNameSelectorName1);
        Elements nameElements2 = nameElements1.select(productNameSelectorName2);

        return nameElements2.isEmpty() ? null : nameElements2.first().text();
    }

    /**
     * @since 1.1.0.0
     */
    @Override
    protected boolean checkPresence(Elements presenceElements) {
        Pattern pattern = Pattern.compile(productAvailableTextName.toUpperCase());
        Matcher matcher = pattern.matcher(presenceElements.first().text().trim().toUpperCase());

        return matcher.matches();
    }

    // </editor-fold>
}
