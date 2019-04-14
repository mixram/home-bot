package com.mixram.telegram.bot;

import com.mixram.telegram.bot.config.cache.CacheCustomConfig;
import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.utils.htmlparser.ParseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author mixram on 2019-04-14.
 * @since ...
 */
@Service
public class TestCacheService {

    private final Random random;
    private final RedisTemplate<String, Data3DPlastic> plast2RedisTemplate;

    @Autowired
    public TestCacheService(@Qualifier("data3DPlasticRedisTemplate") RedisTemplate<String, Data3DPlastic> plast2RedisTemplate) {
        this.plast2RedisTemplate = plast2RedisTemplate;
        this.random = new Random();
    }


    public void storePlasticToRedisManually2(Data3DPlastic plastic,
                                             Shop3D key) {
        plast2RedisTemplate.opsForValue().set(key.toString(), plastic);
    }

    public Data3DPlastic getPlasticFromRedisManually2(Shop3D key) {
        return plast2RedisTemplate.opsForValue().get(key.toString());
    }

    @CacheEvict(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, beforeInvocation = true)
    public void storePlasticToRedisManuallyEvict3(String key) {
        //
    }

    @CachePut(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, unless = "#result == null")
    public Data3DPlastic storePlasticToRedisManuallyPut3(String key) {
        List<ParseData> list = new ArrayList<>(1);
        list.add(ParseData.builder()
                          .pageTitle(String.valueOf(random.nextInt()))
                          .productName("222")
                          .productOldPrice(new BigDecimal("20.45"))
                          .productSalePrice(new BigDecimal("100.24"))
                          .build());

        return Data3DPlastic.builder()
                            .data(list)
                            .build();
    }

    @Cacheable(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, unless = "#result == null")
    public Data3DPlastic getPlasticFromRedisManually3(String key) {
        return null;
    }

    @CacheEvict(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, beforeInvocation = true)
    @CachePut(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, unless = "#result == null")
    public Data3DPlastic storePlasticToRedisManually4(Shop3D key) {
        List<ParseData> list = new ArrayList<>(1);
        list.add(ParseData.builder()
                          .pageTitle(String.valueOf(random.nextInt()))
                          .productName("222")
                          .productOldPrice(new BigDecimal("20.45"))
                          .productSalePrice(new BigDecimal("100.24"))
                          .build());

        return Data3DPlastic.builder()
                            .data(list)
                            .build();
    }

    @Cacheable(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, unless = "#result == null")
    public Data3DPlastic getPlasticFromRedisManually4(Shop3D key) {
        return null;
    }
}
