package com.mixram.telegram.bot.services.modules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import com.mixram.telegram.bot.utils.htmlparser.HtmlPageParser;
import com.mixram.telegram.bot.utils.htmlparser.ParseData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@Log4j2
@Service
class DiscountsOn3DPlastic3DPlastService {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final List<ParseData> urls;

    private final HtmlPageParser parser;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    public DiscountsOn3DPlastic3DPlastService(@Qualifier("htmlPage3DPlastParserV2") HtmlPageParser parser,
                                              @Value("${parser.3dplast.urls}") String urls) {
        this.parser = parser;
        this.urls = JsonUtil.fromJson(urls, new TypeReference<>() {});
    }

    // </editor-fold>


    /**
     * To search for discounts.
     *
     * @return data or exception.
     *
     * @since 0.1.0.0
     */
    protected Data3DPlastic search() {
        List<ParseData> data = new ArrayList<>(urls.size());
        urls.forEach(u -> {
            try {
                data.add(parser.parse(u));
            } catch (Exception e) {
                log.warn("Exception of html-page parsing!", e);
            }
        });

        return Data3DPlastic.builder()
                            .data(data)
                            .build();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
