package com.mixram.telegram.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * Main class for bot.
 *
 * @author mixram on 2018-04-20.
 * @since 0.1.0.0
 */
@Slf4j
@SpringBootApplication
public class UniversalBotApplication {

    public static void main(String[] args) throws Exception {
        pushVersion2Env();

        SpringApplication app = new SpringApplication(UniversalBotApplication.class);
        app.setWebApplicationType(WebApplicationType.SERVLET);
        app.run(args);

        log.info("PRODUCT_VERSION ==> '{}'", System.getProperty("product.version"));
        log.info("PRODUCT_VERSION_FULL ==> '{}'", System.getProperty("product.version.full"));
        log.info("CURRENT_DATE_TIME ==> '{}'", LocalDateTime.now());
    }

    private static void pushVersion2Env() throws IOException {
        Properties p = new Properties();
        p.load(UniversalBotApplication.class.getResourceAsStream("/META-INF/build-info.properties"));

        String[] productVersion = p.getProperty("build.version").split("[.]");
        if (productVersion.length < 4) {
            throw new IllegalStateException(String.format("Wrong build.version=%s!", p.getProperty("build.version")));
        }

        System.setProperty("product.version", productVersion[0] + "." + productVersion[1]);
        System.setProperty("product.version.full",
                           productVersion[0] + "." + productVersion[1] + "." + productVersion[2] + "." + productVersion[3]);
    }
}
