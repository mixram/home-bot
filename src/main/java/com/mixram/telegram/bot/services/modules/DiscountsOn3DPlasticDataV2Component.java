package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.config.cache.RedisTemplateHelper;
import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author mixram on 2019-04-10.
 * @since 0.1.2.0
 */
@Primary
@Log4j2
@Component
public class DiscountsOn3DPlasticDataV2Component implements Module3DPlasticDataSearcher {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final DiscountsOn3DPlasticService service;
    private final RedisTemplateHelper redisTemplate;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsOn3DPlasticDataV2Component(DiscountsOn3DPlasticService service,
                                               RedisTemplateHelper redisTemplate) {
        this.service = service;
        this.redisTemplate = redisTemplate;
    }

    // </editor-fold>


    @Override
    public Data3DPlastic search(Shop3D shop) {
        Validate.notNull(shop, "Shop is not specified!");

        Data3DPlastic result = service.search(shop);
        log.info("DISCOUNTS: {}", () -> JsonUtil.toPrettyJson(result));

        redisTemplate.deletePlasticFromRedis(shop);
        if (result != null) {
            redisTemplate.storePlasticToRedis(result, shop);
        }

        //TODO: need to store data into DB (+cache)

        return result;
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
