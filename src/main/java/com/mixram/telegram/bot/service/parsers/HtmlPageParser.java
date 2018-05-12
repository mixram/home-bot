package com.mixram.telegram.bot.service.parsers;

import com.mixram.telegram.bot.service.parsers.pojo.ParseData;

/**
 * Interface for parsers.
 *
 * @author mixram on 2018-05-12.
 * @since 0.1.1.0
 */
public interface HtmlPageParser {

    /**
     * TO parse page been specified by URL.
     *
     * @param url URL to parse.
     *
     * @return the result of page parsing.
     *
     * @since 0.1.1.0
     */
    ParseData parse(String url);
}
