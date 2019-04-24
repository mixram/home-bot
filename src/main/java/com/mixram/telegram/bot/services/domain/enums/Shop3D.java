package com.mixram.telegram.bot.services.domain.enums;

import lombok.Getter;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
public enum Shop3D {

    SHOP_3DUA("3DUA", "https://3dua.com.ua/plastik-dlya-3d-printera"),
    SHOP_U3DF("U3DF", "https://cs2734145.prom.ua"),
    SHOP_MONOFILAMENT("MonoFilament", "https://monofilament.com.ua/products/standartnye-materialy/"),
    //    SHOP_PLEXIWIRE("Plexiwire", "https://shop.plexiwire.com.ua/plexiwire-filament/"),
    SHOP_3DPLAST("3DPlast", "https://3dplast.biz"),
    //    SHOP_DASPLAST("DASplast", "https://dasplast.com")
    ;

    @Getter
    String name;
    @Getter
    String url;

    Shop3D(String name,
           String url) {
        this.name = name;
        this.url = url;
    }

}
