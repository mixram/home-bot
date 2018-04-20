package com.mixram.telegram.bot.config;

import com.mixram.telegram.bot.utils.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration file.
 *
 * @author mixram on 2018-04-20.
 * @since 0.1.0.0
 */
@Configuration
public class AppConfiguration {

    @Bean
    public String botToken() {
        return appProperties.getProperty(TOKEN_PROP_NAME);
    }

    /*===Private elements===*/


    /*===Util elements===*/

    private static final String TOKEN_PROP_NAME = "token";

    private AppProperties appProperties;

    @Autowired
    public void setAppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

}
