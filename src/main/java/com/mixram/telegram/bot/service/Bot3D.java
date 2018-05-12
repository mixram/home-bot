package com.mixram.telegram.bot.service;

import com.mixram.telegram.bot.service.parsers.HtmlPageParser;
import com.mixram.telegram.bot.service.parsers.JyskHtmlPageParser;
import com.mixram.telegram.bot.service.parsers.pojo.ParseData;
import com.mixram.telegram.bot.utils.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * The bot :-)
 *
 * @author mixram on 2018-04-20.
 * @since 0.1.0.0
 */
@Slf4j
public class Bot3D extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Incoming update of type {}: {}",
                  update.getClass().getSimpleName(),
                  update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String text = message.getText();

            SendMessage messageToSend;
            if (PARSE_PATTERN.matcher(text.toUpperCase()).matches()) {
                String messageToSendString;
                String searchType = text.substring(text.indexOf(":") + 1).toUpperCase();
                switch (searchType) {
                    case "JYSK":
                        sendResponse(new SendMessage()
                                             .setChatId(message.getChatId())
                                             .setText(WAIT_SEARCH_MESSAGE));

                        ParseData parsed = juskParser.parse(JYSK_URL + "/campaigns");
                        messageToSendString = new StringBuilder()
                                .append("<b>").append(searchType).append("</b>").append("\n")
                                .append("Заголовок: ").append(parsed.getPageTitle()).append(";\n")
                                .append("Продукт: ").append(parsed.getProductName()).append(";\n")
                                .append("Обычная цена: ").append(parsed.getProductPrice()).append("грн;\n")
                                .append("Цена со скидкой: ").append(parsed.getProductSalePrice()).append("грн;\n")
                                .append("Ссылка: ").append(JYSK_URL).append(parsed.getProductUrl()).append(".")
                                .toString();
                        break;
                    default:
                        messageToSendString = "<b>" + searchType + "</b>\n" + "Тип поиска '" + searchType + "' не поддерживается.";
                }

                messageToSend = new SendMessage()
                        .setChatId(message.getChatId())
                        .setText(messageToSendString)
                        .enableHtml(true);
                sendResponse(messageToSend);
            } else {
                messageToSend = new SendMessage()
                        .setChatId(message.getChatId())
                        .setText(MISUNDERSTANDING_MESSAGE);
                sendResponse(messageToSend);
            }
        }
    }

    /**
     * //
     *
     * @param message
     *
     * @since 0.1.1.0
     */
    private void sendResponse(SendMessage message) {
        try {
            log.debug("Response: {}", message);
            execute(message);
        } catch (TelegramApiException e) {
            log.warn("", e);

            SendMessage messageToSendError = new SendMessage()
                    .setChatId(message.getChatId())
                    .setText(ERROR_MESSAGE);
            try {
                execute(messageToSendError);
            } catch (TelegramApiException e1) {
                log.warn("", e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return appProperties.getProperty(BOT_NAME);
    }

    @Override
    public String getBotToken() {
        return appProperties.getProperty(BOT_TOKEN);
    }


    /*===Private elements===*/


    /*===Util elements===*/

    private static final String PARSE_PATTERN_STRING = "^SALES:.*";
    private static final Pattern PARSE_PATTERN = Pattern.compile(PARSE_PATTERN_STRING);
    private static final String MISUNDERSTANDING_MESSAGE = "Моя твоя не понимать... :-)";
    private static final String ERROR_MESSAGE = "Упс... Что-то пошло не так... :-)";
    private static final String WAIT_SEARCH_MESSAGE = "Подождите, пожалуйста, обрабатываю запрос...";
    private static final String JYSK_URL = "https://jysk.ua";

    private final String BOT_NAME = "name";
    private final String BOT_TOKEN = "token";

    private final AppProperties appProperties;
    private final HtmlPageParser juskParser;

    /**
     * To create an instance of {@link Bot3D}.
     *
     * @param driverClass web driver class.
     *
     * @since 0.1.1.0
     */
    public Bot3D(Class<? extends WebDriver> driverClass) throws IOException, InstantiationException, IllegalAccessException {
        this.appProperties = new AppProperties();
        this.juskParser = new JyskHtmlPageParser(driverClass);

        this.appProperties.checkPropertyExists(BOT_NAME);
        this.appProperties.checkPropertyExists(BOT_TOKEN);
    }
}
