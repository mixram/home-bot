package com.mixram.telegram.bot.services.shedulers;

import com.mixram.telegram.bot.services.reminders.Reminder;
import com.mixram.telegram.bot.utils.AsyncHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author mixram on 2019-04-26.
 * @since 1.4.1.0
 */
@Slf4j
@Component
public class RemindersScheduler implements Scheduler {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final boolean doSchedule;

    private final Set<Reminder> reminders;
    private final AsyncHelper asyncHelper;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public RemindersScheduler(@Value("${bot.settings.scheduler.reminders.enable}") boolean doSchedule,
                              Set<Reminder> reminders,
                              AsyncHelper asyncHelper) {
        this.doSchedule = doSchedule;
        this.reminders = reminders;
        this.asyncHelper = asyncHelper;
    }

    @PostConstruct
    public void init() {
        //
    }

    // </editor-fold>


    @Override
    @Scheduled(cron = "${bot.settings.scheduler.reminders.cron-time}")
    public void schedule() {
        if (!doSchedule) {
            return;
        }

        doSchedule();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.2.2.0
     */
    private void doSchedule() {
        reminders.forEach(lp -> asyncHelper.doAsync((Supplier<Void>) () -> {
            lp.remind();

            return null;
        }));
    }

    // </editor-fold>
}
