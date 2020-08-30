package com.mixram.telegram.bot.services.services.antibot;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mixram.telegram.bot.config.cache.RedisTemplateHelper;
import com.mixram.telegram.bot.services.domain.entity.CallbackQuery;
import com.mixram.telegram.bot.services.domain.entity.InlineKeyboard;
import com.mixram.telegram.bot.services.domain.entity.User;
import com.mixram.telegram.bot.services.services.bot.entity.MessageData;
import com.mixram.telegram.bot.services.services.bot.entity.NewMemberTempData;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
import com.mixram.telegram.bot.utils.META;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author mixram on 2020-01-19.
 * @since 1.7.0.0
 */
@Log4j2
@Component
public class AntiBotImpl implements AntiBot {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    public static final Random RANDOM = new Random();
    public static final Map<Integer, Pair<String, String>> ANTI_BOT_QUESTIONS;
    public static final int MIN;
    public static final int MAX;

    private static final String NEW_MEMBERS_TEMP_DATA = "new_members_temp_data";
    private static final String ID_SEPARATOR = "_";

    private final RedisTemplateHelper redisTemplateHelper;
    private final TelegramAPICommunicationComponent telegramAPICommunicationComponent;

    static {
        Map<Integer, Pair<String, String>> tempMap = new HashMap<>(6);
        tempMap.put(0, Pair.of("zero_no_bot", "Нуль"));
        tempMap.put(1, Pair.of("one_no_bot", "Один"));
        tempMap.put(2, Pair.of("two_no_bot", "Два"));
        tempMap.put(3, Pair.of("three_no_bot", "Три"));
        tempMap.put(4, Pair.of("four_no_bot", "Чотири"));
        tempMap.put(5, Pair.of("five_no_bot", "П'ять"));

        ANTI_BOT_QUESTIONS = ImmutableMap.copyOf(tempMap);
        MAX = ANTI_BOT_QUESTIONS.size() - 1;
        MIN = 1;
    }


    @Data
    @AllArgsConstructor
    private class NewMessageAdder implements Consumer<Long> {

        private Long userId;
        private Long chatId;

        @Override
        public void accept(Long aLong) {
            addMessageToExistingDeleteList(userId, chatId, aLong);
        }

    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public AntiBotImpl(RedisTemplateHelper redisTemplateHelper,
                       TelegramAPICommunicationComponent telegramAPICommunicationComponent) {
        this.redisTemplateHelper = redisTemplateHelper;
        this.telegramAPICommunicationComponent = telegramAPICommunicationComponent;
    }

    // </editor-fold>


    @Override
    public MessageData checkUser(List<User> newChatMembers,
                                 Long chatId,
                                 Long userIncomeMessageId,
                                 Locale locale) {
        Validate.notEmpty(newChatMembers, "A list of members is not specified or empty!");
        Validate.notNull(locale, "Locale is not specified!");

        //TODO: need to rebuild in order to be able to check more then one new user at the same time
        Validate.isTrue(newChatMembers.size() == 1, "Can not check more then one user at the same time!");

        Map<String, NewMemberTempData> membersData = getMembersTempDataFromRedis();
        List<MessageData> messages = Lists.newArrayListWithExpectedSize(newChatMembers.size());
        newChatMembers.forEach(u -> {
            int nextInt = getNextInt();
            NewMemberTempData newMember =
                    NewMemberTempData.builder()
                                     .user(u)
                                     .added(LocalDateTime.now())
                                     .messagesToDelete(Lists.newArrayList())
                                     .userIncomeMessageId(userIncomeMessageId)
                                     .rightAnswerNumber(nextInt)
                                     .build();
            membersData.put(prepareId(chatId, u.getId()), newMember);
            messages.add(MessageData.builder()
                                    .message(defineRandomMessageV2(u.getId(), u.getFirstName(), nextInt))
                                    .replyMarkup(defineStandardKeyboardV2())
                                    .doIfAntiBot(new NewMessageAdder(u.getId(), chatId))
                                    .build());
        });

        storeNewMembersTempDataToRedis(membersData);

        return messages.get(0);
    }

    @Override
    public void checkUsers() {
        LocalDateTime checkTime = LocalDateTime.now().minusMinutes(1);
        Map<String, NewMemberTempData> membersTempDataFromRedis = getMembersTempDataFromRedis();

        List<String> removeFromTemp = Lists.newArrayList();
        membersTempDataFromRedis.forEach((key, value) -> {
            LocalDateTime added = value.getAdded();
            if (added.isBefore(checkTime)) {
                String[] dataArray = key.split(ID_SEPARATOR);
                String chatId = dataArray[0];
                String userId = dataArray[1];

                doRemoveUserProcedures(chatId, userId, value);

                removeFromTemp.add(key);
            }
        });

        if (!CollectionUtils.isEmpty(removeFromTemp)) {
            removeFromTemp.forEach(membersTempDataFromRedis :: remove);
            storeNewMembersTempDataToRedis(membersTempDataFromRedis);
        }
    }

    @Override
    public void proceedCallBack(CallbackQuery callbackQuery) {
        doProceedCallbackV2(callbackQuery);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * To engage user removing procedures.
     *
     * @param chatId chat ID.
     * @param userId user ID.
     * @param data   data about a new member.
     *
     * @since 1.8.4.1
     */
    private void doRemoveUserProcedures(@Nonnull String chatId,
                                        @Nonnull String userId,
                                        @Nonnull NewMemberTempData data) {
        telegramAPICommunicationComponent.kickUserFromGroup(chatId, userId);
        log.info("User {} has been kicked from chat {}!",
                 () -> userId,
                 () -> chatId);

        telegramAPICommunicationComponent.unbanUserInChat(chatId, userId);
        log.info("User {} has been unbaned in chat {}!",
                 () -> userId,
                 () -> chatId);

        telegramAPICommunicationComponent.removeMessageFromChat(chatId,
                                                                String.valueOf(data.getUserIncomeMessageId()));
        data.getMessagesToDelete().forEach(
                m -> telegramAPICommunicationComponent.removeMessageFromChat(chatId, String.valueOf(m)));

        MessageData messageData = MessageData.builder()
                                             .toAdmin(true)
                                             .message(String.format(
                                                     "<b>User has been kicked from chat %s!</b>\n%s",
                                                     userId,
                                                     JsonUtil.toPrettyJson(data)))
                                             .build();
        telegramAPICommunicationComponent.sendMessageToAdmin(messageData);
    }

    /**
     * @since 1.8.3.0
     */
    private void doProceedCallbackV2(CallbackQuery callbackQuery) {
        Validate.notNull(callbackQuery, "Callback is not specified!");

        User user = callbackQuery.getUser();
        Validate.notNull(user, "User is not specified!");

        Long chatId = callbackQuery.getMessage().getChat().getChatId();
        String key = prepareId(chatId, user.getId());
        Map<String, NewMemberTempData> membersTempDataFromRedis = getMembersTempDataFromRedis();
        NewMemberTempData newMemberTempData = membersTempDataFromRedis.get(key);
        if (newMemberTempData == null) {
            throw new UnsupportedOperationException(String.format("User %s not found!", key));
        }

        int rightAnswerNumber = newMemberTempData.getRightAnswerNumber();
        String keyForAnswer = ANTI_BOT_QUESTIONS.getOrDefault(rightAnswerNumber, Pair.of(null, null)).getKey();
        if (!callbackQuery.getData().equalsIgnoreCase(keyForAnswer)) {
            doRemoveUserProcedures(chatId.toString(), user.getId().toString(), newMemberTempData);

            throw new IllegalArgumentException(
                    String.format("Wrong answer '%s' (expected '%s') on the antibot question! %s",
                                  callbackQuery.getData(), keyForAnswer, callbackQuery));
        }

        newMemberTempData.getMessagesToDelete().forEach(
                message -> telegramAPICommunicationComponent.removeMessageFromChat(String.valueOf(chatId),
                                                                                   String.valueOf(message)));

        membersTempDataFromRedis.remove(key);

        storeNewMembersTempDataToRedis(membersTempDataFromRedis);
    }

    /**
     * @since 1.7.0.0
     */
    private void doProceedCallback(CallbackQuery callbackQuery) {
        Validate.notNull(callbackQuery, "Callback is not specified!");
        Validate.isTrue(META.NOT_A_BOT_TEXT.equalsIgnoreCase(callbackQuery.getData()),
                        "The callback is not for AntiBot!");

        //TODO: need to rebuild logic when "not_a_bot_text" will not be the one

        User user = callbackQuery.getUser();
        Validate.notNull(user, "User is not specified!");

        Long chatId = callbackQuery.getMessage().getChat().getChatId();
        String key = prepareId(chatId, user.getId());
        Map<String, NewMemberTempData> membersTempDataFromRedis = getMembersTempDataFromRedis();
        NewMemberTempData newMemberTempData = membersTempDataFromRedis.get(key);
        if (newMemberTempData == null) {
            throw new UnsupportedOperationException(String.format("User %s not found!", key));
        }

        newMemberTempData.getMessagesToDelete().forEach(
                message -> telegramAPICommunicationComponent.removeMessageFromChat(String.valueOf(chatId),
                                                                                   String.valueOf(message)));

        membersTempDataFromRedis.remove(key);

        storeNewMembersTempDataToRedis(membersTempDataFromRedis);
    }

    /**
     * @since 1.8.3.0
     */
    private int getNextInt() {
        return RANDOM.nextInt(MAX - MIN + 1) + MIN;
    }

    /**
     * @since 1.7.0.0
     */
    private void addMessageToExistingDeleteList(Long userId,
                                                Long chatId,
                                                Long messageId) {
        String key = prepareId(chatId, userId);
        Map<String, NewMemberTempData> membersData = getMembersTempDataFromRedis();
        NewMemberTempData data = membersData.get(key);
        if (data == null) {
            log.warn("No data in 'MembersTempDataFromRedis' by key={}", () -> key);

            return;
        }

        data.getMessagesToDelete().add(messageId);

        storeNewMembersTempDataToRedis(membersData);
    }

    /**
     * @since 1.7.0.0
     */
    private void storeNewMembersTempDataToRedis(Map<String, NewMemberTempData> data) {
        redisTemplateHelper.storeNewMembersTempDataToRedis(data, NEW_MEMBERS_TEMP_DATA);
    }

    /**
     * @since 1.7.0.0
     */
    private Map<String, NewMemberTempData> getMembersTempDataFromRedis() {
        Map<String, NewMemberTempData> membersData = redisTemplateHelper.getMembersTempDataFromRedis(
                NEW_MEMBERS_TEMP_DATA);

        return membersData == null ? Maps.newHashMap() : membersData;
    }

    /**
     * @since 1.7.0.0
     * @deprecated use {@link AntiBotImpl#defineStandardKeyboard()} instead since 1.8.3.0.
     */
    @Deprecated
    private InlineKeyboard defineRandomKey() {
        List<List<InlineKeyboard.Key>> keyboard = new ArrayList<>(1);
        keyboard.add(Lists.newArrayList(new InlineKeyboard.Key(META.NOT_A_BOT_TEXT, "Я не бот 🤟")));

        return InlineKeyboard.builder()
                             .inlineKeyboard(keyboard)
                             .build();
    }

    /**
     * @since 1.8.3.0
     */
    private InlineKeyboard defineStandardKeyboard() {
        List<List<InlineKeyboard.Key>> keyboard = new ArrayList<>();
        keyboard.add(
                Lists.newArrayList(
                        new InlineKeyboard.Key(ANTI_BOT_QUESTIONS.get(0).getKey(), "0"),
                        new InlineKeyboard.Key(ANTI_BOT_QUESTIONS.get(1).getKey(), "1"),
                        new InlineKeyboard.Key(ANTI_BOT_QUESTIONS.get(2).getKey(), "2")
                )
        );
        keyboard.add(
                Lists.newArrayList(
                        new InlineKeyboard.Key(ANTI_BOT_QUESTIONS.get(3).getKey(), "3"),
                        new InlineKeyboard.Key(ANTI_BOT_QUESTIONS.get(4).getKey(), "4"),
                        new InlineKeyboard.Key(ANTI_BOT_QUESTIONS.get(5).getKey(), "5")
                )
        );

        return InlineKeyboard.builder()
                             .inlineKeyboard(keyboard)
                             .build();
    }

    /**
     * @since 1.8.3.0
     */
    private InlineKeyboard defineStandardKeyboardV2() {
        List<Integer> shuffledKeys = getShuffledKeys(ANTI_BOT_QUESTIONS);
        int sizeOfOneRow = getSizeOfOneRow(ANTI_BOT_QUESTIONS);
        List<InlineKeyboard.Key> firstRow = new ArrayList<>(sizeOfOneRow);
        List<InlineKeyboard.Key> secondRow = new ArrayList<>(sizeOfOneRow);

        firstRow.add(0, new InlineKeyboard.Key(ANTI_BOT_QUESTIONS.get(0).getKey(), "0"));
        for (Integer key : shuffledKeys) {
            if (0 == key) {
                continue;
            }
            if (firstRow.size() < sizeOfOneRow) {
                firstRow.add(new InlineKeyboard.Key(ANTI_BOT_QUESTIONS.get(key).getKey(), key.toString()));
                continue;
            }
            if (secondRow.size() < sizeOfOneRow) {
                secondRow.add(new InlineKeyboard.Key(ANTI_BOT_QUESTIONS.get(key).getKey(), key.toString()));
            }
        }

        return InlineKeyboard.builder()
                             .inlineKeyboard(Lists.newArrayList(firstRow, secondRow))
                             .build();
    }

    /**
     * @since 1.8.3.0
     */
    private int getSizeOfOneRow(Map<?, ?> map) {
        return map.size() / 2;
    }

    /**
     * @since 1.8.3.0
     */
    private List<Integer> getShuffledKeys(Map<Integer, Pair<String, String>> antiBotQuestions) {
        List<Integer> keys = new ArrayList<>(antiBotQuestions.keySet());
        Collections.shuffle(keys);

        return keys;
    }

    /**
     * @since 1.7.0.0
     * @deprecated use {@link AntiBotImpl#defineRandomMessageV2(Long, String, int)} instead since 1.8.3.0.
     */
    @Deprecated
    private String defineRandomMessage(User user) {
        return String.format("<a href=\"tg://user?id=%s\">%s</a>, якщо Ви людина - натисніть на кнопку 😊",
                             user.getId(),
                             user.getFirstName());
    }

    /**
     * @since 1.8.3.0
     */
    private String defineRandomMessageV2(Long userId,
                                         String userName,
                                         int questionNumber) {
        return String.format(
                "<a href=\"tg://user?id=%s\">%s</a>, якщо Ви людина - натисніть на кнопку з цифрою '%s' 😊",
                userId, userName, ANTI_BOT_QUESTIONS.get(questionNumber).getValue());
    }

    /**
     * @since 1.7.0.0
     */
    private List<Long> createDeleteList(List<User> newChatMembers,
                                        Long userIncomeMessageId) {
        return newChatMembers.size() == 1 ? Lists.newArrayList(userIncomeMessageId) : Lists.newArrayList();
    }

    /**
     * @since 1.7.0.0
     */
    private String prepareId(Long chatId,
                             Long id) {
        return chatId + ID_SEPARATOR + id;
    }

    // </editor-fold>

}
