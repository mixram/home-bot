package com.mixram.telegram.bot;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author mixram on 2019-04-15.
 * @since ...
 */
public class PropertyTextConverter {

    public static void main(String[] args) {
        String src = "В наличии|Заканчивается";
        String unicodeEscaped = StringEscapeUtils.escapeJava(src);
        String unicodeUnescaped = StringEscapeUtils.unescapeJava(unicodeEscaped);

        System.out.format("*** unicodeEscaped = %s\n*** unicodeUnescaped = %s\n", unicodeEscaped, unicodeUnescaped);
    }
}
