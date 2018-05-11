package com.mixram.telegram.bot.utils.databinding.support;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author mixram on 2018-04-25.
 * @since 0.1.0.0
 */
public class CustomJsonMapper extends ObjectMapper {

    public CustomJsonMapper() {
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
        registerModule(new JavaTimeModule());

        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
