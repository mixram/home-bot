package com.mixram.telegram.bot.utils.databinding.support.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author mixram on 2017-07-17
 * @since 0.1.0.0
 */
public class LDDeserializer extends JsonDeserializer<LocalDate> {

    private final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("yyMMdd");

    public LDDeserializer() {
    }

    public LocalDate deserialize(JsonParser jp,
                                 DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
            String textToParse = jp.getText();
            if (StringUtils.isBlank(textToParse)) {
                return null;
            }
            textToParse = textToParse.trim();
            if (textToParse.contains("-")) {
                return LocalDate.parse(textToParse, formatter1);
            } else if (textToParse.contains(".")) {
                return LocalDate.parse(textToParse, formatter3);
            }
            if (textToParse.length() == 6) {
                return LocalDate.parse(textToParse, formatter4);
            } else {
                return LocalDate.parse(textToParse, formatter2);
            }
        } else {
            throw ctxt.mappingException(LocalDate.class);
        }
    }
}
