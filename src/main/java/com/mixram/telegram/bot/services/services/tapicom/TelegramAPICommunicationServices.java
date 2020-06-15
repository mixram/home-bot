package com.mixram.telegram.bot.services.services.tapicom;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.mixram.telegram.bot.services.domain.entity.*;
import com.mixram.telegram.bot.services.domain.ex.TelegramApiException;
import com.mixram.telegram.bot.services.services.bot.entity.MessageData;
import com.mixram.telegram.bot.services.services.tapicom.entity.SendMessageData;
import com.mixram.telegram.bot.utils.CommonHeadersBuilder;
import com.mixram.telegram.bot.utils.CustomMessageSource;
import com.mixram.telegram.bot.utils.META;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import com.mixram.telegram.bot.utils.rest.RestClient;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author mixram on 2019-04-22.
 * @since 1.3.0.0
 */
@Log4j2
@Service
class TelegramAPICommunicationServices {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String SOMETHING_WRONG_MESSAGE = "telegram.bot.message.something-wrong";

    private static final String GET_ME_URL = "/getMe";
    private static final String GET_UPDATES_URL = "/getUpdates";
    private static final String SEND_MESSAGE_URL = "/sendMessage";
    private static final String LEAVE_CHAT_URL = "/leaveChat";
    private static final String KICK_CHAT_MEMBER_URL = "/kickChatMember";
    private static final String DELETE_MESSAGE_URL = "/deleteMessage";
    private static final String UNBAN_CHAT_MEMBER_URL = "/unbanChatMember";

    private final String botName;
    private final String mainUrlPart;
    private final Integer secondsToBanUser;
    private final Set<Long> adminsPrime;

    private final RestClient restClient;
    private final CustomMessageSource messageSource;
    private final META meta;

    /**
     * Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of
     * previously received updates. By default, updates starting with the earliest unconfirmed update are returned. An
     * update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id. The
     * negative offset can be specified to retrieve updates starting from -offset update from the end of the updates
     * queue. All previous updates will forgotten.
     */
    private AtomicLong offset;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    TelegramAPICommunicationServices(@Value("${bot.settings.base-url}") String telegramUrl,
                                     @Value("${bot.settings.bot-token}") String botToken,
                                     @Value("${bot.settings.bot-name}") String botName,
                                     @Value("${bot.settings.admins-prime}") String adminsPrime,
                                     @Value("${bot.settings.time-to-ban-user-after-kick}") Integer secondsToBanUser,
                                     META meta,
                                     CustomMessageSource messageSource,
                                     RestClient restClient) {
        restClient.setAnchorForLog(this.getClass().getSimpleName());
        this.restClient = restClient;
        this.messageSource = messageSource;
        this.meta = meta;

        this.botName = botName;
        this.adminsPrime = JsonUtil.fromJson(adminsPrime, new TypeReference<Set<Long>>() {});
        this.mainUrlPart = telegramUrl + "/bot" + botToken;
        this.secondsToBanUser = secondsToBanUser;
    }

    // </editor-fold>


    /**
     * To send messageData to Telegram API.
     *
     * @param update      "update" entity from Telegram API.
     * @param messageData messageData to send to Telegram API.
     *
     * @since 0.1.3.0
     */
    protected void sendMessage(Update update,
                               MessageData messageData) {
        try {
            log.debug("sendMessage => : update={}, messageData={}",
                      () -> update,
                      () -> messageData);

            SendMessageData data = createSendMessageData(update, messageData.isUserResponse());

            doSendMessage(data.getChatId(), data.getMessageId(), messageData);

            if (messageData.isLeaveChat()) {
                try {
                    leaveChat(update.getMessage().getChat().getChatId().toString());
                } catch (Exception e) {
                    log.warn("Chat leaving error!", e);
                }
            }
        } catch (Exception e) {
            log.warn("", e);

            try {
                SendMessageData data = createSendMessageData(update, messageData.isUserResponse());
                MessageData messageDataNew =
                        MessageData.builder()
                                   .toResponse(true)
                                   .message(messageSource.getMessage(SOMETHING_WRONG_MESSAGE,
                                                                     defineLocale(update.getMessage().getUser())))
                                   .userResponse(messageData.isUserResponse())
                                   .build();

                doSendMessage(data.getChatId(), data.getMessageId(), messageDataNew);
            } catch (TelegramApiException e1) {
                log.warn("", e);
            }
        }
    }

    /**
     * To send messageData to concrete chat.
     *
     * @param chatId      chat ID.
     * @param messageData messageData to send to Telegram API.
     *
     * @since 1.4.1.0
     */
    protected void sendMessageToChat(Long chatId,
                                     MessageData messageData) {
        try {
            log.debug("sendMessageToChat => : chatId={}, messageData={}",
                      () -> chatId,
                      () -> messageData);

            doSendMessage(chatId, null, messageData);
        } catch (Exception e) {
            log.warn("sendMessageToChat ==> ERROR!", e);
        }
    }

    /**
     * To send messageData to Telegram API for bot admin.
     *
     * @param message messageData to send to Telegram API.
     *
     * @since 0.1.3.0
     */
    protected void sendMessageToAdmin(MessageData message) {
        try {
            log.debug("sendMessageToAdmin => : messageData={}", () -> message);

            adminsPrime.forEach(id -> doSendMessage(id, null, message));

            if (message.isLeaveChat()) {
                log.warn("Can not 'leave chat' from admin messages sending logic!");
            }
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
            UpdateResponse updatesHolder = restClient.get(url, params, headers.toSingleValueMap(),
                                                          UpdateResponse.class);
            Validate.notNull(updatesHolder, "Empty answer!");
            Validate.isTrue(updatesHolder.getResult(), "An error in process of updates getting! %s", updatesHolder);

            List<Update> updates = Optional.ofNullable(updatesHolder.getData()).orElse(
                    Lists.newArrayListWithExpectedSize(0));
            offset = updates.stream()
                            .map(Update::getUpdateId)
                            .max(Comparator.naturalOrder())
                            .map(AtomicLong::new)
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
     * To get admins IDs.
     *
     * @return IDs.
     *
     * @since 1.3.0.0
     */
    protected Set<Long> getAdmins() {
        return adminsPrime;
    }

    /**
     * To kick user from the chat.
     *
     * @param chatId chat ID.
     * @param userId user ID.
     *
     * @since 1.7.0.0
     */
    protected void kickUserFromChat(String chatId,
                                    String userId) {
        try {
            SendMessage sendMessage =
                    SendMessage.builder()
                               .chatId(chatId)
                               .userId(userId)
                               .untilDate(LocalDateTime.now()
                                                       .plusSeconds(secondsToBanUser)
                                                       .atZone(ZoneId.systemDefault()).toInstant()
                                                       .getEpochSecond())
                               .build();

            String url = mainUrlPart + KICK_CHAT_MEMBER_URL;
            HttpHeaders headers = CommonHeadersBuilder.newInstance()
                                                      .json()
                                                      .build();
            log.debug("kickUserFromChat => message={}", () -> sendMessage);

            Object answerResponse =
                    restClient.post(url, headers.toSingleValueMap(), sendMessage, Object.class);
            Validate.notNull(answerResponse, "Empty message!");

            log.debug("kickUserFromChat ==> answer on message: {}", () -> answerResponse);
        } catch (Exception e) {
            log.warn("", e);
        }
    }

    /**
     * To unban user in chat.
     *
     * @param chatId chat ID.
     * @param userId user ID.
     *
     * @since 1.7.0.0
     */
    protected void unbanUserInChat(String chatId,
                                   String userId) {
        try {
            SendMessage sendMessage =
                    SendMessage.builder()
                               .chatId(chatId)
                               .userId(userId)
                               .build();

            String url = mainUrlPart + UNBAN_CHAT_MEMBER_URL;
            HttpHeaders headers = CommonHeadersBuilder.newInstance()
                                                      .json()
                                                      .build();
            log.debug("unbanUserInChat => : message={}", () -> sendMessage);

            Object answerResponse =
                    restClient.post(url, headers.toSingleValueMap(), sendMessage, Object.class);
            Validate.notNull(answerResponse, "Empty message!");

            log.debug("unbanUserInChat ==> answer on message: {}", () -> answerResponse);
        } catch (Exception e) {
            log.warn("", e);
        }
    }

    /**
     * To delete message from the chat.
     *
     * @param chatId    chat ID.
     * @param messageId message ID.
     *
     * @since 1.7.0.0
     */
    protected void removeMessageFromChat(String chatId,
                                         String messageId) {
        try {
            SendMessage sendMessage = SendMessage.builder()
                                                 .chatId(chatId)
                                                 .messageId(messageId)
                                                 .build();

            String url = mainUrlPart + DELETE_MESSAGE_URL;
            HttpHeaders headers = CommonHeadersBuilder.newInstance()
                                                      .json()
                                                      .build();
            log.debug("removeMessageFromChat => message={}", () -> sendMessage);

            Object answerResponse =
                    restClient.post(url, headers.toSingleValueMap(), sendMessage, Object.class);
            Validate.notNull(answerResponse, "Empty message!");

            log.debug("removeMessageFromChat ==> answer on message: {}", () -> answerResponse);
        } catch (Exception e) {
            log.warn("", e);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.4.1.0
     */
    private Locale defineLocale(User user) {
        String languageCode = user.getLanguageCode();

        return StringUtils.isNotBlank(languageCode) ? new Locale(languageCode) : META.DEFAULT_LOCALE;
    }

    /**
     * @since 1.4.0.0
     */
    private void leaveChat(String chatId) {
        SendMessage.SendMessageBuilder builder = SendMessage.builder()
                                                            .chatId(chatId);

        String url = mainUrlPart + LEAVE_CHAT_URL;
        HttpHeaders headers = CommonHeadersBuilder.newInstance()
                                                  .json()
                                                  .build();

        AnswerResponse<Boolean> answerResponse =
                restClient.post(url, headers.toSingleValueMap(), builder.build(),
                                new ParameterizedTypeReference<AnswerResponse<Boolean>>() {});
        Validate.notNull(answerResponse, "Empty message!");
        Validate.isTrue(answerResponse.getResult(), "An error in process of chat leaving! %s", answerResponse);
    }

    /**
     * @since 1.0.0.0
     */
    private SendMessageData createSendMessageData(Update update,
                                                  boolean userResponse) {
        Message mess = update.getMessage();
        if (mess == null) {
            mess = update.getCallbackQuery().getMessage();
        }

        Long messageId = mess.getMessageId();
        Long chatId;
        if (userResponse) {
            chatId = mess.getUser()
                         .getId();
        } else {
            chatId = mess.getChat()
                         .getChatId();
        }

        return new SendMessageData(chatId, messageId);
    }

    /**
     * @since 0.1.3.0
     */
    private void doSendMessage(Long chatId,
                               Long messageId,
                               MessageData message) {
        SendMessage sendMessage = SendMessage.builder()
                                             .chatId(chatId.toString())
                                             .text(message.getMessage())
                                             .replyMarkup(message.getReplyMarkup())
                                             .parseMode("HTML")
                                             .disableWebPagePreview(!message.isShowUrlPreview())
                                             .disableNotification(false)
                                             .build();
        if (message.isToResponse()) {
            sendMessage
                    .setReplyToMessageId(messageId);
        }

        String url = mainUrlPart + SEND_MESSAGE_URL;
        HttpHeaders headers = CommonHeadersBuilder.newInstance()
                                                  .json()
                                                  .build();

        log.debug("doSendMessage => message={}", () -> sendMessage);

        AnswerResponse<Message> answerResponse =
                restClient.post(url, headers.toSingleValueMap(), sendMessage,
                                new ParameterizedTypeReference<AnswerResponse<Message>>() {});
        Validate.notNull(answerResponse, "Empty message!");
        Validate.isTrue(answerResponse.getResult(), "An error in process of message sending! %s", answerResponse);

        if (message.getDoIfAntiBot() != null) {
            message.getDoIfAntiBot().accept(answerResponse.getData().getMessageId());
        }

        log.debug("doSendMessage => answer on message: {}", () -> answerResponse);
    }

    // </editor-fold>
}
