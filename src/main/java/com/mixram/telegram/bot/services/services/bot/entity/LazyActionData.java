package com.mixram.telegram.bot.services.services.bot.entity;

import com.mixram.telegram.bot.services.services.bot.enums.LazyAction;
import com.mixram.telegram.bot.utils.Validable;
import com.mixram.telegram.bot.utils.Validable2;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author mixram on 2020-07-20.
 * @since 1.8.2.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LazyActionData implements Serializable, Validable, Validable2 {

    private static final long serialVersionUID = 0L;

    private Long chatId;
    private Long messageId;
    private LazyAction action;
    private LocalDateTime actionDateTime;

    @Override
    public boolean isValid() {
        return chatId != null && messageId != null && action != null && actionDateTime != null;
    }

    @Override
    public boolean isInvalid() {
        return !isValid();
    }

    @Override
    public void checkValid() {
        Validate.isTrue(isValid(), "Data is invalid!");
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
