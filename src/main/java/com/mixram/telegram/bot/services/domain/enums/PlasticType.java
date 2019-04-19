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
    COPET("coPET"),
    /**
     * ABS.
     */
    ABS("ABS"),
    /**
     * ABS+.
     */
    ABS_PLUS("ABS+"),
    /**
     * ABS ECO.
     */
    ABS_ECO("ABS-ECO"),
    /**
     * ABS FLEX.
     */
    ABS_FLEX("ABS-FLEX"),
    /**
     * ABS PRO.
     */
    ABS_PRO("ABS-PRO"),
    /**
     * PLA.
     */
    PLA("PLA"),
    /**
     * PLA PLUS.
     */
    PLA_PLUS("PLA+"),
    /**
     * FLEX.
     */
    FLEX("Flex"),
    /**
     * SAN.
     */
    SAN("SAN"),
    /**
     * PC.
     */
    PC("PC"),
    /**
     * HIPS.
     */
    HIPS("HIPS"),
    /**
     * Nylon.
     */
    NYLON("Nylon"),
    /**
     * ELASTAN.
     */
    ELASTAN("ELASTAN"),
    /**
     * PET.
     */
    PET("PET"),
    /**
     * PA.
     */
    PA("PA"),
    /**
     * PBT.
     */
    PBT("PBT"),
    /**
     * ASA.
     */
    ASA("ASA"),
    /**
     * PLA CCF.
     */
    PLA_CCF("PLA-CCF"),
    /**
     * PLA-CCU.
     */
    PLA_CCU("PLA-CCU"),
    /**
     * PLA-СG+.
     */
    PLA_CG_PLUS("PLA-CG+");

    @Getter
    String name;

    PlasticType(String name) {
        this.name = name;
    }

}
