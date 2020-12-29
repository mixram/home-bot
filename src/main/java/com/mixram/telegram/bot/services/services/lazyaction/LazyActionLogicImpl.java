package com.mixram.telegram.bot.services.services.lazyaction;

import com.google.common.collect.Lists;
import com.mixram.telegram.bot.config.cache.RedisTemplateHelper;
import com.mixram.telegram.bot.services.services.bot.entity.LazyActionData;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author mixram on 2020-07-20.
 * @since 1.8.2.0
 */
@Log4j2
@Component
public class LazyActionLogicImpl implements LazyActionLogic {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String LAZY_ACTION_DATA = "lazy_action_data";
    public static final Comparator<LazyActionData> LAZY_ACTION_COMPARATOR =
            Comparator.comparing(LazyActionData :: getActionDateTime);

    private final RedisTemplateHelper redisTemplateHelper;
    private final TelegramAPICommunicationComponent communicationComponent;


    @Data
    @AllArgsConstructor
    private class LazyActionSaver implements Consumer<Long> {

        private LazyActionData lazyActionData;

        @Override
        public void accept(Long messageId) {
            if (lazyActionData.getMessageId() == null) {
                lazyActionData.setMessageId(messageId);
            }

            saveLazyActionToRedis(lazyActionData);
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public LazyActionLogicImpl(RedisTemplateHelper redisTemplateHelper,
                               TelegramAPICommunicationComponent communicationComponent) {
        this.redisTemplateHelper = redisTemplateHelper;
        this.communicationComponent = communicationComponent;
    }

    // </editor-fold>


    @Override
    public void doLazyAction() {
        List<LazyActionData> lazyActionDataList = getLazyActionListFromRedis();
        if (lazyActionDataList.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        List<LazyActionData> forActionProceed = Lists.newArrayList();
        List<LazyActionData> forActionSaveToRedis = Lists.newArrayList();
        for (LazyActionData lazyActionData : lazyActionDataList) {
            if (now.isBefore(lazyActionData.getActionDateTime())) forActionSaveToRedis.add(lazyActionData);
            else forActionProceed.add(lazyActionData);
        }

        if (forActionProceed.isEmpty()) {
            return;
        }

        saveLazyActionToRedis(forActionSaveToRedis);

        forActionProceed.forEach(this :: doLazyAction);
    }

    @Override
    public Consumer<Long> createSaver(LazyActionData lazyActionData) {
        return new LazyActionSaver(lazyActionData);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.8.2.0
     */
    private void doLazyAction(LazyActionData lazyActionData) {
        try {
            switch (lazyActionData.getAction()) {
                case DELETE:
                    communicationComponent.removeMessageFromChat(lazyActionData.getChatId().toString(),
                                                                 lazyActionData.getMessageId().toString());
                    break;
                default:
                    throw new UnsupportedOperationException(
                            String.format("Unexpected action type '%s'!", lazyActionData.getAction()));
            }
        } catch (Exception e) {
            log.warn("Unexpected error lazy-action doing!", e);
        }
    }

    /**
     * @since 1.8.2.0
     */
    private void saveLazyActionToRedis(LazyActionData lazyActionData) {
        lazyActionData.checkValid();

        List<LazyActionData> lazyActionDataList = getLazyActionListFromRedis();

        lazyActionDataList.add(lazyActionData);
        lazyActionDataList.sort(LAZY_ACTION_COMPARATOR);

        saveLazyActionToRedis(lazyActionDataList);
    }

    private void saveLazyActionToRedis(List<LazyActionData> lazyActionDataList) {
        redisTemplateHelper.storeLazyActionDataToRedis(lazyActionDataList, LAZY_ACTION_DATA);
    }

    /**
     * @since 1.8.2.0
     */
    private List<LazyActionData> getLazyActionListFromRedis() {
        List<LazyActionData> lazyActionDataList = redisTemplateHelper.getLazyActionDataFromRedis(LAZY_ACTION_DATA);

        return lazyActionDataList == null ? Lists.newArrayList() : lazyActionDataList;
    }

    // </editor-fold>
}
