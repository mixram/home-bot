package com.mixram.telegram.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Main class for test-bot.
 *
 * @author mixram on 2018-04-20.
 * @since 0.1.0.0
 */
@Slf4j
@Service
public class BotService extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null) {
            Long chatId = message.getChatId();
            if (chatId != null) {
                sendMsg(chatId.toString(), message.getText());
            }
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return "Nerd3dBot";
    }


    /*===Private elements===*/

    private synchronized void sendMsg(String chatId,
                                      String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            log.warn("", e);
        }
    }


    /*===Util elements===*/

    private final String botToken;

    @Autowired
    public BotService(String botToken) {
        this.botToken = botToken;
    }
}
