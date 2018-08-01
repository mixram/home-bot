package com.mixram.telegram.bot.services.shedulers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * Component to manage work with schedulers for "long pooling" Telegram communication type.
 *
 * @author mixram on 2018-07-31.
 * @since 0.1.0.0
 */
@Slf4j
public class LongPoolingScheduler implements Scheduler {

    @Override
    @Scheduled(cron = "${scheduler.long.pooling.cron.time}")
    public void schedule() {
        log.debug("Long pooling scheduler is alive! {}", LocalDateTime.now());
    }

    /*===Private elements===*/


    /*===Util elements===*/

}
