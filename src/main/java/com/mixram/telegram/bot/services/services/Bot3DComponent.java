package com.mixram.telegram.bot.services.services;

import com.mixram.telegram.bot.services.domain.entity.Update;
import com.mixram.telegram.bot.services.services.entity.MessageData;

/**
 * @author mixram on 2019-04-10.
 * @since ...
 */
public interface Bot3DComponent {

    MessageData proceedUpdate(Update update);
}
