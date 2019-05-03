package com.mixram.telegram.bot.services.services.discounts;

import com.mixram.telegram.bot.services.domain.entity.Data3DPlastic;
import com.mixram.telegram.bot.utils.htmlparser.HtmlPageParserLegacy;
import com.mixram.telegram.bot.utils.htmlparser.v2.entity.ParseData;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mixram on 2019-04-15.
 * @since 0.2.0.0
 * @deprecated legacy since 1.4.2.0, use 'v2' instead.
 */
@Deprecated
@Log4j2
public abstract class DiscountsOn3DPlasticV2Service implements DiscountsOnPlasticServiceLegacy {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final List<ParseData> urls;
    private final long waitTime;

    private final HtmlPageParserLegacy parser;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    public DiscountsOn3DPlasticV2Service(List<ParseData> urls,
                                         long waitTime,
                                         HtmlPageParserLegacy parser) {
        this.urls = urls;
        this.waitTime = waitTime;
        this.parser = parser;
    }

    // </editor-fold>


    @Override
    public Data3DPlastic search() {
        List<ParseData> data = new ArrayList<>(urls.size());
        List<ParseData> brokenUrls = new ArrayList<>();

        LocalDateTime nextTime = LocalDateTime.now();
        for (ParseData url : urls) {
            while (true) {
                if (LocalDateTime.now().isAfter(nextTime)) {
                    try {
                        data.add(parser.parse(url));
                    } catch (Exception e) {
                        log.warn("Exception of html-page parsing!", e);

                        brokenUrls.add(url);
                    }

                    nextTime = LocalDateTime.now().plus(waitTime, ChronoUnit.MILLIS);

                    break;
                }
            }
        }

        return Data3DPlastic.builder()
                            .data(data)
                            .brokenUrls(brokenUrls)
                            .build();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
