package com.mixram.telegram.bot.config.cache;

import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@Slf4j
//@Configuration
//@EnableCaching(proxyTargetClass = true/*, mode = AdviceMode.ASPECTJ*/)
public class CacheCustomConfig extends CachingConfigurerSupport {

    public static final String REDIS_CACHE_MANAGER = "redisCacheManager";
    //    public static final String GUAVA_CACHE_MANAGER = "guavaCacheManager";

    public static final String PLASTIC_3D_DATA_CACHE = "3d_plastic_data";

    private final List<String> cacheNames = new ArrayList<>();

    private final RedisSerializer<Object> genericJackson2JsonRedisSerializer;

    @Autowired
    public CacheCustomConfig(
            @Qualifier("genericJackson2JsonRedisSerializer") RedisSerializer<Object> genericJackson2JsonRedisSerializer) {
        this.genericJackson2JsonRedisSerializer = genericJackson2JsonRedisSerializer;
    }

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

        RedisSerializationContext.SerializationPair<Object> serializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                                                                                 .disableCachingNullValues()
                                                                                 .entryTtl(Duration.ZERO)
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
    //        return (o, method, os) -> o.getClass().getSimpleName() + "_"
    //                                  + method.getName() + "_"
    //                                  + StringUtils.arrayToDelimitedString(os, "_");
    //    }
}
