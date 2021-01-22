package com.mixram.telegram.bot.services.services.antibot;

import com.mixram.telegram.bot.services.domain.entity.CASData;
import com.mixram.telegram.bot.services.domain.entity.CallbackQuery;
import com.mixram.telegram.bot.services.domain.entity.User;
import com.mixram.telegram.bot.services.services.bot.entity.MessageData;

import java.util.List;
import java.util.Locale;

/**
 * @author mixram on 2020-01-19.
 * @since 1.7.0.0
 */
public interface AntiBot {

    /**
     * To check if users are not bots.
     *
     * @param newChatMembers      a list with new users.
     * @param chatId              chat ID.
     * @param userIncomeMessageId message ID.
     * @param locale              locale.
     *
     * @return created message to respond.
     *
     * @since 1.7.0.0
     */
    MessageData checkUser(List<User> newChatMembers,
                          Long chatId,
                          Long userIncomeMessageId,
                          Locale locale);

    /**
     * To check users confirmation been not a bot.
     *
     * @since 1.7.0.0
     */
    void checkUsers();

    /**
     * To proceed the callback from chat.
     *
     * @param callbackQuery callback.
     *
     * @return message.
     *
     * @since 1.7.0.0
     */
    void proceedCallBack(CallbackQuery callbackQuery);

    /**
     * To get data from <a href="https://cas.chat">CAS service</a>.
     *
     * @param id user ID.
     *
     * @return CAS data.
     *
     * @since 1.8.5.0
     */
    CASData checkCAS(Long id);
}
