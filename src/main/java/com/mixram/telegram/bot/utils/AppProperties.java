package com.mixram.telegram.bot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Component to work with properties.
 *
 * @author mixram on 2018-04-20.
 * @since 0.1.0.0
 */
@Slf4j
@Component
public class AppProperties {

    /**
     * To get property.
     *
     * @param key property name.
     *
     * @return found property or null, if property is not found.
     *
     * @since 0.1.0.0
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
     * @since 0.1.0.0
     */
    public String getRequiredProperty(String key) {
        Validate.notBlank(key, "Key is not specified!");

        String property = properties.getProperty(key);
        Validate.notBlank(property, "Can not find property '%s'!", key);

        return property;
    }

    /**
     * To assert the property existence.
     *
     * @param key property name.
     *
     * @since 0.1.0.0
     */
    public void assertPropertyExists(String key) {
        Validate.notNull(key, "Property key is not specified!");
        Validate.notNull(getProperty(key), "Property value for key '%s' is not found!", key);
    }

    /*===Private elements===*/


    /*===Util elements===*/

    private final Properties properties;

    /**
     * To create an instance of {@link AppProperties}.<br>
     * Properties will be load automatically. If property-file have not found - an exception will be thrown.
     *
     * @since 0.1.0.0
     */
    public AppProperties() throws IOException {
        this.properties = new Properties();

        String pathToProps = System.getProperty("application.props");
        Validate.notBlank(pathToProps, "Path to external properties is not found!");

        try(InputStream is = new FileInputStream(pathToProps)) {
            this.properties.load(is);
        }
    }

}
