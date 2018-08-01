package com.mixram.telegram.bot.config;

import com.mixram.telegram.bot.services.shedulers.LongPoolingScheduler;
import com.mixram.telegram.bot.services.shedulers.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for all app.
 *
 * @author mixram on 2018-07-31.
 * @since 0.1.0.0
 */
@Configuration
public class AppConfig {

    @Bean("longPoolingScheduler")
    public Scheduler longPoolingScheduler() {
        return new LongPoolingScheduler();
    }
}
