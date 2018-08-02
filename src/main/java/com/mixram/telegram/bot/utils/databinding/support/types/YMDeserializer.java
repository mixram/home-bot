package com.mixram.telegram.bot.utils.databinding.support.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * @author mixram on 2017-07-17
 * @since 0.1.0.0
 */
public class YMDeserializer extends JsonDeserializer<YearMonth> {

    private final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM");
    private final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMM");

    public YMDeserializer() {
    }

    public YearMonth deserialize(JsonParser jp,
                                 DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
            String textToParse = jp.getText();
            if (StringUtils.isBlank(textToParse)) {
                return null;
            }

            textToParse = textToParse.trim();
            if (textToParse.contains("-")) {
                return YearMonth.parse(textToParse, formatter1);
            } else {
                return YearMonth.parse(textToParse, formatter2);
            }
        } else {
            throw ctxt.mappingException(YearMonth.class);
        }
    }
}
