package com.mixram.telegram.bot;

import com.google.common.collect.Lists;
import com.mixram.telegram.bot.services.domain.InputMedia;
import com.mixram.telegram.bot.services.domain.entity.InputMediaPhoto;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;

import java.util.Comparator;
import java.util.List;

/**
 * @author mixram on 2019-04-13.
 * @since 0.1.0.0
 */
public class Tttest {

    public static void main(String[] args) {
        final List<InputMedia> mg = Lists.newArrayList(
                new InputMediaPhoto("1", null, null),
                new InputMediaPhoto("2", "тут текст", null),
                new InputMediaPhoto("3", null, null),
                new InputMediaPhoto("4", "#продам", null)
        );
        mg.sort(COMPARATOR2);

        System.out.println("T: " + JsonUtil.toPrettyJson(mg));
    }

    private static final Comparator<InputMedia> COMPARATOR = (o1, o2) -> {
        final String caption1 = o1.getCaption() == null ? "" : o1.getCaption();
        final String caption2 = o2.getCaption() == null ? "" : o2.getCaption();

        return caption1.compareTo(caption2);
    };

    private static final Comparator<InputMedia> COMPARATOR2 = (o1, o2) -> {
        if (o1.getCaption() == null && o2.getCaption() != null) return 1;
        if (o1.getCaption() != null && o2.getCaption() == null) return -1;
        if (o1.getCaption() == null && o2.getCaption() == null) return 0;

        return o1.getCaption().compareTo(o2.getCaption());
    };
}
