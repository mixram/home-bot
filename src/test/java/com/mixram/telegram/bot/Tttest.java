package com.mixram.telegram.bot;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author mixram on 2019-04-13.
 * @since ...
 */
public class Tttest {

    public static final Map<Integer, String> ANTI_BOT_QUESTIONS;
    public static final int MIN = 1;
    public static final int MAX;

    static {
        Map<Integer, String> tempMap = new HashMap<>(6);
        tempMap.put(0, "Нуль");
        tempMap.put(1, "Один");
        tempMap.put(2, "Два");
        tempMap.put(3, "Три");
        tempMap.put(4, "Чотири");
        tempMap.put(5, "П'ять");

        ANTI_BOT_QUESTIONS = ImmutableMap.copyOf(tempMap);
        MAX = ANTI_BOT_QUESTIONS.size() - 1;
    }

    public static void main(String[] args) {
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(MAX - MIN + 1) + MIN);
        }
    }
}
