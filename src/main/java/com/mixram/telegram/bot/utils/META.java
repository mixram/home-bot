package com.mixram.telegram.bot.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.mixram.telegram.bot.services.domain.entity.BotSettings;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

/**
 * @author mixram on 2019-04-25.
 * @since 0.1.0.0
 */
@Component
public final class META {

    public static final Locale DEFAULT_LOCALE = new Locale("uk");
    public static final String NOT_A_BOT_TEXT = "i_am_real_man";

    public final Map<Long, BotSettings> settings;

    @Autowired
    public META(@Value("${bot.settings.group.settings}") String settings) {
        Map<Long, BotSettings> tempMap = JsonUtil.fromJson(settings, new TypeReference<Map<Long, BotSettings>>() {});
        this.settings = ImmutableMap.copyOf(tempMap);
    }
}
