package com.mixram.telegram.bot.services.domain.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
public enum Command {

    /**
     * Discounts for all shops.
     */
    D_ALL(null, "ALL"),
    /**
     * Discounts for https://3dua.com.ua.
     */
    D_3DUA(Shop3D.SHOP_3DUA, "3DUA"),
    /**
     * Discounts for https://u3df.com.ua.
     */
    D_U3DF(Shop3D.SHOP_U3DF, "U3DF"),
    /**
     * Discounts for https://monofilament.com.ua.
     */
    D_MF(Shop3D.SHOP_MONOFILAMENT, "MONO"),
    /**
     * Discounts for https://shop.plexiwire.com.ua.
     */
    D_PLEX(Shop3D.SHOP_PLEXIWIRE, "PLEX"),
    /**
     * Discounts for https://3dplast.biz.
     */
    D_3DP(Shop3D.SHOP_3DPLAST, "3DP"),
    /**
     * Discounts for https://dasplast.com.
     */
    D_DAS(Shop3D.SHOP_DASPLAST, "DAS"),
    /**
     * Info about bot.
     */
    INFO(null, "INFO"),
    /**
     * Info about bot version, etc (for admin only).
     */
    INFO_ADMIN(null, "INFO-ADMIN"),
    /**
     * Start command.
     */
    START(null, "START"),
    /**
     * Start command.
     */
    TEST(null, "TEST"),
    /**
     * Start command.
     */
    CAS(null, "CAS");

    @Getter
    Shop3D shop;
    @Getter
    String name;

    private static final Map<String, Command> BY_NAME;
    private static final Map<Shop3D, Command> BY_SHOP;

    static {
        Map<String, Command> byNameTemp = new HashMap<>(Command.values().length);
        for (Command value : Command.values()) {
            byNameTemp.put(value.getName(), value);
        }
        BY_NAME = ImmutableMap.copyOf(byNameTemp);

        Map<Shop3D, Command> byShopTemp = new HashMap<>(Command.values().length);
        for (Command value : Command.values()) {
            if (value.getShop() != null) {
                byShopTemp.put(value.getShop(), value);
            }
        }
        BY_SHOP = ImmutableMap.copyOf(byShopTemp);
    }

    Command(Shop3D shop,
            String name) {
        this.shop = shop;
        this.name = name;
    }

    public static Command getByName(String name) {
        return BY_NAME.get(name);
    }

    public static Command getByShop(Shop3D shop) {
        return BY_SHOP.get(shop);
    }

}
