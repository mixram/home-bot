package com.mixram.telegram.bot.config;

import com.mixram.telegram.bot.config.props.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(appProperties.getRequiredProperty("scheduler.base.pool.size", Integer :: valueOf));
        taskScheduler.initialize();

        taskRegistrar.setTaskScheduler(taskScheduler);
    }


    /*===Private elements===*/


    /*===Util elements===*/

    private final AppProperties appProperties;

    @Autowired
    public SchedulerConfig(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
}
