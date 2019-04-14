package com.mixram.telegram.bot;

import com.mixram.telegram.bot.services.domain.enums.Command;

/**
 * @author mixram on 2019-04-13.
 * @since ...
 */
public class Tttest {

    public static void main(String[] args) {
        final String text = "/sales_all";

        String[] fs = text.split(" ");
        String fsc = fs[0];
        String[] ss = fsc.split("_");
        String ssc = ss[1];

        Command command = Command.getByName(ssc.toUpperCase());

        System.out.println("C: " + command);
    }
}
