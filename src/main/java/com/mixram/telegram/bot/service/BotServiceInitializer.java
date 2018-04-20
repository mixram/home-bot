package com.mixram.telegram.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Component to initialize the work with {@link BotService}.
 *
 * @author mixram on 2018-04-20.
 * @since 0.1.0.0
 */
@Slf4j
@Component
public class BotServiceInitializer {

    @Autowired
    public BotServiceInitializer(BotService botService) {
        ApiContextInitializer.init();
        TelegramBotsApi botApi = new TelegramBotsApi();

        try {
            botApi.registerBot(botService);
        } catch (TelegramApiException e) {
            log.warn("", e);
        }
    }
}
