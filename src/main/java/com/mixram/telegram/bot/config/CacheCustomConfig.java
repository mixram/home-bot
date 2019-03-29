package com.mixram.telegram.bot.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mixram on 2019-03-29.
 * @since 0.2.0.0
 */
@Slf4j
@Configuration
@EnableCaching(proxyTargetClass = true) //(mode = AdviceMode.ASPECTJ)
public class CacheCustomConfig extends CachingConfigurerSupport {

    public static final String REDIS_CACHE_MANAGER = "redisCacheManager";
    //    public static final String GUAVA_CACHE_MANAGER = "guavaCacheManager";

    public static final String PLASTIC_3D_DATA_CACHE = "3d_plastic_data";

    private final List<String> cacheNames = new ArrayList<>();

    @PostConstruct
    public void init() {
        log.info("Starting creating holders for cache names and caches expiration");

        cacheNames.add(PLASTIC_3D_DATA_CACHE);

        log.info("Have finished creating holders for cache names ({}).", cacheNames.size());
    }

    @Primary
    @Bean(name = REDIS_CACHE_MANAGER)
    public CacheManager getRedisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        log.info("Start creating RedisCacheManager...");

        ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
                .failOnEmptyBeans(false)
                .build();
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);
        RedisSerializationContext.SerializationPair<Object> serializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(serializer);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                                                                                 .disableCachingNullValues()
                                                                                 .entryTtl(Duration.ofMinutes(5))
                                                                                 .serializeValuesWith(serializationPair);
        redisCacheConfiguration.usePrefix();

        RedisCacheManager cacheManager = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();
        cacheManager.afterPropertiesSet();

        cacheNames.forEach(cacheManager :: getCache);    // инициализируем кеши

        log.info("Have finished creating RedisCacheManager for caches: {}.",
                 JsonUtil.toJson(cacheManager.getCacheNames()));

        return cacheManager;
    }

    //    @Bean(name = GUAVA_CACHE_MANAGER)
    //    public CacheManager getGuavaCacheManager() {
    //        GuavaCacheManager guavaCacheManager = new GuavaCacheManager();
    //        guavaCacheManager.setCacheSpecification("maximumSize=500,expireAfterWrite=600s");
    //        return guavaCacheManager;
    //    }

    //    @Override
    //    public KeyGenerator keyGenerator() {
    //        return new KeyGenerator() {
    //
    //            @Override
    //            public Object generate(Object o,
    //                                   Method method,
    //                                   Object... os) {
    //                Object key = null;
    //                for (Object obj : os) {
    //                    if (obj instanceof Keyable) {
    //                        key = ((Keyable) obj).getKeyProperty();
    //                    }
    //                }
    //
    //                if (key == null) {
    //                    throw new IllegalArgumentException(
    //                            String.format("The instance to cache in redis must implement %s interface!",
    //                                          Keyable.class.getSimpleName()));
    //                }
    //
    //                return key;
    //            }
    //        };
    //    }
}
