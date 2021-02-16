package com.mixram.telegram.bot.config.cache;

import com.mixram.telegram.bot.services.domain.entity.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.entity.Message;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.services.services.bot.entity.LazyActionData;
import com.mixram.telegram.bot.services.services.bot.entity.NewMemberTempData;
import com.mixram.telegram.bot.services.services.stat.entity.StatData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

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
    private static final String NEW_MEMBER_PREFIX = "new_member";
    private static final String LAZY_ACTION_PREFIX = "lazy_action";
    private static final String POSTPONED_MESSAGES_PREFIX = "postponed_messages";

    private final String prefix;

    private final RedisTemplate<String, Data3DPlastic> redisTemplate3DPlastic;
    private final RedisTemplate<String, StatData> redisTemplateStatData;
    private final RedisTemplate<String, Map<String, NewMemberTempData>> redisTemplateNewMemberTempData;
    private final RedisTemplate<String, List<LazyActionData>> redisLazyActionData;
    private final RedisTemplate<String, Map<String, List<Message>>> redisTemplatePostponedMessagesData;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public RedisTemplateHelper(
            @Qualifier("data3DPlasticRedisTemplate") RedisTemplate<String, Data3DPlastic> redisTemplate3DPlastic,
            @Qualifier("dataStatDataRedisTemplate") RedisTemplate<String, StatData> redisTemplateStatData,
            @Qualifier("dataNewMemberTempDataRedisTemplate") RedisTemplate<String, Map<String, NewMemberTempData>> redisTemplateNewMemberTempData,
            @Qualifier("dataLazyActionDataRedisTemplate") RedisTemplate<String, List<LazyActionData>> redisLazyActionData,
            @Qualifier("dataPostponedMessagesDataRedisTemplate") RedisTemplate<String, Map<String, List<Message>>> redisTemplatePostponedMessagesData,
            @Value("${spring.redis.prefix}") String prefix) {
        this.redisTemplate3DPlastic = redisTemplate3DPlastic;
        this.redisTemplateStatData = redisTemplateStatData;
        this.redisTemplateNewMemberTempData = redisTemplateNewMemberTempData;
        this.redisLazyActionData = redisLazyActionData;
        this.redisTemplatePostponedMessagesData = redisTemplatePostponedMessagesData;
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
    public void storePlasticToRedis(@Nonnull Data3DPlastic plastic,
                                    @Nonnull Shop3D key) {
        redisTemplate3DPlastic.opsForValue().set(prepareKey(key, PLASTIC_SHOP_PREFIX), plastic);
    }

    /**
     * To delete data about plastic from redis.
     *
     * @param key key to delete with.
     *
     * @since 0.1.3.0
     */
    public void deletePlasticFromRedis(@Nonnull Shop3D key) {
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
    public Data3DPlastic getPlasticFromRedis(@Nonnull Shop3D key) {
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
    public void storeOldPlasticToRedis(@Nonnull Data3DPlastic plastic,
                                       @Nonnull Shop3D key) {
        redisTemplate3DPlastic.opsForValue().set(prepareKey(key, PLASTIC_SHOP_OLD_PREFIX), plastic);
    }

    /**
     * To delete old data about plastic from redis.
     *
     * @param key key to delete with.
     *
     * @since 1.4.1.0
     */
    public void deleteOldPlasticFromRedis(@Nonnull Shop3D key) {
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
    public Data3DPlastic getOldPlasticFromRedis(@Nonnull Shop3D key) {
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
    public void storeStatisticsToRedis(@Nonnull StatData stat,
                                       @Nonnull String key) {
        redisTemplateStatData.opsForValue().set(prepareKey(key, STAT_PREFIX), stat);
    }

    /**
     * To delete statistics from redis.
     *
     * @param key key to delete with.
     *
     * @since 1.3.0.0
     */
    public void deleteStatisticsFromRedis(@Nonnull String key) {
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
    public StatData getStatisticsFromRedis(@Nonnull String key) {
        return redisTemplateStatData.opsForValue().get(prepareKey(key, STAT_PREFIX));
    }

    /**
     * To save new members temporary data in redis.
     *
     * @param data data to save.
     * @param key  key to save with.
     *
     * @since 1.7.0.0
     */
    public void storeNewMembersTempDataToRedis(@Nonnull Map<String, NewMemberTempData> data,
                                               @Nonnull String key) {
        redisTemplateNewMemberTempData.opsForValue().set(prepareKey(key, NEW_MEMBER_PREFIX), data);
    }

    /**
     * To delete new members temporary data from redis.
     *
     * @param key key to delete with.
     *
     * @since 1.3.0.0
     */
    public void deleteNewMembersTempDataFromRedis(@Nonnull String key) {
        redisTemplateNewMemberTempData.delete(prepareKey(key, NEW_MEMBER_PREFIX));
    }

    /**
     * To get new members temporary data from redis.
     *
     * @param key key to get with.
     *
     * @return data or null.
     *
     * @since 1.7.0.0
     */
    public Map<String, NewMemberTempData> getMembersTempDataFromRedis(@Nonnull String key) {
        return redisTemplateNewMemberTempData.opsForValue().get(prepareKey(key, NEW_MEMBER_PREFIX));
    }

    /**
     * To save lazy-action data in redis.
     *
     * @param data data to save.
     * @param key  key to save with.
     *
     * @since 1.8.2.0
     */
    public void storeLazyActionDataToRedis(@Nonnull List<LazyActionData> data,
                                           @Nonnull String key) {
        redisLazyActionData.opsForValue().set(prepareKey(key, LAZY_ACTION_PREFIX), data);
    }

    /**
     * To delete lazy-action data from redis.
     *
     * @param key key to delete with.
     *
     * @since 1.8.2.0
     */
    public void deleteLazyActionDataFromRedis(@Nonnull String key) {
        redisLazyActionData.delete(prepareKey(key, LAZY_ACTION_PREFIX));
    }

    /**
     * To get lazy-action data from redis.
     *
     * @param key key to get with.
     *
     * @return data or null.
     *
     * @since 1.8.2.0
     */
    public List<LazyActionData> getLazyActionDataFromRedis(@Nonnull String key) {
        return redisLazyActionData.opsForValue().get(prepareKey(key, LAZY_ACTION_PREFIX));
    }

    /**
     * To get postponed messages data from redis.
     *
     * @param key key to get with.
     *
     * @return data or null.
     *
     * @since 1.8.8.0
     */
    public Map<String, List<Message>> getPostponedMessagesDataFromRedis(@Nonnull String key) {
        return redisTemplatePostponedMessagesData.opsForValue().get(prepareKey(key, POSTPONED_MESSAGES_PREFIX));
    }

    /**
     * To save postponed messaged to Redis.
     *
     * @param data data to save.
     * @param key  key part.
     *
     * @since 1.8.8.0
     */
    public void storePostponedMessagesDataToRedis(@Nonnull Map<String, List<Message>> data,
                                                  @Nonnull String key) {
        redisTemplatePostponedMessagesData.opsForValue().set(prepareKey(key, POSTPONED_MESSAGES_PREFIX), data);
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
