package com.mixram.telegram.bot.config;

import com.mixram.telegram.bot.service.BotExecutorService;
import com.mixram.telegram.bot.service.ex.TelegramApiException;
import com.mixram.telegram.bot.service.interfaces.LongPollingExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Main configuration file.
 *
 * @author mixram on 2018-04-20.
 * @since 0.1.0.0
 */
@Slf4j
@Configuration
public class AppConfiguration implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        try {
            for (LongPollingExecutor bot : longPollingExecutors) {
                service.registerBot(bot);
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Bean
    @ConditionalOnMissingBean(BotExecutorService.class)
    public BotExecutorService botExecutorService() {
        return new BotExecutorService();
    }

    /*===Private elements===*/


    /*===Util elements===*/

    //TODO: to realize a webhook executor initialization

    private final List<LongPollingExecutor> longPollingExecutors;

    private BotExecutorService service;

    @Autowired
    public void setService(BotExecutorService service) {
        this.service = service;
    }

    @Autowired
    public AppConfiguration(List<LongPollingExecutor> longPollingExecutors) {
        this.longPollingExecutors = longPollingExecutors;
    }
}
