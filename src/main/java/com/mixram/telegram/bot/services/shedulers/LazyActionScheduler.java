package com.mixram.telegram.bot.services.shedulers;

import com.mixram.telegram.bot.services.services.lazyaction.LazyActionLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author mixram on 2020-07-20.
 * @since 1.8.2.0
 */
@Component
public class LazyActionScheduler implements Scheduler {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final LazyActionLogic lazyAction;

    private final boolean doSchedule;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">


    @Autowired
    public LazyActionScheduler(@Value("${bot.settings.scheduler.lazy-action.enable}") boolean doSchedule,
                               LazyActionLogic lazyAction) {
        this.lazyAction = lazyAction;
        this.doSchedule = doSchedule;
    }


    // </editor-fold>


    @Override
    @Scheduled(cron = "${bot.settings.scheduler.lazy-action.cron-time}")
    public void schedule() {
        if (!doSchedule) {
            return;
        }

        lazyAction.doLazyAction();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
