package com.mixram.telegram.bot.config.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.WebFilter;

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


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
