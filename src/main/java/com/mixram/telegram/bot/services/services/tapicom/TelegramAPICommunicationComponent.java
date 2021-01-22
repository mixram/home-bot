package com.mixram.telegram.bot.services.services.tapicom;

import com.mixram.telegram.bot.services.domain.entity.CASData;
import com.mixram.telegram.bot.services.domain.entity.Update;
import com.mixram.telegram.bot.services.services.bot.entity.MessageData;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * @author mixram on 2019-04-22.
 * @since 1.3.0.0
 */
@Component
public class TelegramAPICommunicationComponent {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    public static final String SOMETHING_WRONG_MESSAGE = "telegram.bot.message.something-wrong";

    private final TelegramAPICommunicationServices services;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public TelegramAPICommunicationComponent(TelegramAPICommunicationServices services) {
        this.services = services;
    }

    // </editor-fold>


    /**
     * @see TelegramAPICommunicationServices#sendMessageToChat(Long, MessageData)
     * @since 1.3.0.0
     */
    public void sendMessage(Update update,
                            MessageData messageData) {
        Validate.notNull(update, "Update is not specified!");
        Validate.notNull(messageData, "Message data is not specified!");
        Validate.notBlank(messageData.getMessage(), "Message is not specified!");

        services.sendMessage(update, messageData);
    }

    /**
     * @see TelegramAPICommunicationServices#sendMessageToChat(Long, MessageData)
     * @since 1.3.0.0
     */
    public void sendMessageToChat(Long chatId,
                                  MessageData messageData) {
        Validate.notNull(chatId, "Chat ID is not specified!");
        Validate.notNull(messageData, "Message data is not specified!");

        services.sendMessageToChat(chatId, messageData);
    }

    /**
     * @see TelegramAPICommunicationServices#sendMessageToAdmin(MessageData)
     * @since 1.3.0.0
     */
    public void sendMessageToAdmin(MessageData messageData) {
        Validate.notNull(messageData, "Message data is not specified!");
        Validate.notBlank(messageData.getMessage(), "Message is not specified!");

        services.sendMessageToAdmin(messageData);
    }

    /**
     * @see TelegramAPICommunicationServices#getUpdates()
     * @since 1.3.0.0
     */
    public List<Update> getUpdates() {
        return services.getUpdates();
    }

    /**
     * @see TelegramAPICommunicationServices#assertBot()
     * @since 1.3.0.0
     */
    public void assertBot() {
        services.assertBot();
    }

    /**
     * @see TelegramAPICommunicationServices#getAdmins()
     * @since 1.3.0.0
     */
    public Set<Long> getAdmins() {
        return services.getAdmins();
    }

    /**
     * @see TelegramAPICommunicationServices#kickUserFromChat(String, String)
     * @since 1.7.0.0
     */
    public void kickUserFromGroup(String chatId,
                                  String userId) {
        Validate.notBlank(chatId, "Chat ID is not specified!");
        Validate.notBlank(userId, "User ID is not specified!");

        services.kickUserFromChat(chatId, userId);
    }

    /**
     * @see TelegramAPICommunicationServices#removeMessageFromChat(String, String)
     * @since 1.7.0.0
     */
    public void removeMessageFromChat(String chatId,
                                      String messageId) {
        Validate.notBlank(chatId, "Chat ID is not specified!");
        Validate.notBlank(messageId, "Message ID is not specified!");

        services.removeMessageFromChat(chatId, messageId);
    }

    /**
     * @see TelegramAPICommunicationServices#unbanUserInChat(String, String)
     * @since 1.7.0.0
     */
    public void unbanUserInChat(String chatId,
                                String userId) {
        Validate.notBlank(chatId, "Chat ID is not specified!");
        Validate.notBlank(userId, "User ID is not specified!");

        services.unbanUserInChat(chatId, userId);
    }

    /**
     * @see TelegramAPICommunicationServices#manageRightsChatMember(String, String, boolean)
     * @since 1.8.5.0
     */
    public void manageRightsChatMember(@Nonnull String chatId,
                                       @Nonnull String userId,
                                       boolean restrict) {
        Validate.notBlank(chatId, "Chat ID is not specified!");
        Validate.notBlank(userId, "User ID is not specified!");

        services.manageRightsChatMember(chatId, userId, restrict);
    }

    /**
     * @see TelegramAPICommunicationServices#checkCAS(Long)
     * @since 1.8.5.0
     */
    @Nonnull
    public CASData checkCAS(@Nonnull Long id) {
        Validate.notNull(id, "User ID is not specified!");

        return services.checkCAS(id);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
