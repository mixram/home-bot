package com.mixram.telegram.bot.utils.htmlparser;

import com.mixram.telegram.bot.utils.htmlparser.v2.entity.ParseData;

/**
 * @author mixram on 2019-04-09.
 * @since 0.1.1.0
 * @deprecated legacy since 1.4.2.0, use 'v2'.
 */
@Deprecated
public interface HtmlPageParser {

    /**
     * To parse page been specified by URL.
     *
     * @param parseData URL to parse.
     *
     * @return the result of page parsing.
     *
     * @since 0.1.1.0
     */
    ParseData parse(ParseData parseData);
}
