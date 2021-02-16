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
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LazyActionData that = (LazyActionData) o;
        return chatId.equals(that.chatId) && messageId.equals(that.messageId) && action == that.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, messageId, action);
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
