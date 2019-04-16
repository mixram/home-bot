package com.mixram.telegram.bot.services.domain.enums;

import lombok.Getter;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
public enum Shop3D {

    SHOP_3DUA("3DUA"),
    //    SHOP_U3DF("U3DF"),
    //    SHOP_MONOFILAMENT("MonoFilament"),
    //    SHOP_PLEXIWIRE("Plexiwire"),
    SHOP_3DPLAST("3DPlast"),
    //    SHOP_DASPLAST("DASplast")
    ;

    @Getter
    String name;

    Shop3D(String name) {
        this.name = name;
    }

}
