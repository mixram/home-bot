package com.mixram.telegram.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "customPoolTaskExecutor")
    public Executor customPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("custom_async_");

        return executor;
    }
}
