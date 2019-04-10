package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.config.CacheCustomConfig;
import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.Shop3D;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

/**
 * @author mixram on 2019-04-10.
 * @since 0.1.2.0
 */
@Log4j2
@Component
public class DiscountsOn3DPlasticDataComponent implements Module3DPlasticDataSearcher {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final DiscountsOn3DPlasticService service;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsOn3DPlasticDataComponent(DiscountsOn3DPlasticService service) {
        this.service = service;
    }

    // </editor-fold>


    @CacheEvict(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, key = "#p0", beforeInvocation = true)
    @CachePut(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, unless = "#result == null", key = "#p0")
    @Override
    public Data3DPlastic search(Shop3D shop) {
        Validate.notNull(shop, "Shop is not specified!");

        Data3DPlastic result = service.search(shop);
        log.info("DISCOUNTS: {}", () -> JsonUtil.toPrettyJson(result));

        //TODO: need to store data into DB (+cache)

        return result;
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
