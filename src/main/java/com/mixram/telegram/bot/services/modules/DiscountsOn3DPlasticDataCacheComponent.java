package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.config.CacheCustomConfig;
import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@Log4j2
@CacheConfig(cacheManager = CacheCustomConfig.REDIS_CACHE_MANAGER)
@Component
public class DiscountsOn3DPlasticDataCacheComponent implements Module3DPlasticDataSearcher {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    //

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    //

    // </editor-fold>


    @Cacheable(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, unless = "#result == null", key = "#p0")
    @Override
    public Data3DPlastic search(Shop3D shop) {
        return null;
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
