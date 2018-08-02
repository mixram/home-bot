package com.mixram.telegram.bot.utils.databinding;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mixram.telegram.bot.utils.databinding.ex.JsonException;
import com.mixram.telegram.bot.utils.databinding.support.CustomJsonMapper;

import java.io.File;
import java.io.InputStream;

/**
 * @author mixram on 2017-07-17
 * @since 0.1.0.0
 */
public final class JsonUtil {

    public static final ObjectMapper OBJECT_MAPPER = new CustomJsonMapper();
    public static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();


    private JsonUtil() {
    }

    /**
     * Преобразование объекта в PrettyJson. Гарантирует ответ без null.
     *
     * @param obj объект для преобразования.
     *
     * @return преобразованный в строку объект или JsonException.
     *
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
     * Преобразование объекта в Json. Гарантирует ответ без null.
     *
     * @param obj объект для преобразования.
     *
     * @return преобразованный в строку объект или JsonException.
     *
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
     * Преобразование объекта в массив байт. Гарантирует ответ без null.
     *
     * @param obj объект для преобразования.
     *
     * @return преобразованный в массив байт объект или JsonException.
     *
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
     * Получение объект из Json. Гарантирует ответ без null.
     *
     * @param json json-строка для преобразования в объект.
     * @param <T>  класс объекта для преобразования.
     *
     * @return преобразованный объект или JsonException.
     *
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
     * Получение объект из Json. Гарантирует ответ без null.
     *
     * @param json json-строка для преобразования в объект.
     * @param <T>  класс объекта для преобразования.
     *
     * @return преобразованный объект или JsonException.
     *
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
     * Получение объект из Json. Гарантирует ответ без null.
     *
     * @param file файл, из которого будет производиться чтение.
     * @param <T>  класс объекта для преобразования.
     *
     * @return преобразованный объект или JsonException.
     *
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
     * Получение объект из Json. Гарантирует ответ без null.
     *
     * @param is  InputStream с данными.
     * @param <T> класс объекта для преобразования.
     *
     * @return преобразованный объект или JsonException.
     *
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
     * Получение объект из массива байт. Гарантирует ответ без null.
     *
     * @param bytes массив байт с данными.
     * @param <T>   класс объекта для преобразования.
     *
     * @return преобразованный объект или JsonException.
     *
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
}
