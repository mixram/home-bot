package com.mixram.telegram.bot.services.services.tapicom;

import com.mixram.telegram.bot.services.domain.entity.Update;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mixram on 2019-04-22.
 * @since 1.3.0.0
 */
@Component
public class TelegramAPICommunicationComponent {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final TelegramAPICommunicationServices services;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public TelegramAPICommunicationComponent(TelegramAPICommunicationServices services) {
        this.services = services;
    }

    // </editor-fold>


    /**
     * @see TelegramAPICommunicationServices#sendMessage(Update, String)
     * @since 1.3.0.0
     */
    public void sendMessage(Update update,
                            String message) {
        Validate.notNull(update, "Update is not specified!");
        Validate.notBlank(message, "Message is not specified!");

        services.sendMessage(update, message);
    }

    /**
     * @see TelegramAPICommunicationServices#sendMessageToAdmin(String)
     * @since 1.3.0.0
     */
    public void sendMessageToAdmin(String message) {
        Validate.notBlank(message, "Message is not specified!");

        services.sendMessageToAdmin(message);
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
     * @see TelegramAPICommunicationServices#getAdminName()
     * @since 1.3.0.0
     */
    public String getAdminName() {
        return services.getAdminName();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
