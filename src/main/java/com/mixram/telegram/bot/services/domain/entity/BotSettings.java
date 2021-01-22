package com.mixram.telegram.bot.services.domain.entity;

import com.mixram.telegram.bot.utils.Validable;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.Data;

import java.util.Set;

/**
 * @author mixram on 2020-06-15.
 * @since 1.8.0.0
 */
@Data
public class BotSettings implements Validable {

    private Set<Long> adminsPrime;
    private Set<Long> admins;
    private Boolean reminders;
    private Boolean newDiscountReminder;
    private Boolean enableAntiBot;
    private Boolean versionInform;
    private String welcomeNewUserMessage;
    private String botInfoMessage;
    /**
     * In seconds.
     */
    private Integer helloMessageDeleteTime;
    /**
     * In seconds.
     */
    private Integer botInfoRequestDeleteTime;
    private Set<Long> infoChats;

    @Override
    public boolean isValid() {
        return adminsPrime != null && admins != null && reminders != null && newDiscountReminder != null &&
                enableAntiBot != null && versionInform != null && welcomeNewUserMessage != null &&
                botInfoMessage != null && helloMessageDeleteTime != null && botInfoRequestDeleteTime != null;
    }

    @Override
    public boolean isInvalid() {
        return !isValid();
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
