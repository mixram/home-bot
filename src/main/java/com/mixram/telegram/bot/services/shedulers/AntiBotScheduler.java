package com.mixram.telegram.bot.services.shedulers;

import com.mixram.telegram.bot.services.services.antibot.AntiBotImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author mixram on 2020-01-20.
 * @since 1.7.0.0
 */
@Log4j2
@Component
public class AntiBotScheduler implements Scheduler {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final AntiBotImpl antiBot;

    private final boolean doSchedule;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public AntiBotScheduler(@Value("${bot.settings.scheduler.anti-bot.enable}") boolean doSchedule,
                            AntiBotImpl antiBot) {
        this.antiBot = antiBot;
        this.doSchedule = doSchedule;
    }


    // </editor-fold>


    @Override
    @Scheduled(cron = "${bot.settings.scheduler.anti-bot.cron-time}")
    public void schedule() {
        if (!doSchedule) {
            return;
        }

        antiBot.checkUsers();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
