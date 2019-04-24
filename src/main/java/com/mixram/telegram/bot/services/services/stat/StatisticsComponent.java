package com.mixram.telegram.bot.services.services.stat;

import com.google.common.collect.ImmutableMap;
import com.mixram.telegram.bot.config.cache.RedisTemplateHelper;
import com.mixram.telegram.bot.services.domain.entity.Chat;
import com.mixram.telegram.bot.services.domain.entity.Message;
import com.mixram.telegram.bot.services.domain.entity.Update;
import com.mixram.telegram.bot.services.services.stat.entity.StatData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author mixram on 2019-04-20.
 * @since 1.3.0.0
 */
@Log4j2
@Component
public class StatisticsComponent {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String STATISTICS_KEY_NAME = "user_statistics";

    private final RedisTemplateHelper redisTemplateHelper;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public StatisticsComponent(RedisTemplateHelper redisTemplateHelper) {
        this.redisTemplateHelper = redisTemplateHelper;
    }


    // </editor-fold>


    public void recordUpdate(Update update) {

        //TODO: statistics development was set on pause

        try {
            if (update == null) {
                return;
            }
            Message message = update.getMessage();
            if (message == null) {
                return;
            }
            Chat chat = message.getChat();
            if (chat == null) {
                return;
            }

            StatData stat = redisTemplateHelper.getStatisticsFromRedis(STATISTICS_KEY_NAME);
            if (stat == null) {
                redisTemplateHelper.storeStatisticsToRedis(createNew(chat), STATISTICS_KEY_NAME);
            } else {
                Map<Long, StatData.StatDataInner> data = stat.getData();
                if (data.containsKey(chat.getChatId())) {
                    StatData.StatDataInner dataInner = data.get(chat.getChatId());
                    dataInner.setCounter(dataInner.getCounter() + 1);

                    //
                }
            }
        } catch (Exception e) {
            log.warn("Error in statistics recording!", e);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.3.0.0
     */
    private StatData createNew(Chat chat) {
        Long chatId = chat.getChatId();

        return StatData.builder()
                       .data(ImmutableMap.of(chatId,
                                             new StatData.StatDataInner(chatId, chat.getUserName(), 1, LocalDateTime.now())))
                       .updated(LocalDateTime.now())
                       .build();
    }

    // </editor-fold>
}
