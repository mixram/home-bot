package com.mixram.telegram.bot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mixram on 2019-04-13.
 * @since ...
 */
public class Tttest {

    public static void main(String[] args) {
        String test1 = "ere";
        String test2 = "er4e";
        String test3 = "от 450 грн";
        String test4 = "32грн";
        String test5 = "от 50 до 30 грн";

        final Pattern pattern = Pattern.compile("\\d+");

        final Matcher matcher1 = pattern.matcher(test1);
        final Matcher matcher2 = pattern.matcher(test2);
        final Matcher matcher3 = pattern.matcher(test3);
        final Matcher matcher4 = pattern.matcher(test4);
        final Matcher matcher5 = pattern.matcher(test5);

        System.out.println("1: " + (matcher1.find() ? matcher1.group(0) : null));
        System.out.println("2: " + (matcher2.find() ? matcher2.group(0) : null));
        System.out.println("3: " + (matcher3.find() ? matcher3.group(0) : null));
        System.out.println("4: " + (matcher4.find() ? matcher4.group(0) : null));
        System.out.println("5: " + (matcher5.find() ? matcher5.group(0) : null));
    }
}
