package com.mixram.telegram.bot;

import java.util.regex.Pattern;

/**
 * @author mixram on 2019-04-13.
 * @since 0.1.0.0
 */
public class Tttest {

    public static void main(String[] args) {
        final String test1 = "#продам тест";
        final String test2 = "#продам\n\nтест";
        final String test3 = "dddd\n#продам\n\nтест";

        System.out.println("1: " + MARKET_PATTERN.matcher(test1).matches());
        System.out.println("2: " + MARKET_PATTERN.matcher(test2).matches());
        System.out.println("3: " + MARKET_PATTERN.matcher(test3).matches());
    }

    private static final String MARKET_PATTERN_STRING = ".*#(продам|куплю|бронь).*";
    private static final Pattern MARKET_PATTERN = Pattern.compile(MARKET_PATTERN_STRING, Pattern.DOTALL);
}
