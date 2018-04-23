package com.mixram.telegram.bot.service;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The bot :-)
 *
 * @author mixram on 2018-04-20.
 * @since 0.1.0.0
 */
public class Bot3D extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Create a SendMessage object with mandatory fields
            SendMessage message = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                System.out.println("ANSWER: " + message);
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return props.getProperty(BOT_NAME);
    }

    @Override
    public String getBotToken() {
        return props.getProperty(BOT_TOKEN);
    }


    /*===Private elements===*/


    /*===Util elements===*/

    private final String BOT_NAME = "name";
    private final String BOT_TOKEN = "token";

    private final Properties props = new Properties();

    public Bot3D() throws IOException {
        try(InputStream is = new FileInputStream(System.getProperty("application.props"))) {
            props.load(is);
        }
    }
}
