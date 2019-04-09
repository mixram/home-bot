package com.mixram.telegram.bot.utils.htmlparser;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author mixram on 2019-04-09.
 * @since 0.2.0.0
 */
@Log4j2
public class HtmlPage3DPlastParser implements HtmlPageParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final WebDriver driver;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    /**
     * To create an instance of {@link JyskHtmlPageParser} with driver manager of specified type.
     *
     * @param driverClass the class of driver to use as parser.
     *
     * @since 0.1.1.0
     */
    public HtmlPage3DPlastParser(Class<? extends WebDriver> driverClass)
            throws IllegalAccessException, InstantiationException {
        WebDriverManager
                .getInstance(driverClass)
                .setup();
        this.driver = driverClass.newInstance();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

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
}
