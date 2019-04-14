package com.mixram.telegram.bot.services.services;

import com.google.common.collect.Lists;
import com.mixram.telegram.bot.services.domain.LongPooling;
import com.mixram.telegram.bot.services.domain.entity.*;
import com.mixram.telegram.bot.services.domain.ex.TelegramApiException;
import com.mixram.telegram.bot.utils.CommonHeadersBuilder;
import com.mixram.telegram.bot.utils.ConcurrentUtilites;
import com.mixram.telegram.bot.utils.rest.RestClient;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@Log4j2
@Component
public class Bot3DLongPooling implements LongPooling {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String ERROR_MESSAGE = "Упс... Что-то пошло не так...\uD83D\uDE33";

    private static final String GET_ME_URL = "/getMe";
    private static final String GET_UPDATES_URL = "/getUpdates";
    private static final String SEND_MESSAGE_URL = "/sendMessage";

    private final String botName;
    private final String adminName;
    private final String mainUrlPart;

    private final RestClient restClient;
    private final Bot3DComponent bot3DComponent;

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
    public Bot3DLongPooling(@Value("${bot.settings.base-url}") String telegramUrl,
                            @Value("${bot.settings.bot-token}") String botToken,
                            @Value("${bot.settings.bot-name}") String botName,
                            @Value("${bot.settings.admin-username}") String adminName,
                            @Qualifier("bot3DComponentImpl") Bot3DComponent bot3DComponent,
                            RestClient restClient) {
        restClient.setAnchorForLog(Bot3DLongPooling.class.getSimpleName());
        this.restClient = restClient;
        this.bot3DComponent = bot3DComponent;

        this.botName = botName;
        this.adminName = adminName;
        this.mainUrlPart = telegramUrl + "/bot" + botToken;
    }

    @PostConstruct
    public void init() {
        assertBot();
    }

    // </editor-fold>


    @Override
    public void check() {
        log.debug("{} is started!", Bot3DLongPooling.class :: getSimpleName);

        List<Update> updates = getUpdates();

        Map<Update, CompletableFuture<String>> answers = new HashMap<>(updates.size());
        updates.forEach(u -> answers.put(u, ConcurrentUtilites.supplyAsyncWithLocalThreadContext(
                aVoid -> bot3DComponent.proceedUpdate(u))));
        answers.forEach((k, v) -> sendMessage(k, v.join()));

        //        updates.forEach(u -> sendMessage(u, bot3DComponent.proceedUpdate(u)));

        //        for (Update update : updates) {
        //            sendMessage(update, bot3DComponent.proceedUpdate(update));
        //        }

    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 0.1.3.0
     */
    private void sendMessage(Update update,
                             String answer) {
        if (answer == null) {
            return;
        }

        try {
            log.debug("sendMessage => : update={}, answer={}",
                      () -> update,
                      () -> answer);

            doSendMessage(update, answer);
        } catch (Exception e) {
            log.warn("", e);

            try {
                doSendMessage(update, ERROR_MESSAGE);
            } catch (TelegramApiException e1) {
                log.warn("", e);
            }
        }
    }

    /**
     * @since 0.1.3.0
     */
    private void doSendMessage(Update update,
                               String answer) {
        Message mess = update.getMessage();
        Long chatId = mess.getChat()
                          .getChatId();
        Integer messageId = mess.getMessageId();

        SendMessage message = SendMessage.builder()
                                         .chatId(chatId.toString())
                                         .text(answer)
                                         .parseMode("HTML")
                                         .disableWebPagePreview(false)
                                         .disableNotification(false)
                                         .replyToMessageId(messageId)
                                         .build();

        String url = mainUrlPart + SEND_MESSAGE_URL;
        HttpHeaders headers = CommonHeadersBuilder.newInstance()
                                                  .json()
                                                  .build();

        AnswerResponse answerResponse = restClient.post(url, headers.toSingleValueMap(), message, AnswerResponse.class);
        Validate.notNull(answerResponse, "Empty answer!");
        Validate.isTrue(answerResponse.getResult(), "An error in process of answer sending! %s", answerResponse);

        log.debug("Answer on answer: {}", () -> answerResponse);
    }

    /**
     * @since 0.1.2.0
     */
    private List<Update> getUpdates() {
        List<Update> result = null;

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
        }

        return result;
    }

    /**
     * @since 0.1.2.0
     */
    private void assertBot() {
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

    // </editor-fold>

}
