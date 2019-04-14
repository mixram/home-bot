package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.config.cache.RedisTemplateHelper;
import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@Log4j2
@Component
public class DiscountsOn3DPlasticDataCacheV2Component implements Module3DPlasticDataSearcher {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final RedisTemplateHelper redisTemplate;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsOn3DPlasticDataCacheV2Component(RedisTemplateHelper redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // </editor-fold>


    @Override
    public Data3DPlastic search(Shop3D shop) {
        return redisTemplate.getPlasticFromRedis(shop);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
