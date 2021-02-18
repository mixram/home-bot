package com.mixram.telegram.bot.services.domain;

import javax.annotation.Nullable;

/**
 * @author mixram on 2021-02-17.
 * @since 1.8.8.0
 */
public interface InputMedia extends TelegramApiEntity {

    @Nullable
    String getCaption();
}
