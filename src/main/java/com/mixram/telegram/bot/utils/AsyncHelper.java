package com.mixram.telegram.bot.utils;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * @author mixram on 2019-03-29.
 * @since 0.2.0.0
 */
@Component
public class AsyncHelper {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    //

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    //

    // </editor-fold>


    @Async
    public void doAsync(Supplier<?> supplier) {
        supplier.get();
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
