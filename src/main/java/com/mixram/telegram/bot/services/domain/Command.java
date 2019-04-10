package com.mixram.telegram.bot.services.domain;

import lombok.Getter;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
public enum Command {

    /**
     * To show all commands.
     */
    COMMANDS(null),
    /**
     * Discounts for all shops.
     */
    D_ALL(null),
    /**
     * Discounts for https://3dua.com.ua.
     */
    D_3DUA(Shop3D.SHOP_3DUA),
    /**
     * Discounts for https://u3df.com.ua.
     */
    DISCOUNT_U3DF(Shop3D.SHOP_U3DF),
    /**
     * Discounts for https://monofilament.com.ua.
     */
    D_MF(Shop3D.SHOP_MONOFILAMENT),
    /**
     * Discounts for https://shop.plexiwire.com.ua.
     */
    D_PLEX(Shop3D.SHOP_PLEXIWIRE),
    /**
     * Discounts for https://3dplast.biz.
     */
    D_3DP(Shop3D.SHOP_3DPLAST),
    /**
     * Discounts for https://dasplast.com.
     */
    D_DAS(Shop3D.SHOP_DASPLAST);

    @Getter
    Shop3D shop;

    Command(Shop3D shop) {
        this.shop = shop;
    }

}
