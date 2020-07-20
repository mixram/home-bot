package com.mixram.telegram.bot;

import com.mixram.telegram.bot.services.services.bot.entity.LazyActionData;
import com.mixram.telegram.bot.services.services.lazyaction.LazyActionLogicImpl;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import org.assertj.core.util.Lists;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author mixram on 2019-04-13.
 * @since ...
 */
public class Tttest {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LazyActionData test1 = LazyActionData.builder()
                                             .messageId(1L)
                                             .actionDateTime(now)
                                             .build();
        LazyActionData test2 = LazyActionData.builder()
                                             .messageId(1L)
                                             .actionDateTime(now.plusMinutes(20))
                                             .build();
        LazyActionData test3 = LazyActionData.builder()
                                             .messageId(1L)
                                             .actionDateTime(now.plusHours(2).plusMinutes(15))
                                             .build();

        List<LazyActionData> testList = Lists.newArrayList(test1, test2, test3);
        testList.sort(LazyActionLogicImpl.LAZY_ACTION_COMPARATOR);

        System.out.println("TEST: " + JsonUtil.toPrettyJson(testList));
    }
}
