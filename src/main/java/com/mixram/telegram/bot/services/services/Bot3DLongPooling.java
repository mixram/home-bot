package com.mixram.telegram.bot.services.services;

import com.mixram.telegram.bot.services.domain.LongPooling;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@Log4j2
@Component
public class Bot3DLongPooling implements LongPooling {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    //

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    //

    // </editor-fold>


    @Override
    public void check() {
        log.info("{} is started!", Bot3DLongPooling.class :: getSimpleName);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
