package com.mixram.telegram.bot.services.services.bot.entity;

import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;

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
    /**
     * Message.
     */
    private String message;
    /**
     * Markup for reply (e.g.: to make a keyboard)
     */
    private Object replyMarkup;
    /**
     * Logic to run if the message is from anti-bot check.
     */
    private Consumer<Long> doIfAntiBot;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
