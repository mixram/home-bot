package com.mixram.telegram.bot.services.reminders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mixram.telegram.bot.services.services.bot.Bot3DComponentImpl;
import com.mixram.telegram.bot.services.services.bot.entity.MessageData;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
import com.mixram.telegram.bot.utils.CustomMessageSource;
import com.mixram.telegram.bot.utils.META;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author mixram on 2019-04-26.
 * @since 1.4.1.0
 */
@Component
public class DiscountsReminder implements Reminder {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String REMINDER_MESSAGE = "telegram.bot.message.reminder.discounts";

    private final Bot3DComponentImpl bot3DComponentImpl;
    private final TelegramAPICommunicationComponent communicationComponent;
    private final CustomMessageSource messageSource;

    private final Set<String> chatsIds;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsReminder(@Value("${bot.settings.work-with-groups}") String chatsIds,
                             Bot3DComponentImpl bot3DComponentImpl,
                             TelegramAPICommunicationComponent communicationComponent,
                             CustomMessageSource messageSource) {
        this.bot3DComponentImpl = bot3DComponentImpl;
        this.communicationComponent = communicationComponent;
        this.messageSource = messageSource;

        this.chatsIds = JsonUtil.fromJson(chatsIds, new TypeReference<Set<String>>() {});
    }

    // </editor-fold>


    @Override
    public void remind() {
        String messagePart = bot3DComponentImpl.prepareMessageForShopsToSendString(false, true, META.DEFAULT_LOCALE);
        String message = messageSource.getMessage(REMINDER_MESSAGE, META.DEFAULT_LOCALE, messagePart);

        MessageData data = MessageData.builder()
                                      .message(message)
                                      .build();

        chatsIds.forEach(c -> communicationComponent.sendMessageToChat(Long.valueOf(c), data));
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
