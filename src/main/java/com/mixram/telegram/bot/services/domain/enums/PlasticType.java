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
    COPET("COPET"),
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
    ABS_ECO("ABS-E"),
    /**
     * ABS FLEX.
     */
    ABS_FLEX("ABS-F"),
    /**
     * ABS PRO.
     */
    ABS_PRO("ABS-P"),
    /**
     * MBS.
     */
    MBS("MBS"),
    /**
     * ABS X.
     */
    ABS_X("ABS-X"),
    /**
     * ABS Premium.
     */
    ABS_P("ABS_P"),
    /**
     * ABS engineering.
     */
    ABS_E("ABS-E"),
    /**
     * PLA.
     */
    PLA("PLA"),
    /**
     * PLA PLUS.
     */
    PLA_PLUS("PLA+"),
    /**
     * APLA.
     */
    APLA("APLA"),
    /**
     * FLEX.
     */
    FLEX("FLEX"),
    /**
     * SAN.
     */
    SAN("SAN"),
    /**
     * PC.
     */
    PC("PC"),
    /**
     * PC+ABS.
     */
    PC_ABS("PC+A"),
    /**
     * HIPS.
     */
    HIPS("HIPS"),
    /**
     * Nylon.
     */
    NYLON("NYLON"),
    /**
     * ELASTAN.
     */
    ELASTAN("ELAST"),
    /**
     * PET.
     */
    PET("PET"),
    /**
     * PETG.
     */
    PETG("PETG"),
    /**
     * PA.
     */
    PA("PA"),
    /**
     * PA+.
     */
    PA_PLUS("PA+"),
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
    PLA_CCF("PLA-F"),
    /**
     * PLA-CCU.
     */
    PLA_CCU("PLA-U"),
    /**
     * PLA-СG+.
     */
    PLA_CG_PLUS("PLA-G"),
    /**
     * Substandard.
     */
    SUBSTANDARD("SALE"),
    /**
     * Composite.
     */
    COMPO("COMP"),
    /**
     * Other.
     */
    OTHER("OTHER");

    @Getter
    String name;

    PlasticType(String name) {
        this.name = name;
    }

}
