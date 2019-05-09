package com.mixram.telegram.bot.services.services.discounts;

import com.mixram.telegram.bot.services.domain.entity.Data3DPlastic;
import com.mixram.telegram.bot.utils.htmlparser.HtmlPageParser;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseData;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseDataSettings;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseDataSettingsHolder;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mixram on 2019-05-03.
 * @since 1.4.2.0
 */
@Log4j2
abstract class DiscountsOn3DPlasticService implements DiscountsOnPlasticService {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final ParseDataSettingsHolder settings;
    private final long waitTime;

    private final HtmlPageParser parser;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    public DiscountsOn3DPlasticService(ParseDataSettingsHolder settings,
                                       long waitTime,
                                       HtmlPageParser parser) {
        this.settings = settings;
        this.waitTime = waitTime;
        this.parser = parser;
    }

    // </editor-fold>


    @Override
    public Data3DPlastic search() {
        List<ParseDataSettings> settings = this.settings.getSettings();
        String shopUrl = this.settings.getShopUrl();

        List<ParseData> validData = new ArrayList<>(settings.size());
        List<ParseData> brokenData = new ArrayList<>();

        LocalDateTime nextTime = LocalDateTime.now();
        for (ParseDataSettings data : settings) {
            while (true) {
                if (LocalDateTime.now().isAfter(nextTime)) {
                    try {
                        validData.addAll(parser.parse(data));
                    } catch (Exception e) {
                        log.warn("Exception of html-page parsing!", e);

                        brokenData.add(ParseData.builder()
                                                .commonUrl(data.getCommonUrl())
                                                .type(data.getType())
                                                .build());
                    }

                    nextTime = LocalDateTime.now().plus(waitTime, ChronoUnit.MILLIS);

                    break;
                }
            }
        }

        validData.forEach(d -> d.setShopUrl(shopUrl));

        return Data3DPlastic.builder()
                            .data(validData)
                            .brokenUrls(brokenData)
                            .build();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
