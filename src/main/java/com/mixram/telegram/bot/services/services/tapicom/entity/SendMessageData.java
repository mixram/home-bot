package com.mixram.telegram.bot.services.services.tapicom.entity;

import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mixram on 2019-04-22.
 * @since 1.3.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageData {

    private Integer chatId;
    private Integer messageId;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
