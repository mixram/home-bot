package com.mixram.telegram.bot.utils.htmlparser;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * @author mixram on 2019-04-09.
 * @since 0.1.1.0
 */
@Log4j2
//@Service
public class HtmlPage3DPlastParser implements HtmlPageParser {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final WebDriver driver;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public HtmlPage3DPlastParser() throws IllegalAccessException, InstantiationException {
        Class<ChromeDriver> driverClass = ChromeDriver.class;
        WebDriverManager
                .getInstance(driverClass)
                .setup();
        this.driver = driverClass.newInstance();
    }

    // </editor-fold>


    @Override
    public ParseData parse(String url) {
        log.debug("URL to parse: '{}'", () -> url);

        driver.get(url);

        ParseData data = new ParseData();
        data.setPageTitle(driver.getTitle());
        data.setProductUrl(url);

        try {
            WebElement oldPrice = driver.findElement(By.className("b-product-cost__old-price"));
            String oldPriceText = oldPrice.getText();
            String[] split = oldPriceText.split(" ");

            data.setProductOldPrice(new BigDecimal(split[0]));
        } catch (Exception e) {
            data.setProductOldPrice(null);
        }

        WebElement currentPrice = driver.findElement(By.className("b-product-cost__price"));
        String currentPriceText = currentPrice.getText();
        String[] split = currentPriceText.split(" ");

        data.setProductSalePrice(new BigDecimal(split[0]));

        WebElement productName = driver.findElement(By.cssSelector("[data-qaid=\"product_name\"]"));
        data.setProductName(productName.getText());

        return data;
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
