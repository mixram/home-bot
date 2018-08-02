package com.mixram.telegram.bot.utils.databinding.support.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author mixram on 2017-07-17
 * @since 0.1.0.0
 */
public class LTDeserializer extends JsonDeserializer<LocalTime> {

    private final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HHmmss");

    public LTDeserializer() {
    }

    public LocalTime deserialize(JsonParser jp,
                                 DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
            String textToParse = jp.getText();
            if (StringUtils.isBlank(textToParse)) {
                return null;
            }

            textToParse = textToParse.trim();
            if (textToParse.contains(":")) {
                return LocalTime.parse(textToParse, formatter1);
            } else {
                return LocalTime.parse(textToParse, formatter2);
            }
        } else {
            throw ctxt.mappingException(LocalTime.class);
        }
    }
}
