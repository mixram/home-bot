package com.mixram.telegram.bot.config.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mixram.telegram.bot.utils.databinding.support.CustomJsonMapper;
import com.mixram.telegram.bot.utils.databinding.support.CustomXmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.WebFilter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Web configuration.
 *
 * @author mixram on 2018-08-02.
 * @since 0.1.0.0
 */
@Slf4j
@Configuration
public class WebConfig {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static Set<String> actuatorEndpoints = Sets.newHashSet("/health", "/info");

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    public static void setActuatorEndpoints(Set<String> actuatorEndpoints) {
        WebConfig.actuatorEndpoints = actuatorEndpoints;
    }

    public static Set<String> getActuatorEndpoints() {
        return WebConfig.actuatorEndpoints;
    }

    // </editor-fold>


    @Bean
    public WebFilter acceptWebFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (actuatorEndpoints.contains(request.getPath().value())) {
                log.trace("AUTH_PROC_LOG ==> actuator endpoint '{}' is detected. Change header 'Accept'");

                ServerHttpRequest mutatedRequest = request
                        .mutate()
                        .headers(httpHeaders -> httpHeaders.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON)))
                        .build();
                exchange = exchange.mutate().request(mutatedRequest).build();
            }

            return chain.filter(exchange);
        };
    }

    @Bean("createMessageConverters")
    public List<HttpMessageConverter<?>> createMessageConverters() {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(new CustomJsonMapper());
        jackson2HttpMessageConverter.setSupportedMediaTypes(Lists.newArrayList(
                MediaType.APPLICATION_JSON,
                MediaType.TEXT_PLAIN,
                MediaType.APPLICATION_JSON_UTF8,
                MediaType.TEXT_HTML
        ));

        MappingJackson2XmlHttpMessageConverter jackson2XmlHttpMessageConverter = new MappingJackson2XmlHttpMessageConverter();
        jackson2XmlHttpMessageConverter.setObjectMapper(new CustomXmlMapper());
        jackson2XmlHttpMessageConverter.setSupportedMediaTypes(Lists.newArrayList(
                MediaType.APPLICATION_ATOM_XML,
                MediaType.APPLICATION_RSS_XML,
                MediaType.APPLICATION_XHTML_XML,
                MediaType.APPLICATION_XML,
                MediaType.TEXT_XML
        ));

        List<HttpMessageConverter<?>> converters = new ArrayList<>(4);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        converters.add(jackson2HttpMessageConverter);
        converters.add(jackson2XmlHttpMessageConverter);
        //converters.add(new Jaxb2RootElementHttpMessageConverter());
        //converters.add(new Jaxb2CollectionHttpMessageConverter());

        return converters;
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
