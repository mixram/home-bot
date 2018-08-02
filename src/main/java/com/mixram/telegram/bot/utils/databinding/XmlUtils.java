package com.mixram.telegram.bot.utils.databinding;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mixram.telegram.bot.utils.databinding.ex.XmlException;
import com.mixram.telegram.bot.utils.databinding.support.CustomXmlMapper;

import java.io.InputStream;

/**
 * Класс-обёртка, обеспечивающий функционал работы с xml.
 *
 * @author mixram on 22/09/16
 * @since 0.1.0.0
 */
public final class XmlUtils {

    private static final XmlMapper XML_MAPPER = new CustomXmlMapper();
    private static final ObjectWriter OBJECT_WRITER;

    static {
        OBJECT_WRITER = XML_MAPPER.writerWithDefaultPrettyPrinter();
    }

    /**
     * Преобразовать xml в объект.
     *
     * @param xml           xml для преобразования.
     * @param typeReference тип объекта.
     * @param <T>           возвращаемый тип.
     *
     * @return десериализованный из xml объект.
     *
     * @throws XmlException в случае ошибки парсинга.
     * @since 0.1.0.0
     */
    public static <T> T fromXml(String xml,
                                TypeReference<T> typeReference) {
        try {
            return XML_MAPPER.readValue(xml, typeReference);
        } catch (Exception e) {
            throw new XmlException(e);
        }
    }

    /**
     * Преобразовать xml в объект.
     *
     * @param xml   xml для преобразования.
     * @param clazz класс объекта.
     * @param <T>   возвращаемый тип.
     *
     * @return десериализованный из xml объект.
     *
     * @throws XmlException в случае ошибки парсинга.
     * @since 0.1.0.0
     */
    public static <T> T fromXml(String xml,
                                Class<T> clazz) {
        try {
            return XML_MAPPER.readValue(xml, clazz);
        } catch (Exception e) {
            throw new XmlException(e);
        }
    }

    /**
     * Преобразовать xml в объект.
     *
     * @param is    InputStream с данными.
     * @param clazz класс объекта.
     * @param <T>   возвращаемый тип.
     *
     * @return десериализованный из xml объект.
     *
     * @throws XmlException в случае ошибки парсинга.
     * @since 0.1.0.0
     */
    public static <T> T fromXml(InputStream is,
                                Class<T> clazz) {
        try {
            return XML_MAPPER.readValue(is, clazz);
        } catch (Exception e) {
            throw new XmlException(e);
        }
    }

    /**
     * Преобразовать xml в объект.
     *
     * @param is            InputStream с данными.
     * @param typeReference тип объекта.
     * @param <T>           возвращаемый тип.
     *
     * @return десериализованный из xml объект.
     *
     * @throws XmlException в случае ошибки парсинга.
     * @since 0.1.0.0
     */
    public static <T> T fromXml(InputStream is,
                                TypeReference<T> typeReference) {
        try {
            return XML_MAPPER.readValue(is, typeReference);
        } catch (Exception e) {
            throw new XmlException(e);
        }
    }

    /**
     * Преобразовать объект в xml-строку.
     *
     * @param value объект для преобразования.
     *
     * @return стока с xml-представлением объекта.
     *
     * @throws XmlException в случае преобразования в строку.
     * @since 0.1.0.0
     */
    public static String toXml(Object value) {
        try {
            return XML_MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            throw new XmlException(e);
        }
    }

    /**
     * Преобразовать объект в pretty-xml-строку.
     *
     * @param value объект для преобразования.
     *
     * @return стока с xml-представлением объекта.
     *
     * @throws XmlException в случае преобразования в строку.
     * @since 0.1.0.0
     */
    public static String toPrettyXml(Object value) {
        try {
            return OBJECT_WRITER.writeValueAsString(value);
        } catch (Exception e) {
            throw new XmlException(e);
        }
    }
}
