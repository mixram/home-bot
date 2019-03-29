package com.mixram.telegram.bot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Configuration for schedulers management.
 *
 * @author mixram on 2018-07-31.
 * @since 0.1.0.0
 */
@EnableScheduling
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final Integer poolSize;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public SchedulerConfig(@Value("${bot.settings.scheduler.base-pool-size}") Integer poolSize) {
        this.poolSize = poolSize;
    }

    // </editor-fold>


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(poolSize);
        taskScheduler.initialize();

        taskRegistrar.setTaskScheduler(taskScheduler);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
