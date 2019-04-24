package com.mixram.telegram.bot.services.services.stat.entity;

import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author mixram on 2019-04-20.
 * @since 1.3.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatData {

    private Map<Long, StatDataInner> data;
    private LocalDateTime updated;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatDataInner {

        private Long id;
        private String username;
        private Integer counter;
        private LocalDateTime updated;
    }
}
