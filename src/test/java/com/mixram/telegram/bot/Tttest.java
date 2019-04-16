package com.mixram.telegram.bot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mixram on 2019-04-13.
 * @since ...
 */
public class Tttest {

    public static void main(String[] args) {
        String test1 = "sale";
        String test2 = "long";
        String patternString = "sale|long";

        Pattern pattern = Pattern.compile(patternString);

        Matcher matcher1 = pattern.matcher(test1);
        Matcher matcher2 = pattern.matcher(test2);

        System.out.println("1: " + matcher1.matches());
        System.out.println("2: " + matcher2.matches());
    }
}
