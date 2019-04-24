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

    /**
     * To send the answer to admin only.
     */
    private boolean toAdmin;
    /**
     * To send message as response to incoming message.
     */
    private boolean toResponse;
    /**
     * To leave a chat.
     */
    private boolean leaveChat;
    /**
     * To answer to user or group.
     */
    private boolean userResponse;
    /**
     * To show URL`s preview or not.
     */
    private boolean showUrlPreview;
    private String message;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
