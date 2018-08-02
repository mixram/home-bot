package com.mixram.telegram.bot.utils.databinding.support;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.mixram.telegram.bot.utils.databinding.support.types.LDDeserializer;

import java.time.LocalDate;

/**
 * @author mixram on 2017-07-17
 * @since 0.1.0.0
 */
public class CustomXmlModule extends JacksonXmlModule {

    public CustomXmlModule() {
        super.setDefaultUseWrapper(false);
        super.addDeserializer(LocalDate.class, new LDDeserializer());
    }
}
