package com.mixram.telegram.bot.config.props;

/**
 * @author mixram on 2018-08-01.
 * @since 0.1.0.0
 */
@FunctionalInterface
public interface AppPropertiesConverter<F, T> {

    T convert(F from);
}
