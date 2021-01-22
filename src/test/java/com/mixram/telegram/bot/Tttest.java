package com.mixram.telegram.bot;

/**
 * @author mixram on 2019-04-13.
 * @since ...
 */
public class Tttest {

    public static void main(String[] args) {
        final String test = "/cas_msg_1234567890";

        System.out.println("T: " + test.substring(test.lastIndexOf("_") + 1));
    }
}
