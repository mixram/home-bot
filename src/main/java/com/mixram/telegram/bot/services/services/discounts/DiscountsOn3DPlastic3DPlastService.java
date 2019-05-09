package com.mixram.telegram.bot.services.services.discounts;

import com.mixram.telegram.bot.services.domain.entity.Data3DPlastic;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import com.mixram.telegram.bot.utils.htmlparser.HtmlPageParser;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseDataSettingsHolder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author mixram on 2019-05-03.
 * @since 1.4.2.0
 */
@Log4j2
@Service
public class DiscountsOn3DPlastic3DPlastService extends DiscountsOn3DPlasticService {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    //

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsOn3DPlastic3DPlastService(@Value("${parser.3dplast.settings}") String settings,
                                              @Value("${parser.3dplast.time-to-wait-till-parse-new-url}") long waitTime,
                                              @Qualifier("htmlPage3DPlastParser") HtmlPageParser parser) {
        super(JsonUtil.fromJson(settings, ParseDataSettingsHolder.class), waitTime, parser);
    }

    // </editor-fold>


    /**
     * To search for discounts.
     *
     * @return data or exception.
     *
     * @since 1.4.2.0
     */
    @Override
    public Data3DPlastic search() {
        return super.search();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
