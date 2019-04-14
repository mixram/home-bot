package com.mixram.telegram.bot.services.domain.enums;

import lombok.Getter;

/**
 * @author mixram on 2019-04-13.
 * @since 0.1.3.0
 */
public enum PlasticType {

    /**
     * CoPET(PET-G).
     */
    COPET("coPET(PET-G)"),
    /**
     * ABS.
     */
    ABS("ABS"),
    /**
     * PLA.
     */
    PLA("PLA"),
    /**
     * FLEX.
     */
    FLEX("FLEX");

    @Getter
    String name;

    PlasticType(String name) {
        this.name = name;
    }

}
