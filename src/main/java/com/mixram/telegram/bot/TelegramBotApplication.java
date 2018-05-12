package com.mixram.telegram.bot;

import com.mixram.telegram.bot.service.Bot3D;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Main class for bot.
 *
 * @author mixram on 2018-04-20.
 * @since 0.1.0.0
 */
@Slf4j
public class TelegramBotApplication {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting the application...");

        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            System.out.println("Registering the bot...");

            botsApi.registerBot(new Bot3D(ChromeDriver.class));

            System.out.println("The bot have registered successfully...");
            System.out.println("The application have started successfully...");
        } catch (TelegramApiException e) {
            log.warn("", e);
        }
    }
}
