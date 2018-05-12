package com.mixram.telegram.bot.service.parsers;

import com.mixram.telegram.bot.service.parsers.pojo.ParseData;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Parser to parse web pages.
 *
 * @author mixram on 2018-05-12.
 * @since 0.1.1.0
 */
@Slf4j
public class JyskHtmlPageParser implements HtmlPageParser {

    @Override
    public ParseData parse(String url) {
        driver.get(url);

        ParseData data = new ParseData();
        data.setPageTitle(driver.getTitle());

        try {
            WebElement searchInput = driver.findElement(By.className("product-teaser"));
            data.setProductName(searchInput.getAttribute("data-name"));
            data.setProductUrl(searchInput.getAttribute("data-url"));
            data.setProductPrice(searchInput.getAttribute("data-price"));
            data.setProductSalePrice(searchInput.getAttribute("data-minsingleprice"));
        } catch (Exception e) {
            log.warn("", e);
        }

        return data;
    }

    private final WebDriver driver;

    /**
     * To create an instance of {@link JyskHtmlPageParser} with driver manager of specified type.
     *
     * @param driverClass the class of driver to use as parser.
     *
     * @since 0.1.1.0
     */
    public JyskHtmlPageParser(Class<? extends WebDriver> driverClass)
            throws IllegalAccessException, InstantiationException {
        WebDriverManager
                .getInstance(driverClass)
                .setup();
        this.driver = driverClass.newInstance();
    }
}
