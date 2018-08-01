package com.mixram.telegram.bot.config.props;

import com.google.common.collect.ImmutableMap;
import com.mixram.telegram.bot.utils.SpringUtils;
import lombok.Data;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Component for app properties..
 *
 * @author mixram on 2018-08-01.
 * @since 0.1.0.0
 */
@Data
@Component
public class AppProperties {

    /**
     * To get property value.
     *
     * @param key property key.
     *
     * @return found value or null.
     *
     * @since 0.1.0.0
     */
    public String getProperty(String key) {
        Validate.notNull(key, "Property key is not specified!");
        return propMap.get(key);
    }

    /**
     * To get property value.
     *
     * @param key property key.
     *
     * @return found value or exception.
     *
     * @since 0.1.0.0
     */
    public String getRequiredProperty(String key) {
        Validate.notNull(key, "Property key is not specified!");
        String value = getProperty(key);

        Validate.notNull(value, "Can not find property '%s'!", key);

        return value;
    }

    /**
     * To get property value.
     *
     * @param key       property key.
     * @param converter converter to convert string property into desired response type.
     *
     * @return found value or exception.
     *
     * @since 0.1.0.0
     */
    public <T> T getRequiredProperty(String key,
                                     AppPropertiesConverter<String, T> converter) {
        return converter.convert(getRequiredProperty(key));
    }


    /*===Private elements===*/


    /*===Util elements===*/

    private final Map<String, String> propMap;
    private final Environment env;

    @Autowired
    public AppProperties(ConfigurableEnvironment env) {
        this.env = env;
        this.propMap = ImmutableMap.copyOf(SpringUtils.getAllProperties(env));
    }
}
