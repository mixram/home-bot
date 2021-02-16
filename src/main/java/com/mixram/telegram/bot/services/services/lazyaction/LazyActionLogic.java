package com.mixram.telegram.bot.services.services.lazyaction;

import com.mixram.telegram.bot.services.services.bot.entity.LazyActionData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author mixram on 2020-07-20.
 * @since 1.8.2.0
 */
public interface LazyActionLogic {

    void doLazyAction();

    Consumer<Long> createSaver(@Nonnull LazyActionData lazyActionData);

    void saveLazyActionToRedis(@Nonnull LazyActionData lazyActionData);

    void saveLazyActionsToRedis(@Nonnull List<LazyActionData> lazyActionData);

    void removeLazyActionFromRedis(@Nonnull LazyActionData lazyActionData);

    void removeLazyActionsFromRedis(@Nonnull List<LazyActionData> lazyActionData);

}
