package com.mixram.telegram.bot.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author mixram on 2019-11-25.
 * @since 1.6.1.1
 */
@TestPropertySource("file:/Users/mixram/Documents/Dev/projects/universal-bot/dev/configs/bootstrap.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class DateTimeUtilsTest {

    @Test
    public void getOperationDate() {
        final LocalDateTime expected = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        final Long timestamp = expected.toEpochSecond(OffsetDateTime.now().getOffset());
        final LocalDateTime actual = DateTimeUtils.getOperationDate(timestamp);

        Assert.assertEquals(expected, actual);

        System.out.println("OK --- " + new Object() {}.getClass().getEnclosingMethod().getName());
    }
}
