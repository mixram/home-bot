package com.mixram.telegram.bot.utils.htmlparser;

/**
 * @author mixram on 2019-04-09.
 * @since 0.1.1.0
 */
public interface HtmlPageParser {

    /**
     * TO parse page been specified by URL.
     *
     * @param parseData URL to parse.
     *
     * @return the result of page parsing.
     *
     * @since 0.1.1.0
     */
    ParseData parse(ParseData parseData);
}
