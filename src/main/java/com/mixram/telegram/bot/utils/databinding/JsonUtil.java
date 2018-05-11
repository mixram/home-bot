package com.mixram.telegram.bot.utils.databinding;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mixram.telegram.bot.utils.databinding.ex.JsonException;
import com.mixram.telegram.bot.utils.databinding.support.CustomJsonMapper;

import java.io.File;
import java.io.InputStream;

/**
 * Wrapper for easy work with json.
 *
 * @author mixram on 2018-04-25.
 * @since 0.1.0.0
 */
public final class JsonUtil {

    private JsonUtil() {
    }

    //TODO: to create a descriptions for API.

    /**
     * @since 0.1.0.0
     */
    public static String toPrettyJson(Object obj) {
        try {
            return OBJECT_WRITER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * @since 0.1.0.0
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * @since 0.1.0.0
     */
    public static byte[] writeValueAsBytes(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * @since 0.1.0.0
     */
    public static <T> T fromJson(String json,
                                 TypeReference<T> typeReference) throws JsonException {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * @since 0.1.0.0
     */
    public static <T> T fromJson(String json,
                                 Class<T> clazz) throws JsonException {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * @since 0.1.0.0
     */
    public static <T> T fromJson(File file,
                                 TypeReference<T> typeReference) throws JsonException {
        try {
            return OBJECT_MAPPER.readValue(file, typeReference);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * @since 0.1.0.0
     */
    public static <T> T fromJson(InputStream is,
                                 TypeReference<T> typeReference) throws JsonException {
        try {
            return OBJECT_MAPPER.readValue(is, typeReference);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }

    /**
     * @since 0.1.0.0
     */
    public static <T> T readValue(byte[] bytes,
                                  TypeReference<T> typeReference) throws JsonException {
        try {
            return OBJECT_MAPPER.readValue(bytes, typeReference);
        } catch (Exception e) {
            throw new JsonException(e);
        }
    }


    /*===Private elements===*/


    /*===Util elements===*/

    private static final ObjectMapper OBJECT_MAPPER = new CustomJsonMapper();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();
}
