package com.mixram.telegram.bot.utils;

import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

/**
 * @author mixram on 2019-02-13.
 * @since 0.1.0.0
 */
public class CommonHeadersBuilder {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final HttpHeaders headers;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    private CommonHeadersBuilder(HttpHeaders headers) {
        Validate.notNull(headers, "Headers are not specified!");
        this.headers = headers;
    }

    // </editor-fold>


    public static CommonHeadersBuilder newInstance() {
        return new CommonHeadersBuilder(new HttpHeaders());
    }

    public static CommonHeadersBuilder newInstance(HttpHeaders headers) {
        return new CommonHeadersBuilder(headers);
    }


    public HttpHeaders build() {
        return headers;
    }

    public CommonHeadersBuilder json() {
        jsonAccept();
        jsonContentType();

        return this;
    }

    public CommonHeadersBuilder jsonAccept() {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));

        return this;
    }

    public CommonHeadersBuilder jsonContentType() {
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return this;
    }

    public CommonHeadersBuilder xml() {
        xmlAccept();
        xmlContentType();

        return this;
    }

    public CommonHeadersBuilder xmlAccept() {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));

        return this;
    }

    public CommonHeadersBuilder xmlContentType() {
        headers.setContentType(MediaType.APPLICATION_XML);

        return this;
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
