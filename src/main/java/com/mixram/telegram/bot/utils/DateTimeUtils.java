package com.mixram.telegram.bot.utils;

import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * @author mixram on 2019-11-25.
 * @since 1.6.1.1
 */
@Log4j2
public class DateTimeUtils {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    //

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    //

    // </editor-fold>


    /**
     * To convert timestamp ({@link Long}) to {@link LocalDateTime}.
     *
     * @param timestamp timestamp.
     *
     * @return converted date and time.
     *
     * @since 1.6.1.1
     */
    public static LocalDateTime getOperationDate(Long timestamp) {
        try {
            return LocalDateTime.ofEpochSecond(timestamp, 0, OffsetDateTime.now().getOffset());
        } catch (Exception e) {
            log.warn("Can not convert timestamp to LocalDateTime!", e);
            return LocalDateTime.now();
        }
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
