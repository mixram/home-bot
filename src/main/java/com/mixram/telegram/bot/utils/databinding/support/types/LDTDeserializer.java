package com.mixram.telegram.bot.utils.databinding.support.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author mixram on 2017-07-17
 * @since 0.1.0.0
 */
public class LDTDeserializer extends JsonDeserializer<LocalDateTime> {

    private final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public LDTDeserializer() {
    }

    public LocalDateTime deserialize(JsonParser jp,
                                     DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
            String textToParse = jp.getText();
            if (StringUtils.isBlank(textToParse)) {
                return null;
            }

            textToParse = textToParse.trim();
            if (textToParse.contains("T")) {
                return LocalDateTime.parse(textToParse);
            } else if (textToParse.contains("-")) {
                return LocalDateTime.parse(textToParse, formatter1);
            } else if (textToParse.contains(".")) {
                return LocalDateTime.parse(textToParse, formatter3);
            }

            return LocalDateTime.parse(textToParse, formatter2);
        } else {
            throw ctxt.mappingException(LocalDateTime.class);
        }
    }
}
