package com.mixram.telegram.bot.utils.htmlparser;

import com.mixram.telegram.bot.utils.htmlparser.entity.ParseData;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseDataSettings;

import java.util.List;

/**
 * @author mixram on 2019-05-02.
 * @since 1.4.2.0
 */
public interface HtmlPageParser {

    /**
     * To parse page been specified by URL.
     *
     * @param parseData URL to parse.
     *
     * @return the list of page parsing results.
     *
     * @since 1.4.2.0
     */
    List<ParseData> parse(ParseDataSettings parseData);
}
