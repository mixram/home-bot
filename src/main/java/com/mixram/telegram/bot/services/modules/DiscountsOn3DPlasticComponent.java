package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.config.CacheCustomConfig;
import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.Shop3D;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author mixram on 2019-03-29.
 * @since 0.2.0.0
 */
@CacheConfig(cacheManager = CacheCustomConfig.REDIS_CACHE_MANAGER)
@Component
public class DiscountsOn3DPlasticComponent implements Module3DPlasticDataSearcher {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final DiscountsOn3DPlasticService service;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsOn3DPlasticComponent(DiscountsOn3DPlasticService service) {
        this.service = service;
    }

    // </editor-fold>


    @Cacheable(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, unless = "#result == null")
    @Override
    public Data3DPlastic search(Shop3D shop) {
        Validate.notNull(shop, "Shop is not specified!");

        return service.search(shop);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
