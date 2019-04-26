package com.mixram.telegram.bot.config.cache;

import com.mixram.telegram.bot.services.domain.entity.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.services.services.stat.entity.StatData;
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
    private static final String PLASTIC_SHOP_OLD_PREFIX = "plastic_shop_old";
    private static final String STAT_PREFIX = "statistics";

    private final String prefix;

    private final RedisTemplate<String, Data3DPlastic> redisTemplate3DPlastic;
    private final RedisTemplate<String, StatData> redisTemplateStatData;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public RedisTemplateHelper(@Qualifier("data3DPlasticRedisTemplate") RedisTemplate<String, Data3DPlastic> redisTemplate3DPlastic,
                               @Qualifier("dataStatDataRedisTemplate") RedisTemplate<String, StatData> redisTemplateStatData,
                               @Value("${spring.redis.prefix}") String prefix) {
        this.redisTemplate3DPlastic = redisTemplate3DPlastic;
        this.redisTemplateStatData = redisTemplateStatData;
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
        redisTemplate3DPlastic.opsForValue().set(prepareKey(key, PLASTIC_SHOP_PREFIX), plastic);
    }

    /**
     * To delete data about plastic from redis.
     *
     * @param key key to delete with.
     *
     * @since 0.1.3.0
     */
    public void deletePlasticFromRedis(Shop3D key) {
        redisTemplate3DPlastic.delete(prepareKey(key, PLASTIC_SHOP_PREFIX));
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
        return redisTemplate3DPlastic.opsForValue().get(prepareKey(key, PLASTIC_SHOP_PREFIX));
    }


    /**
     * To save old data about plastic in redis.
     *
     * @param plastic plastic data to save.
     * @param key     key to save with.
     *
     * @since 1.4.1.0
     */
    public void storeOldPlasticToRedis(Data3DPlastic plastic,
                                       Shop3D key) {
        redisTemplate3DPlastic.opsForValue().set(prepareKey(key, PLASTIC_SHOP_OLD_PREFIX), plastic);
    }

    /**
     * To delete old data about plastic from redis.
     *
     * @param key key to delete with.
     *
     * @since 1.4.1.0
     */
    public void deleteOldPlasticFromRedis(Shop3D key) {
        redisTemplate3DPlastic.delete(prepareKey(key, PLASTIC_SHOP_OLD_PREFIX));
    }

    /**
     * To get old data about plastic from redis.
     *
     * @param key key to get with.
     *
     * @return data or null.
     *
     * @since 1.4.1.0
     */
    public Data3DPlastic getOldPlasticFromRedis(Shop3D key) {
        return redisTemplate3DPlastic.opsForValue().get(prepareKey(key, PLASTIC_SHOP_OLD_PREFIX));
    }


    /**
     * To save statistics in redis.
     *
     * @param stat statistics to save.
     * @param key  key to save with.
     *
     * @since 1.3.0.0
     */
    public void storeStatisticsToRedis(StatData stat,
                                       String key) {
        redisTemplateStatData.opsForValue().set(prepareKey(key, STAT_PREFIX), stat);
    }

    /**
     * To delete statistics from redis.
     *
     * @param key key to delete with.
     *
     * @since 1.3.0.0
     */
    public void deleteStatisticsFromRedis(String key) {
        redisTemplateStatData.delete(prepareKey(key, STAT_PREFIX));
    }

    /**
     * To get statistics from redis.
     *
     * @param key key to get with.
     *
     * @return data or null.
     *
     * @since 1.3.0.0
     */
    public StatData getStatisticsFromRedis(String key) {
        return redisTemplateStatData.opsForValue().get(prepareKey(key, STAT_PREFIX));
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 0.1.3.0
     */
    private String prepareKey(Object key,
                              String prefixCustom) {
        return prefix + "::" + prefixCustom + "::" + key;
    }

    // </editor-fold>
}
