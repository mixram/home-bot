package com.mixram.telegram.bot.utils;

/**
 * @author mixram on 2020-07-20.
 * @since 1.8.2.0
 */
public abstract class ValidableAdapter implements Validable {

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isInvalid() {
        return !isValid();
    }
}
