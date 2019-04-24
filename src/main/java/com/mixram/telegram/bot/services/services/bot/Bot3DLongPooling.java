package com.mixram.telegram.bot.services.services.bot;

import com.mixram.telegram.bot.services.domain.LongPooling;
import com.mixram.telegram.bot.services.domain.entity.Update;
import com.mixram.telegram.bot.services.services.bot.entity.MessageData;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
import com.mixram.telegram.bot.utils.ConcurrentUtilites;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@Log4j2
@Component
public class Bot3DLongPooling implements LongPooling {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final Bot3DComponent bot3DComponent;
    private final TelegramAPICommunicationComponent communicationComponent;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public Bot3DLongPooling(Bot3DComponent bot3DComponent,
                            TelegramAPICommunicationComponent communicationComponent) {
        this.bot3DComponent = bot3DComponent;
        this.communicationComponent = communicationComponent;
    }

    @PostConstruct
    public void init() {
        communicationComponent.assertBot();
    }

    // </editor-fold>


    @Override
    public void check() {
        log.trace("{} is started!", Bot3DLongPooling.class :: getSimpleName);

        List<Update> updates = communicationComponent.getUpdates();

        Map<Update, CompletableFuture<MessageData>> answers = new HashMap<>(updates.size());
        updates.forEach(u -> answers.put(u, ConcurrentUtilites.supplyAsyncWithLocalThreadContext(
                aVoid -> bot3DComponent.proceedUpdate(u))));
        answers.forEach((k, v) -> {
            MessageData join = v.join();
            if (join == null) {
                log.debug("No need to answer on the question! See logs or underlying code for details.");

                return;
            }
            if (join.isToAdmin()) {
                communicationComponent.sendMessageToAdmin(join);
            } else {
                communicationComponent.sendMessage(k, join);
            }
        });

    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
