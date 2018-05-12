package com.mixram.telegram.bot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Component to work with properties.
 *
 * @author mixram on 2018-05-12.
 * @since 0.1.1.0
 */
@Slf4j
public class AppProperties {

    private final Properties properties;

    /**
     * To create an instance of {@link AppProperties}.<br>
     * Properties will be load automatically. If property-file have not found - an exception will be thrown.
     *
     * @since 0.1.1.0
     */
    public AppProperties() throws IOException {
        this.properties = new Properties();

        String pathToProps = System.getProperty("application.props");
        Validate.notBlank(pathToProps, "Path to external properties is not found!");

        try(InputStream is = new FileInputStream(pathToProps)) {
            this.properties.load(is);
        }

        log.debug("token: {}", properties.getProperty("token"));
    }

    /**
     * To get property.
     *
     * @param key property name.
     *
     * @return found property or null, if property is not found.
     *
     * @since 0.1.1.0
     */
    public String getProperty(String key) {
        Validate.notBlank(key, "Key is not specified!");

        return properties.getProperty(key);
    }

    /**
     * To get property.
     *
     * @param key property name.
     *
     * @return found property or exception, if property is not found.
     *
     * @since 0.1.1.0
     */
    public String getRequiredProperty(String key) {
        Validate.notBlank(key, "Key is not specified!");

        String property = properties.getProperty(key);
        Validate.notBlank(property, "Can not find property '%s'!", key);

        return property;
    }

    /**
     * To check if property exists in storage.<br>
     * If no property is found - an {@link NullPointerException} will be thrown.
     *
     * @param key property name.
     *
     * @since 0.1.1.0
     */
    public void checkPropertyExists(String key) {
        Validate.notNull(key, "Property key is not specified!");
        Validate.notNull(getProperty(key), "Property value for key '%s' is not found!", key);
    }
}
