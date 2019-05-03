package com.mixram.telegram.bot;

import java.math.BigDecimal;

/**
 * @author mixram on 2019-04-13.
 * @since ...
 */
public class Tttest {

    public static void main(String[] args) {
        BigDecimal test1 = new BigDecimal("-1");
        BigDecimal test2 = new BigDecimal("1");
        BigDecimal test3 = new BigDecimal("0");

        System.out.println("1: " + byModule(test1));
        System.out.println("2: " + byModule(test2));
        System.out.println("3: " + byModule(test3));
    }

    private static BigDecimal byModule(BigDecimal dec) {
        //        BigDecimal result;
        //        if (signum < 0) {
        //            result = dec.negate();
        //        } else {
        //            result = dec;
        //        }

        return dec.signum() < 0 ? dec.negate() : dec;
    }
}
