package com.mixram.telegram.bot.services.services.market;

import com.mixram.telegram.bot.services.domain.entity.Message;

import javax.annotation.Nonnull;

/**
 * @author mixram on 2021-02-16.
 * @since 1.8.8.0
 */
public interface MarketLogic {

    void doPostponedAction();

    void saveMessageToRedisForPostponedLazyAction(@Nonnull Message message);

    boolean isAdvertisement(@Nonnull Message message);
}
