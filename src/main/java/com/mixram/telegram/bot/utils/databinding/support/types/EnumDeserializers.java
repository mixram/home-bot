package com.mixram.telegram.bot.utils.databinding.support.types;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

import java.io.IOException;

/**
 * @author mixram on 2017-07-17
 * @since 0.1.0.0
 */
public class EnumDeserializers extends BeanDeserializerModifier {

    /*Took here: http://stackoverflow.com/questions/24157817/jackson-databind-enum-case-insensitive#answer-24173645*/

    @Override
    public JsonDeserializer<Enum> modifyEnumDeserializer(DeserializationConfig config,
                                                         JavaType type,
                                                         BeanDescription beanDesc,
                                                         JsonDeserializer<?> deserializer) {
        return new JsonDeserializer<Enum>() {
            @Override
            public Enum deserialize(JsonParser p,
                                    DeserializationContext ctxt) throws IOException {
                Class<? extends Enum> rawClass = (Class<Enum<?>>) type.getRawClass();

                return Enum.valueOf(rawClass, p.getValueAsString().trim().toUpperCase());
            }
        };
    }
}
