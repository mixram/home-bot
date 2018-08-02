package com.mixram.telegram.bot.utils.databinding.support;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

/**
 * @author mixram on 2017-07-17
 * @since 0.1.0.0
 */
public class CustomXmlMapper extends XmlMapper {

    public CustomXmlMapper() {
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        registerModule(new CustomXmlModule());
        registerModule(new GuavaModule());
    }
}
