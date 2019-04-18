package com.mixram.telegram.bot;

import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import com.mixram.telegram.bot.utils.htmlparser.ParseData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mixram on 2019-04-13.
 * @since ...
 */
public class Tttest {

    public static void main(String[] args) throws IOException {
        final String url = "https://monofilament.com.ua/products/standartnye-materialy/abs-eco/abs-eco-chernyj";

        Document doc = Jsoup.connect(url).get();

        ParseData data = new ParseData();
        data.setType(PlasticType.ABS);
        data.setPageTitle(doc.title());
        data.setProductUrl(url);

        Elements oldPriceElements = doc.select("[property=\"product:price:amount\"]");
        if (!oldPriceElements.isEmpty()) {
            data.setProductOldPrice(parsePrice(oldPriceElements));
        }

        Elements salePriceElements = doc.select("[property=\"product:sale_price:amount\"]");
        if (!salePriceElements.isEmpty()) {
            data.setProductSalePrice(parsePrice(salePriceElements));
        }

        data.setProductName(getProductName(doc));

        Elements presenceElements = doc.select("[property=\"product:availability\"]");
        if (!presenceElements.isEmpty()) {
            data.setInStock(checkPresence(presenceElements));
        }

        System.out.println("PARSED: " + JsonUtil.toPrettyJson(data));
    }

    private static BigDecimal parsePrice(Elements elements) {
        String priceText = elements.first().attr("content");
        String[] split = priceText.split(" ");
        String sumString = split[0].replace(",", ".");

        return new BigDecimal(sumString);
    }

    private static String getProductName(Document doc) {
        Elements nameElements = doc.select("[property=\"og:title\"]");

        return nameElements.isEmpty() ? null : nameElements.first().attr("content");
    }

    private static boolean checkPresence(Elements presenceElements) {
        Pattern pattern = Pattern.compile("pending|instock".toUpperCase());
        Matcher matcher = pattern.matcher(presenceElements.first().attr("content").trim().toUpperCase());

        return matcher.matches();
    }
}
