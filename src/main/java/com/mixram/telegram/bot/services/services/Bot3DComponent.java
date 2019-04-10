package com.mixram.telegram.bot.services.services;

import org.springframework.http.HttpHeaders;

/**
 * @author mixram on 2019-04-10.
 * @since ...
 */
public interface Bot3DComponent {

    <T> T getBotInfo(String url,
                     HttpHeaders headers,
                     T answer);
}
