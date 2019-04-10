package com.mixram.telegram.bot.services.shedulers;

import com.mixram.telegram.bot.services.domain.LongPooling;
import com.mixram.telegram.bot.utils.AsyncHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Supplier;

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
    private final AsyncHelper asyncHelper;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public LongPoolingScheduler(@Value("${bot.settings.scheduler.long-pooling.enable}") boolean doSchedule,
                                Set<LongPooling> longPoolings,
                                AsyncHelper asyncHelper) {
        this.doSchedule = doSchedule;
        this.longPoolings = longPoolings;
        this.asyncHelper = asyncHelper;
    }

    // </editor-fold>


    @Override
    @Scheduled(cron = "${bot.settings.scheduler.long-pooling.cron-time}")
    public void schedule() {
        if (!doSchedule) {
            return;
        }

        longPoolings.forEach(lp -> asyncHelper.doAsync((Supplier<Void>) () -> {
            lp.check();

            return null;
        }));
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
