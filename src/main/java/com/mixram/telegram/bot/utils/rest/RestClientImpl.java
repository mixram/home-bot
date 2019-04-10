package com.mixram.telegram.bot.utils.rest;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.OutputStream;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author mixram on 2018-10-17.
 * @since 0.1.0.0
 */
@Scope("prototype")
@Log4j2
@Service
public class RestClientImpl extends RestClientHelper implements RestClient {

    /**
     * To create instance of {@link RestClientImpl} with parameters.
     *
     * @return instance of {@link RestClientImpl} with parameters.
     *
     * @see RestClientFactory#newRestTemplate(int, int, int, int, int, int, boolean, boolean, List, List)
     * @since 0.1.0.0
     */
    public static RestClient getWithParams(String maxConnections,
                                           String maxConnectionsPerRoute,
                                           String connectTimeout,
                                           String connectionRequestTimeout,
                                           String timeoutResponse,
                                           String secondsToLive,
                                           boolean sslDisabled,
                                           List<ClientHttpRequestInterceptor> interceptors,
                                           List<HttpMessageConverter<?>> messageConverters) {
        return new RestClientImpl(maxConnections,
                                  maxConnectionsPerRoute,
                                  connectTimeout,
                                  connectionRequestTimeout,
                                  timeoutResponse,
                                  secondsToLive,
                                  sslDisabled,
                                  true,
                                  interceptors,
                                  messageConverters);
    }

    /**
     * To create instance of {@link RestClientImpl} with parameters.
     *
     * @return instance of {@link RestClientImpl} with parameters.
     *
     * @see RestClientFactory#newRestTemplate(int, int, int, int, int, int, boolean, boolean, List, List)
     * @since 0.1.0.0
     */
    public static RestClient getWithParams(String maxConnections,
                                           String maxConnectionsPerRoute,
                                           String connectTimeout,
                                           String connectionRequestTimeout,
                                           String timeoutResponse,
                                           String secondsToLive,
                                           boolean sslDisabled,
                                           boolean bufferBody,
                                           List<ClientHttpRequestInterceptor> interceptors,
                                           List<HttpMessageConverter<?>> messageConverters) {
        return new RestClientImpl(maxConnections,
                                  maxConnectionsPerRoute,
                                  connectTimeout,
                                  connectionRequestTimeout,
                                  timeoutResponse,
                                  secondsToLive,
                                  sslDisabled,
                                  bufferBody,
                                  interceptors,
                                  messageConverters);
    }

    /**
     * To get instance of {@link RestTemplate} with parameters.
     *
     * @return instance of {@link RestTemplate} with parameters.
     *
     * @see RestClientFactory#newRestTemplate(int, int, int, int, int, int)
     * @since 0.1.0.0
     * @deprecated you can use this method, but it is better to use
     * {@link RestClientImpl#getWithParams(String, String, String, String, String, String, boolean, boolean, List, List)}
     */
    @Deprecated
    public static RestTemplate getTemplateWithParams(int maxConnections,
                                                     int maxConnectionsPerRoute,
                                                     int connectTimeout,
                                                     int connectionRequestTimeout,
                                                     int timeoutResponse,
                                                     int secondsToLive,
                                                     List<ClientHttpRequestInterceptor> interceptors,
                                                     List<HttpMessageConverter<?>> messageConverters) {

        return createRestTemplate(maxConnections, maxConnectionsPerRoute, connectTimeout, connectionRequestTimeout,
                                  timeoutResponse, secondsToLive, false, true, interceptors, messageConverters);
    }

    @Override
    public <T> T get(String url,
                     Map<String, String> params,
                     Map<String, String> headers,
                     Class<T> responseType) {
        return super.get(url, responseType, params, headers);
    }

    @Override
    public <T> T get(String url,
                     Map<String, String> params,
                     Map<String, List<String>> paramsL,
                     Map<String, String> headers,
                     Class<T> responseType) {
        return super.get(url, responseType, params, paramsL, headers);
    }

    @Override
    public <T> T get(String url,
                     Map<String, String> params,
                     Map<String, String> headers,
                     ParameterizedTypeReference<T> responseType) {
        return super.get(url, responseType, params, headers);
    }

    @Override
    public void get(String url,
                    Map<String, String> params,
                    Map<String, String> headers,
                    OutputStream stream) {
        super.get(url, params, headers, stream);
    }

    @Override
    public <T> T get(String url,
                     Map<String, String> headers,
                     Class<T> responseType) {
        return super.get(url, responseType, null, headers);
    }

    @Override
    public <T> T get(String url,
                     Map<String, String> headers,
                     ParameterizedTypeReference<T> responseType) {
        return super.get(url, responseType, null, headers);
    }

    @Override
    public void get(String url,
                    Map<String, String> headers,
                    OutputStream stream) {
        super.get(url, null, headers, stream);
    }

    @Override
    public <T> T post(String url,
                      Map<String, String> params,
                      Map<String, String> headers,
                      Object body,
                      Class<T> responseType) {
        return super.post(url, responseType, params, headers, body);
    }

    @Override
    public <T> T post(String url,
                      Map<String, String> params,
                      Map<String, String> headers,
                      Object body,
                      ParameterizedTypeReference<T> responseType) {
        return super.post(url, responseType, params, headers, body);
    }

    @Override
    public <T> T post(String url,
                      Map<String, String> headers,
                      Object body,
                      ParameterizedTypeReference<T> responseType) {
        return this.post(url, null, headers, body, responseType);
    }

    @Override
    public <T> T post(String url,
                      Map<String, String> headers,
                      Object body,
                      Class<T> responseType) {
        return this.post(url, null, headers, body, responseType);
    }

    @Override
    public void post(String url,
                     Map<String, String> params,
                     Map<String, String> headers,
                     Object body,
                     OutputStream stream) {
        super.post(url, params, headers, body, stream);
    }

    @Override
    public void post(String url,
                     Map<String, String> headers,
                     Object body,
                     OutputStream stream) {
        super.post(url, null, headers, body, stream);
    }

    @Override
    public <T> T put(String url,
                     Map<String, String> params,
                     Map<String, String> headers,
                     Object body,
                     Class<T> responseType) {
        return super.put(url, responseType, params, headers, body);
    }

    @Override
    public <T> T put(String url,
                     Map<String, String> params,
                     Map<String, String> headers,
                     Object body,
                     ParameterizedTypeReference<T> responseType) {
        return super.put(url, responseType, params, headers, body);
    }

    @Override
    public <T> T put(String url,
                     Map<String, String> headers,
                     Object body,
                     Class<T> responseType) {
        return super.put(url, responseType, null, headers, body);
    }

    @Override
    public <T> T put(String url,
                     Map<String, String> headers,
                     Object body,
                     ParameterizedTypeReference<T> responseType) {
        return super.put(url, responseType, null, headers, body);
    }

    @Override
    public <T> T delete(String url,
                        Map<String, String> params,
                        Map<String, String> headers,
                        Class<T> responseType) {
        return super.delete(url, responseType, params, headers);
    }

    @Override
    public <T> T delete(String url,
                        Map<String, String> params,
                        Map<String, String> headers,
                        ParameterizedTypeReference<T> responseType) {
        return super.delete(url, responseType, params, headers);
    }

    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    private boolean isNullOrZero(Integer integer) {
        return integer == null || integer == 0;
    }

    private static RestTemplate createRestTemplate(Integer maxConnections,
                                                   Integer maxConnectionsPerRoute,
                                                   Integer connectionTimeout,
                                                   Integer connectionRequestTimeout,
                                                   Integer connectionResponseTimeout,
                                                   Integer poolConnectionsTimeout,
                                                   boolean sslDisabled,
                                                   boolean bufferBody,
                                                   List<ClientHttpRequestInterceptor> interceptors,
                                                   List<HttpMessageConverter<?>> messageConverters) {
        return RestClientFactory.newRestTemplate(maxConnections, maxConnectionsPerRoute,
                                                 connectionTimeout, connectionRequestTimeout,
                                                 connectionResponseTimeout, poolConnectionsTimeout, sslDisabled, bufferBody,
                                                 interceptors, messageConverters);
    }

    /**
     * @since 0.1.0.0
     */
    private List<HttpMessageConverter<?>> prepareConverters(List<HttpMessageConverter<?>> converters) {
        Set<HttpMessageConverter<?>> convSet = new HashSet<>();

        if (messageConverters != null) {
            convSet.addAll(messageConverters);
        }

        if (converters != null) {
            convSet.addAll(converters);
        }

        backLog.put("Message converters", convSet.size());

        return Lists.newArrayList(convSet);
    }

    /**
     * @since 0.1.0.0
     */
    private List<ClientHttpRequestInterceptor> prepareInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        Set<ClientHttpRequestInterceptor> intSet = new HashSet<>();
        //        intSet.add(new UnconventionalDataConvertInterceptor());
        //        intSet.add(new MDCCustomElementsInterceptor());

        if (interceptors != null) {
            intSet.addAll(interceptors);
        }

        backLog.put("Interceptors", intSet.size());

        return Lists.newArrayList(intSet);
    }

    /**
     * @since 0.8.0
     */
    private Integer calcConnectionTimeout(String connectionTimeoutString) {
        Integer connectionTimeout = isNotBlank(connectionTimeoutString) ? Integer.valueOf(connectionTimeoutString) : null;
        if (isNullOrZero(connectionTimeout)) {
            connectionTimeout = CONNECTION_TIMEOUT;
            log.trace("Custom property for http connection timeout was not found - default value '{}' will be used!",
                      () -> CONNECTION_TIMEOUT);
        }

        backLog.put("Http connection timeout", connectionTimeout);

        return connectionTimeout;
    }

    /**
     * @since 0.8.0
     */
    private Integer calcMaxConnections(String maxConnectionsString) {
        Integer maxConnections = isNotBlank(maxConnectionsString) ? Integer.valueOf(maxConnectionsString) : null;
        if (isNullOrZero(maxConnections)) {
            maxConnections = MAX_CONNECTIONS;
            log.trace("Custom property for http maximum connections was not found - default value '{}' will be used!",
                      () -> MAX_CONNECTIONS);
        }

        backLog.put("Http maximum connections", maxConnections);

        return maxConnections;
    }

    /**
     * @since 0.8.0
     */
    private Integer calcMaxConnectionsPerRoute(String maxConnectionsPerRouteString) {
        Integer maxConnectionsPerRoute =
                isNotBlank(maxConnectionsPerRouteString) ? Integer.valueOf(maxConnectionsPerRouteString) : null;
        if (isNullOrZero(maxConnectionsPerRoute)) {
            maxConnectionsPerRoute = MAX_CONNECTIONS_PER_ROUTE;
            log.trace(
                    "Custom property for http maximum connections per route was not found - default value '{}' will be used!",
                    () -> MAX_CONNECTIONS_PER_ROUTE);
        }

        backLog.put("Http maximum connections", maxConnectionsPerRoute);

        return maxConnectionsPerRoute;
    }

    /**
     * @since 0.8.0
     */
    private Integer calcConnectionRequestTimeout(String connectionRequestTimeoutString) {
        Integer connectionRequestTimeout =
                isNotBlank(connectionRequestTimeoutString) ? Integer.valueOf(connectionRequestTimeoutString) : null;
        if (isNullOrZero(connectionRequestTimeout)) {
            connectionRequestTimeout = CONNECTION_REQUEST_TIMEOUT;
            log.trace(
                    "Custom property for http connection request timeout was not found - default value '{}' will be used!",
                    () -> CONNECTION_REQUEST_TIMEOUT);
        }

        backLog.put("Http connection request", connectionRequestTimeout);

        return connectionRequestTimeout;
    }

    /**
     * @since 0.8.0
     */
    private Integer calcConnectionResponseTimeout(String connectionResponseTimeoutString) {
        Integer connectionResponseTimeout =
                isNotBlank(connectionResponseTimeoutString) ? Integer.valueOf(connectionResponseTimeoutString) : null;
        if (isNullOrZero(connectionResponseTimeout)) {
            connectionResponseTimeout = CONNECTION_RESPONSE_TIMEOUT;
            log.trace(
                    "Custom property for http connection response timeout was not found - default value '{}' will be used!",
                    () -> CONNECTION_RESPONSE_TIMEOUT);
        }

        backLog.put("Http connection response", connectionResponseTimeout);

        return connectionResponseTimeout;
    }

    /**
     * @since 0.8.0
     */
    private Integer calcPoolConnectionsTimeout(String poolConnectionsTimeoutString) {
        Integer poolConnectionsTimeout =
                isNotBlank(poolConnectionsTimeoutString) ? Integer.valueOf(poolConnectionsTimeoutString) : null;
        if (isNullOrZero(poolConnectionsTimeout)) {
            poolConnectionsTimeout = POOL_CONNECTIONS_TIMEOUT;
            log.trace(
                    "Custom property for http pool connections timeout was not found - default value '{}' will be used!",
                    () -> POOL_CONNECTIONS_TIMEOUT);
        }

        backLog.put("Http pool connections", poolConnectionsTimeout);

        return poolConnectionsTimeout;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    public static final Integer CONNECTION_TIMEOUT = 2000;
    public static final Integer MAX_CONNECTIONS = 10;
    public static final Integer MAX_CONNECTIONS_PER_ROUTE = 10;
    public static final Integer CONNECTION_REQUEST_TIMEOUT = 5000;
    public static final Integer CONNECTION_RESPONSE_TIMEOUT = 5000;
    public static final Integer POOL_CONNECTIONS_TIMEOUT = 86400; //one day in seconds

    private String serviceName;
    private Map<String, Integer> backLog = new HashMap<>();

    private List<HttpMessageConverter<?>> messageConverters;

    private String connectionTimeoutName;
    private String maxConnectionsName;
    private String maxConnectionsPerRouteName;
    private String connectionRequestTimeoutName;
    private String connectionResponseTimeoutName;
    private String poolConnectionsTimeoutName;

    @Value("${bot.settings.pool.connections.timeout}")
    public void setPoolConnectionsTimeoutName(String poolConnectionsTimeoutName) {
        this.poolConnectionsTimeoutName = poolConnectionsTimeoutName;
    }

    @Value("${bot.settings.http.connection.response.timeout}")
    public void setConnectionResponseTimeoutName(String connectionResponseTimeoutName) {
        this.connectionResponseTimeoutName = connectionResponseTimeoutName;
    }

    @Value("${bot.settings.http.connection.request.timeout}")
    public void setConnectionRequestTimeoutName(String connectionRequestTimeoutName) {
        this.connectionRequestTimeoutName = connectionRequestTimeoutName;
    }

    @Value("${bot.settings.http.max.connection.per.rout}")
    public void setMaxConnectionsPerRouteName(String maxConnectionsPerRouteName) {
        this.maxConnectionsPerRouteName = maxConnectionsPerRouteName;
    }

    @Value("${bot.settings.http.max.connections}")
    public void setMaxConnectionsName(String maxConnectionsName) {
        this.maxConnectionsName = maxConnectionsName;
    }

    @Value("${bot.settings.http.connection.timeout}")
    public void setConnectionTimeoutName(String connectionTimeoutName) {
        this.connectionTimeoutName = connectionTimeoutName;
    }

    @Autowired
    @Qualifier("createMessageConverters")
    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
        log.debug("Message converters for {}: {}",
                  RestClientImpl.class :: getCanonicalName,
                  () -> messageConverters);
    }

    /**
     * @since 0.8.0
     */
    @Override
    public void setAnchorForLog(String name) {
        RestTemplate restTemplate = super.getRestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        //        interceptors.add(new MDCInterceptor(name));

        restTemplate.setInterceptors(interceptors);

        this.serviceName = name;

        initPropertiesLogging();
    }

    @Override
    public void initPropertiesLogging() {
        log.debug("Additional properties of RestClient for {}: {}",
                  () -> serviceName,
                  () -> backLog);
    }

    @Override
    public void setErrorHandler(ResponseErrorHandler handler) {
        if (handler != null) {
            super.getRestTemplate().setErrorHandler(handler);
        }
    }

    /**
     * To create {@link RestClientImpl}.<br>
     * RestTemplate will be initialized with params from properties (or default params in case of empty properties).
     *
     * @since 0.0.1
     */
    public RestClientImpl() {
    }

    private RestClientImpl(String maxConnections,
                           String maxConnectionsPerRoute,
                           String connectTimeout,
                           String connectionRequestTimeout,
                           String timeoutResponse,
                           String secondsToLive,
                           boolean sslDisabled,
                           boolean bufferBody,
                           List<ClientHttpRequestInterceptor> interceptors,
                           List<HttpMessageConverter<?>> messageConverters) {
        super.setRestTemplate(
                createRestTemplate(
                        calcMaxConnections(maxConnections),
                        calcMaxConnectionsPerRoute(maxConnectionsPerRoute),
                        calcConnectionTimeout(connectTimeout),
                        calcConnectionRequestTimeout(connectionRequestTimeout),
                        calcConnectionResponseTimeout(timeoutResponse),
                        calcPoolConnectionsTimeout(secondsToLive),
                        sslDisabled,
                        bufferBody,
                        prepareInterceptors(interceptors),
                        prepareConverters(messageConverters)));
    }

    @PostConstruct
    public void init() {
        Integer connectionTimeout = calcConnectionTimeout(connectionTimeoutName);
        Integer maxConnections = calcMaxConnections(maxConnectionsName);
        Integer maxConnectionsPerRoute = calcMaxConnectionsPerRoute(maxConnectionsPerRouteName);
        Integer connectionRequestTimeout = calcConnectionRequestTimeout(connectionRequestTimeoutName);
        Integer connectionResponseTimeout = calcConnectionResponseTimeout(connectionResponseTimeoutName);
        Integer poolConnectionsTimeout = calcPoolConnectionsTimeout(poolConnectionsTimeoutName);
        List<ClientHttpRequestInterceptor> interceptors = prepareInterceptors(null);
        List<HttpMessageConverter<?>> converters = prepareConverters(null);

        super.setRestTemplate(createRestTemplate(maxConnections, maxConnectionsPerRoute, connectionTimeout,
                                                 connectionRequestTimeout, connectionResponseTimeout, poolConnectionsTimeout,
                                                 false, true, interceptors, converters));
    }

    // </editor-fold>
}
