package com.mixram.telegram.bot.service;

import com.mixram.telegram.bot.service.interfaces.BotSession;
import com.mixram.telegram.bot.service.interfaces.LongPollingExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for bot management.
 *
 * @author mixram on 2018-04-25.
 * @since 0.1.0.0
 */
@Slf4j
@Service
public class BotExecutorService {

    /**
     * Register a Telegram bot.<br>
     * The {@link BotSession} is started immediately, and may be disconnected by calling close.
     *
     * @param bot the Telegram bot to register.
     *
     * @since 0.1.0.0
     */
    public BotSession registerBot(LongPollingExecutor bot) {
        BotSession session = ApiContext.getInstance(BotSession.class);
        session.setToken(bot.getBotToken());
        session.setOptions(bot.getOptions());
        session.setCallback(bot);
        session.start();

        return session;
    }


    /*===Private elements===*/


    /*===Util elements===*/

    public BotExecutorService() {
    }
}
