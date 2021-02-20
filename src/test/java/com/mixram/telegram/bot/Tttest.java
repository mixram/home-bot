package com.mixram.telegram.bot;

import java.util.regex.Pattern;

/**
 * @author mixram on 2019-04-13.
 * @since 0.1.0.0
 */
public class Tttest {

    public static void main(String[] args) {
        final String test1 = "#продам тест";
        final String test2 = " #продам тест";
        final String test3 = "dsds #продам тест";
        final String test4 = "dsds  тест #продам";
        final String test5 = "dsds  тест#продам";
        final String test6 = "dsds  тест#Продам";
        final String test7 = "#Продам dsds  тест";
        final String test8 = "#придам dsds  тест";

        System.out.println("1: " + MARKET_PATTERN.matcher(test1).matches());
        System.out.println("2: " + MARKET_PATTERN.matcher(test2).matches());
        System.out.println("3: " + MARKET_PATTERN.matcher(test3).matches());
        System.out.println("4: " + MARKET_PATTERN.matcher(test4).matches());
        System.out.println("5: " + MARKET_PATTERN.matcher(test5).matches());
        System.out.println("6: " + MARKET_PATTERN.matcher(test6).matches());
        System.out.println("7: " + MARKET_PATTERN.matcher(test7).matches());
        System.out.println("8: " + MARKET_PATTERN.matcher(test8).matches());
    }

    //    private static final String MARKET_PATTERN_STRING = ".*#[а-яА-я].*";
    private static final String MARKET_PATTERN_STRING = ".*#([пП][рР][оО][дД][аА][мМ]|[кК][уУ][пП][лЛ][юЮ]|[бБ][рР][оО][нН][ьЬ]).*";
    private static final Pattern MARKET_PATTERN = Pattern.compile(MARKET_PATTERN_STRING, Pattern.DOTALL);

}
