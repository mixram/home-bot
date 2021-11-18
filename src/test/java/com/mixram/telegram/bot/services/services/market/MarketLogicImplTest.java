package com.mixram.telegram.bot.services.services.market;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static com.mixram.telegram.bot.services.services.market.MarketLogicImpl.MARKET_PATTERN;

/**
 * @author mixram on 2021-02-21.
 * @since 1.8.8.2
 */
//@Ignore
@TestPropertySource("file:/Users/mixram/Documents/Dev/projects/3d-bot-microservices/universal-bot/dev/configs/bootstrap.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class MarketLogicImplTest extends TestCase {

    @Test
    public void testAdvMatching() {
        final String test1 = "#продам тест";
        final String test2 = " #продам тест";
        final String test3 = "dsds #продам тест";
        final String test4 = "dsds  тест #продам";
        final String test5 = "dsds  тест#продам";
        final String test6 = "dsds  тест#Продам";
        final String test7 = "#Продам dsds  тест";
        final String test8 = "#придам dsds  тест";
        final String test9 = "#вiддам dsds  тест";
        final String test10 = " dsds  #вiддам тест";
        final String test11 = "#вiдам dsds  тест";

        assertTrue(MARKET_PATTERN.matcher(test1).matches());
        assertTrue(MARKET_PATTERN.matcher(test2).matches());
        assertTrue(MARKET_PATTERN.matcher(test3).matches());
        assertTrue(MARKET_PATTERN.matcher(test4).matches());
        assertTrue(MARKET_PATTERN.matcher(test5).matches());
        assertTrue(MARKET_PATTERN.matcher(test6).matches());
        assertTrue(MARKET_PATTERN.matcher(test7).matches());
        assertFalse(MARKET_PATTERN.matcher(test8).matches());
        assertTrue(MARKET_PATTERN.matcher(test9).matches());
        assertTrue(MARKET_PATTERN.matcher(test10).matches());
        assertTrue(MARKET_PATTERN.matcher(test11).matches());
    }
}
