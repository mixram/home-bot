package com.mixram.telegram.bot.services.services.tapicom;

import com.google.common.collect.Lists;
import com.mixram.telegram.bot.services.domain.entity.*;
import com.mixram.telegram.bot.services.domain.ex.TelegramApiException;
import com.mixram.telegram.bot.services.services.bot.Bot3DLongPooling;
import com.mixram.telegram.bot.services.services.tapicom.entity.SendMessageData;
import com.mixram.telegram.bot.utils.CommonHeadersBuilder;
import com.mixram.telegram.bot.utils.rest.RestClient;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mixram on 2019-04-22.
 * @since 1.3.0.0
 */
@Log4j2
@Service
class TelegramAPICommunicationServices {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String ERROR_MESSAGE = "Упс... Что-то пошло не так...\uD83D\uDE33";

    private static final String GET_ME_URL = "/getMe";
    private static final String GET_UPDATES_URL = "/getUpdates";
    private static final String SEND_MESSAGE_URL = "/sendMessage";

    private final String botName;
    private final String adminName;
    private final String mainUrlPart;

    private final RestClient restClient;

    /**
     * Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of
     * previously received updates. By default, updates starting with the earliest unconfirmed update are returned. An
     * update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id. The negative
     * offset can be specified to retrieve updates starting from -offset update from the end of the updates queue. All
     * previous updates will forgotten.
     */
    private AtomicInteger offset;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    TelegramAPICommunicationServices(@Value("${bot.settings.base-url}") String telegramUrl,
                                     @Value("${bot.settings.bot-token}") String botToken,
                                     @Value("${bot.settings.bot-name}") String botName,
                                     @Value("${bot.settings.admin-username}") String adminName,
                                     RestClient restClient) {
        restClient.setAnchorForLog(Bot3DLongPooling.class.getSimpleName());
        this.restClient = restClient;

        this.botName = botName;
        this.adminName = adminName;
        this.mainUrlPart = telegramUrl + "/bot" + botToken;
    }

    // </editor-fold>


    /**
     * To send message to Telegram API.
     *
     * @param update  "update" entity from Telegram API.
     * @param message message to send to Telegram API.
     *
     * @since 0.1.3.0
     */
    protected void sendMessage(Update update,
                               String message) {
        if (update == null || message == null) {
            return;
        }

        try {
            log.debug("sendMessage => : update={}, message={}",
                      () -> update,
                      () -> message);

            SendMessageData data = createSendMessageData(update);

            doSendMessage(data.getChatId(), data.getMessageId(), message);
        } catch (Exception e) {
            log.warn("", e);

            try {
                SendMessageData data = createSendMessageData(update);

                doSendMessage(data.getChatId(), data.getMessageId(), ERROR_MESSAGE);
            } catch (TelegramApiException e1) {
                log.warn("", e);
            }
        }
    }

    /**
     * To send message to Telegram API for bot admin.
     *
     * @param message message to send to Telegram API.
     *
     * @since 0.1.3.0
     */
    protected void sendMessageToAdmin(String message) {
        Validate.notBlank(message, "Message for admin is not specified!");

        try {
            log.debug("sendMessage => : message={}", () -> message);

            doSendMessage(Integer.valueOf(adminName), null, message);
        } catch (Exception e) {
            log.warn("", e);
        }
    }

    /**
     * To get all updates from Telegram API.
     *
     * @return a list of updates (may be empty) or exception.
     *
     * @since 0.1.3.0
     */
    protected List<Update> getUpdates() {
        List<Update> result;

        String url = mainUrlPart + GET_UPDATES_URL;
        HttpHeaders headers = CommonHeadersBuilder.newInstance()
                                                  .json()
                                                  .build();
        Map<String, String> params = new HashMap<>();
        if (offset != null) {
            params.put("offset", String.valueOf(offset.addAndGet(1)));
        }

        try {
            UpdateResponse updatesHolder = restClient.get(url, params, headers.toSingleValueMap(), UpdateResponse.class);
            Validate.notNull(updatesHolder, "Empty answer!");
            Validate.isTrue(updatesHolder.getResult(), "An error in process of updates getting! %s", updatesHolder);

            List<Update> updates = Optional.ofNullable(updatesHolder.getData()).orElse(
                    Lists.newArrayListWithExpectedSize(0));
            offset = updates.stream()
                            .map(Update :: getUpdateId)
                            .max(Comparator.naturalOrder())
                            .map(AtomicInteger ::new)
                            .orElse(null);

            result = updatesHolder.getData();
        } catch (Exception e) {
            log.warn("Exception in process of updates receiving!", e);

            result = Lists.newArrayListWithExpectedSize(0);
        }

        return result;
    }

    /**
     * To assert bot`s data.
     *
     * @since 0.1.3.0
     */
    protected void assertBot() {
        String url = mainUrlPart + GET_ME_URL;
        HttpHeaders headers = CommonHeadersBuilder.newInstance()
                                                  .json()
                                                  .build();

        WhoAmI whoAmI = restClient.get(url, headers.toSingleValueMap(), WhoAmI.class);

        Validate.isTrue(whoAmI != null && whoAmI.getResult() != null, "Empty answer!");
        Validate.isTrue(whoAmI.getResult(), "Error in service! %s", whoAmI);
        Validate.notNull(whoAmI.getResultData(), "No data about bot!");
        Validate.isTrue(botName.equals(whoAmI.getResultData().getUsername()), "Unexpected bot`s name: '%s'!",
                        whoAmI.getResultData().getUsername());

        log.info("BOT: {}", () -> whoAmI);
    }

    /**
     * To get admin`s name (code).
     *
     * @return admin`s name.
     *
     * @since 1.3.0.0
     */
    protected String getAdminName() {
        return adminName;
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.0.0.0
     */
    private SendMessageData createSendMessageData(Update update) {
        Message mess = update.getMessage();
        Integer chatId = mess.getChat()
                             .getChatId();
        Integer messageId = mess.getMessageId();

        return new SendMessageData(chatId, messageId);
    }

    /**
     * @since 0.1.3.0
     */
    private void doSendMessage(Integer chatId,
                               Integer messageId,
                               String message) {
        SendMessage messageToSend = SendMessage.builder()
                                               .chatId(chatId.toString())
                                               .text(message)
                                               .parseMode("HTML")
                                               .disableWebPagePreview(false)
                                               .disableNotification(false)
                                               .replyToMessageId(messageId)
                                               .build();

        String url = mainUrlPart + SEND_MESSAGE_URL;
        HttpHeaders headers = CommonHeadersBuilder.newInstance()
                                                  .json()
                                                  .build();

        AnswerResponse answerResponse =
                restClient.post(url, headers.toSingleValueMap(), messageToSend, AnswerResponse.class);
        Validate.notNull(answerResponse, "Empty message!");
        Validate.isTrue(answerResponse.getResult(), "An error in process of message sending! %s", answerResponse);

        log.debug("Answer on message: {}", () -> answerResponse);
    }

    // </editor-fold>
}
