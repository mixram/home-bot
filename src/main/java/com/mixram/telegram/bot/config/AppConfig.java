package com.mixram.telegram.bot.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Configuration for all app.
 *
 * @author mixram on 2018-07-31.
 * @since 0.1.0.0
 */
@Configuration
public class AppConfig {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    //

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    //

    // </editor-fold


    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("utf-8");
        messageSource.setBasename("i18n/messages");

        return messageSource;
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
