package com.mixram.telegram.bot;

import com.mixram.telegram.bot.utils.CustomMessageSource;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

/**
 * @author mixram on 2019-04-26.
 * @since 1.4.1.0
 */
@Ignore
@TestPropertySource("file:/Users/mixram/Documents/Dev/projects/universal-bot/configs/bootstrap.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocaleTest {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private CustomMessageSource messageSource;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public void setMessageSource(CustomMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // </editor-fold>


    @Test
    public void testLocale() {
        String code = "telegram.bot.message.no-data-for-shop";
        String message1 = messageSource.getMessage(code, Locale.ENGLISH);
        String message2 = messageSource.getMessage(code, Locale.US);
        String message3 = messageSource.getMessage(code, new Locale("en"));

        //        System.out.println("1: " + message1);
        //        System.out.println("2: " + message2);
        //        System.out.println("3: " + message3);

        System.out.println("OK --- " + new Object() {}.getClass().getEnclosingMethod().getName());
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
