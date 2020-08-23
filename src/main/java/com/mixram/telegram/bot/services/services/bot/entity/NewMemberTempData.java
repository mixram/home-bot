package com.mixram.telegram.bot.services.services.bot.entity;

import com.mixram.telegram.bot.services.domain.entity.User;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author mixram on 2020-01-19.
 * @since 1.7.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewMemberTempData {

    private User user;
    private LocalDateTime added;
    private List<Long> messagesToDelete;
    private Long userIncomeMessageId;
    private int rightAnswerNumber;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
