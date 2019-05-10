package com.mixram.telegram.bot.services.domain.enums;

import lombok.Getter;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
public enum Shop3D {

    SHOP_3DUA("3DUA", "3dua.com.ua", "https://3dua.com.ua/plastik-dlya-3d-printera"),
    SHOP_U3DF("U3DF", "u3df.com.ua", "https://u3df.com.ua"),
    SHOP_MONOFILAMENT("MonoFilament", "monofilament.com.ua", "https://monofilament.com.ua/products/standartnye-materialy/"),
    SHOP_PLEXIWIRE("Plexiwire", "shop.plexiwire.com.ua", "https://shop.plexiwire.com.ua/plexiwire-filament/"),
    SHOP_3DPLAST("3DPlast", "3dplast.biz", "https://3dplast.biz"),
    SHOP_DASPLAST("DASplast", "dasplast.com", "https://dasplast.com");

    @Getter
    String name;
    @Getter
    String nameAlt;
    @Getter
    String url;

    Shop3D(String name,
           String nameAlt,
           String url) {
        this.name = name;
        this.nameAlt = nameAlt;
        this.url = url;
    }

}
