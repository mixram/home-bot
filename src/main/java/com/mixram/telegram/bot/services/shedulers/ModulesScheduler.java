package com.mixram.telegram.bot.services.shedulers;

import com.mixram.telegram.bot.services.modules.Module;
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
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@Slf4j
@Component
public class ModulesScheduler implements Scheduler {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final boolean doSchedule;
    private final boolean doWorkOnStart;

    private final Set<Module> modules;
    private final AsyncHelper asyncHelper;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public ModulesScheduler(@Value("${bot.settings.scheduler.modules.enable}") boolean doSchedule,
                            @Value("${bot.settings.scheduler.modules.do-on-app-start.enable}") boolean doWorkOnStart,
                            Set<Module> modules,
                            AsyncHelper asyncHelper) {
        this.doSchedule = doSchedule;
        this.doWorkOnStart = doWorkOnStart;
        this.modules = modules;
        this.asyncHelper = asyncHelper;
    }

    @PostConstruct
    public void init() {
        if (doWorkOnStart) {
            schedule();
        }
    }

    // </editor-fold>


    @Override
    @Scheduled(cron = "${bot.settings.scheduler.modules.cron-time}")
    public void schedule() {
        if (!doSchedule) {
            return;
        }

        modules.forEach(lp -> asyncHelper.doAsync((Supplier<Void>) () -> {
            lp.execute();

            return null;
        }));
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
