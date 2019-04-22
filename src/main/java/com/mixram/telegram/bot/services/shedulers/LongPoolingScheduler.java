package com.mixram.telegram.bot.services.shedulers;

import com.mixram.telegram.bot.services.domain.LongPooling;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Component to manage work with schedulers for "long pooling" Telegram communication type.
 *
 * @author mixram on 2018-07-31.
 * @since 0.1.0.0
 */
@Log4j2
@Component
public class LongPoolingScheduler implements Scheduler {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final boolean doSchedule;

    private final Set<LongPooling> longPoolings;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public LongPoolingScheduler(@Value("${bot.settings.scheduler.long-pooling.enable}") boolean doSchedule,
                                Set<LongPooling> longPoolings) {
        this.doSchedule = doSchedule;
        this.longPoolings = longPoolings;
    }

    // </editor-fold>


    @Override
    @Scheduled(cron = "${bot.settings.scheduler.long-pooling.cron-time}")
    public void schedule() {
        if (!doSchedule) {
            return;
        }

        //not to do in async - Telegram API returns '409 Conflict'
        longPoolings.forEach(LongPooling :: check);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
