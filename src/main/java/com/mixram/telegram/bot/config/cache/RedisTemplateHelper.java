package com.mixram.telegram.bot.config.cache;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author mixram on 2019-04-14.
 * @since 0.1.3.0
 */
@Component
public class RedisTemplateHelper {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String PLASTIC_SHOP_PREFIX = "plastic_shop";

    private final String prefix;

    private final RedisTemplate<String, Data3DPlastic> redisTemplate;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public RedisTemplateHelper(@Qualifier("data3DPlasticRedisTemplate") RedisTemplate<String, Data3DPlastic> redisTemplate,
                               @Value("${spring.redis.prefix}") String prefix) {
        this.redisTemplate = redisTemplate;
        this.prefix = prefix;
    }

    // </editor-fold>


    /**
     * To save data about plastic in redis.
     *
     * @param plastic plastic data to save.
     * @param key     key to save with.
     *
     * @since 0.1.3.0
     */
    public void storePlasticToRedis(Data3DPlastic plastic,
                                    Shop3D key) {
        redisTemplate.opsForValue().set(prepareKey(key), plastic);
    }

    /**
     * To delete data about plastic from redis.
     *
     * @param key key to delete with.
     *
     * @since 0.1.3.0
     */
    public void deletePlasticFromRedis(Shop3D key) {
        redisTemplate.delete(prepareKey(key));
    }

    /**
     * To get data about plastic from redis.
     *
     * @param key key to get with.
     *
     * @return data or null.
     *
     * @since 0.1.3.0
     */
    public Data3DPlastic getPlasticFromRedis(Shop3D key) {
        return redisTemplate.opsForValue().get(prepareKey(key));
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 0.1.3.0
     */
    private String prepareKey(Shop3D key) {
        return prefix + "::" + PLASTIC_SHOP_PREFIX + "::" + key;
    }

    // </editor-fold>
}
