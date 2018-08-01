package com.mixram.telegram.bot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utilities to manage work with Spring.
 *
 * @author mixram on 2018-07-18
 * @since 0.1.0.0
 */
@Slf4j
public class SpringUtils {

    /**
     * To get all properties for Spring.<br>
     * <strong>Important!</strong> not iterable values from PropertySource are not available (JndiPropertySource,
     * StubPropertySource, PropertySource from EndpointWebMvcAutoConfiguration, etc).
     *
     * @param env ConfigurableEnvironment
     *
     * @return the map, key - property key, value - property value.
     *
     * @since 0.1.0.0
     */
    public static Map<String, String> getAllProperties(ConfigurableEnvironment env) {
        Set<String> keys = new HashSet<>();
        for (PropertySource<?> ps : env.getPropertySources()) {
            keys.addAll(getAllPropertiesNames(ps));
        }

        return keys.stream()
                   .collect(Collectors.toMap(key -> key, env :: getProperty));
    }


    /*===Private elements===*/

    /**
     * @since 0.1.0.0
     */
    private static Set<String> getAllPropertiesNames(PropertySource ps) {
        Set<String> keys = new HashSet<>();

        if (ps instanceof EnumerablePropertySource) {
            String[] propertyNames = ((EnumerablePropertySource) ps).getPropertyNames();
            keys.addAll(Arrays.asList(propertyNames));

            return keys;
        }

        if (ps instanceof CompositePropertySource) {
            CompositePropertySource cps = (CompositePropertySource) ps;
            for (PropertySource nestedPs : cps.getPropertySources()) {
                keys.addAll(getAllPropertiesNames(nestedPs));
            }

            return keys;
        }

        log.warn(
                "PropertySource is instanceof {} and cannot be iterated! Cannot get property names from this PropertySource!",
                ps.getClass().getName());

        return keys;
    }


    /*===Util elements===*/

}
