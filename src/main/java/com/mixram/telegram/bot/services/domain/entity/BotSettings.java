package com.mixram.telegram.bot.services.domain.entity;

import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Data;

import java.util.Set;

/**
 * @author mixram on 2020-06-15.
 * @since 1.8.0.0
 */
@Data
public class BotSettings {

    private Set<Long> adminsPrime;
    private Set<Long> admins;
    private Boolean reminders;
    private Boolean newDiscountReminder;
    private Boolean enableAntiBot;
    private Boolean versionInform;
    private String welcomeNewUserMessage;
    private String botInfoMessage;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
