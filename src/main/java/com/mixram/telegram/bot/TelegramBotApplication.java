package com.mixram.telegram.bot;

import java.io.IOException;
import java.util.Properties;

/**
 * Main class for bot.
 *
 * @author mixram on 2018-04-20.
 * @since 0.1.0.0
 */
public class TelegramBotApplication {

    public static void main(String[] args) throws Exception {
        pushVersion2Env();

        //
    }

    private static void pushVersion2Env() throws IOException {
        Properties p = new Properties();
        p.load(Thread.class.getResourceAsStream("/META-INF/build-info.properties"));

        String[] productVersion = p.getProperty("build.version").split("[.]");
        if (productVersion.length < 2) {
            throw new IllegalStateException(String.format("Wrong build.version=%s!", p.getProperty("build.version")));
        }

        System.setProperty("product.version", productVersion[0] + "." + productVersion[1]);
    }
}
