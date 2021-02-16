package com.mixram.telegram.bot.services.services.market;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mixram.telegram.bot.config.cache.RedisTemplateHelper;
import com.mixram.telegram.bot.services.domain.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * @author mixram on 2021-02-16.
 * @since 1.8.8.0
 */
@Component
public class MarketLogicImpl implements MarketLogic {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final Object LOCK = new Object();

    private static final String POSTPONED_MESSAGES_DATA = "postponed_messages_data";

    private final RedisTemplateHelper redisTemplateHelper;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public MarketLogicImpl(RedisTemplateHelper redisTemplateHelper) {
        this.redisTemplateHelper = redisTemplateHelper;
    }

    // </editor-fold>


    /**
     * To do stored lazy action.
     *
     * @since 1.8.8.0
     */
    @Override
    public void doPostponedAction() {
        synchronized (LOCK) {
            final Map<String, List<Message>> messages = getPostponedMessagesListFromRedis();
            if (messages.isEmpty()) return;

            messages.forEach((mediaGroupId, messagesList) -> {
                final boolean isAdv = messagesList.stream()
                                                  .anyMatch(this :: isAdvertisement);
                if (isAdv) {
                    //TODO: тут нужно сделать логику как в doMarketLogic() с удалением ВСЕХ сообщений из lazyAction и форвардингом сообщения со всеми фотками в чат-архив.
                } else {
                    //TODO: тут нужно сделать логику как в doMarketLogic() с сохранением ВСЕХ сообщений в lazyAction для удаления.
                }

                //TODO: и вообще, логики тут и в doMarketLogic() должны быть где-то в одном месте: думаю, что надо всю логику переносить сюда, чтобы она просто вызывалась из doMarketLogic(), а не реализовывалась там.
            });

            savePostponedMessagesDataToRedis(Maps.newHashMap());
        }
    }


    /**
     * To save message to Redis for postponed lazy action.<br> The message will be stored in Redis and processed some later with scheduler.
     * The result of scheduler work will be the message stored in Redis in lazy-actions-collection with
     *
     * @since 1.8.8.0
     */
    @Override
    public void saveMessageToRedisForPostponedLazyAction(@Nonnull Message message) {
        synchronized (LOCK) {
            final Map<String, List<Message>> messages = getPostponedMessagesListFromRedis();

            if (messages.containsKey(message.getMediaGroupId())) {
                messages.get(message.getMediaGroupId()).add(message);
            } else {
                messages.put(message.getMediaGroupId(), Lists.newArrayList(message));
            }

            savePostponedMessagesDataToRedis(messages);
        }
    }


    /**
     * To check if text contains signs, that message is an advertisement.
     *
     * @param message message.
     *
     * @return true - message is an advertisement, false - otherwise.
     *
     * @since 1.8.8.0
     */
    @Override
    public boolean isAdvertisement(@Nonnull Message message) {
        final String text = message.getText() == null ? message.getCaption() : message.getText();

        return StringUtils.isNotBlank(text) && (
                text.startsWith("#продам")
                        || text.startsWith("#куплю")
                        || text.startsWith("#бронь")
                        || text.startsWith("#услуга")
                        || text.startsWith("#продано")
                        || text.startsWith("#послуга")
                        || text.startsWith("#объявление")
        );
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.8.8.0
     */
    private void savePostponedMessagesDataToRedis(@Nonnull Map<String, List<Message>> postponedMessagesMap) {
        redisTemplateHelper.storePostponedMessagesDataToRedis(postponedMessagesMap, POSTPONED_MESSAGES_DATA);
    }

    /**
     * @since 1.8.8.0
     */
    @Nonnull
    private Map<String, List<Message>> getPostponedMessagesListFromRedis() {
        Map<String, List<Message>> lazyActionDataList = redisTemplateHelper.getPostponedMessagesDataFromRedis(POSTPONED_MESSAGES_DATA);

        return lazyActionDataList == null ? Maps.newHashMap() : lazyActionDataList;
    }

    // </editor-fold>
}
