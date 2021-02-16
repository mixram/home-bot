package com.mixram.telegram.bot.services.shedulers;

import com.mixram.telegram.bot.services.services.market.MarketLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author mixram on 2021-02-16.
 * @since 1.8.8.0
 */
@Component
public class MarketLogicScheduler implements Scheduler {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final MarketLogic marketLogic;

    private final boolean doSchedule;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">


    @Autowired
    public MarketLogicScheduler(@Value("${bot.settings.scheduler.market-media-group-action.enable}") boolean doSchedule,
                                MarketLogic marketLogic) {
        this.marketLogic = marketLogic;
        this.doSchedule = doSchedule;
    }


    // </editor-fold>


    @Override
    @Scheduled(cron = "${bot.settings.scheduler.market-media-group-action.cron-time}")
    public void schedule() {
        if (!doSchedule) {
            return;
        }

        marketLogic.doPostponedAction();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
