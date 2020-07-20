package com.mixram.telegram.bot.services.services.bot.entity;

import com.google.common.collect.Lists;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    /**
     * Logic to run if the message has "lazy action" logic.
     */
    private List<Consumer<Long>> doIfLazyAction;

    public void setDoIfLazyAction(List<Consumer<Long>> lazyActionList) {
        prepareLazyActionList();
        this.doIfLazyAction.addAll(lazyActionList);
    }

    public void setDoIfLazyAction(Consumer<Long> lazyAction) {
        prepareLazyActionList();
        this.doIfLazyAction.add(lazyAction);
    }

    private void prepareLazyActionList() {
        if (this.doIfLazyAction == null) this.doIfLazyAction = Lists.newArrayList();
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
