package com.mixram.telegram.bot.utils.databinding.support;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mixram.telegram.bot.utils.databinding.support.types.EnumDeserializers;

/**
 * @author mixram on 2017-07-17
 * @since 0.1.0.0
 */
public class CustomJsonModule extends SimpleModule {

    public CustomJsonModule() {
        super("CustomJsonModule");
        this.setDeserializerModifier(new EnumDeserializers());
    }
}
