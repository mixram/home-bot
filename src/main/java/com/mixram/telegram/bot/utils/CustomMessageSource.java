package com.mixram.telegram.bot.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * @author mixram on 2019-04-25.
 * @since 1.4.1.0
 */
@Component
public class CustomMessageSource {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final MessageSource messageSource;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public CustomMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    public void init() {
        //
    }

    // </editor-fold>


    /**
     * To get internationalized message.
     *
     * @param code   code of message.
     * @param locale locale.
     *
     * @return internationalized message according to current locale.
     *
     * @since 1.4.1.0
     */
    public String getMessage(String code,
                             Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    /**
     * To get internationalized message.
     *
     * @param code   code of message.
     * @param locale locale.
     * @param args   arguments to inject into string.
     *
     * @return internationalized message according to current locale.
     *
     * @since 1.4.1.0
     */
    public String getMessage(String code,
                             Locale locale,
                             Object... args) {
        return messageSource.getMessage(code, args, locale);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
