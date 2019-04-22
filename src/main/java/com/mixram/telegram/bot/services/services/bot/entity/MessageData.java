package com.mixram.telegram.bot.services.services.bot.entity;

import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mixram on 2019-04-22.
 * @since 1.3.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {

    private boolean toAdmin;
    private boolean toResponse;
    private String message;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
