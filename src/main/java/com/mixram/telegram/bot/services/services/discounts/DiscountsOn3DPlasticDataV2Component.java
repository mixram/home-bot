package com.mixram.telegram.bot.services.services.discounts;

import com.mixram.telegram.bot.config.cache.RedisTemplateHelper;
import com.mixram.telegram.bot.services.domain.entity.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.services.modules.Module3DPlasticDataSearcher;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final DiscountsOnPlasticService d3DPlastService;
    private final DiscountsOnPlasticService d3DUAService;
    private final DiscountsOnPlasticService dMonoService;
    private final DiscountsOnPlasticService dU3DFService;
    private final RedisTemplateHelper redisTemplate;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsOn3DPlasticDataV2Component(
            @Qualifier("discountsOn3DPlastic3DPlastService") DiscountsOnPlasticService d3DPlastService,
            @Qualifier("discountsOn3DPlastic3DUAService") DiscountsOnPlasticService d3DUAService,
            @Qualifier("discountsOn3DPlasticMonofilamentService") DiscountsOnPlasticService dMonoService,
            @Qualifier("discountsOn3DPlasticU3DFService") DiscountsOnPlasticService dU3DFService,
            RedisTemplateHelper redisTemplate) {
        this.d3DPlastService = d3DPlastService;
        this.d3DUAService = d3DUAService;
        this.dMonoService = dMonoService;
        this.dU3DFService = dU3DFService;
        this.redisTemplate = redisTemplate;
    }

    // </editor-fold>


    @Override
    public Data3DPlastic search(Shop3D shop) {
        Validate.notNull(shop, "Shop is not specified!");

        Data3DPlastic result = doSearch(shop);
        log.info("DISCOUNTS: {}", () -> JsonUtil.toPrettyJson(result));

        redisTemplate.deletePlasticFromRedis(shop);
        if (result != null) {
            result.setShop(shop);
            redisTemplate.storePlasticToRedis(result, shop);
        }

        //TODO: need to store data into DB (+cache)

        return result;
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 0.2.0.0
     */
    private Data3DPlastic doSearch(Shop3D shop) {
        switch (shop) {
            case SHOP_3DPLAST:
                return d3DPlastService.search();
            case SHOP_3DUA:
                return d3DUAService.search();
            case SHOP_MONOFILAMENT:
                return dMonoService.search();
            case SHOP_U3DF:
                return dU3DFService.search();
            //            case SHOP_DASPLAST:
            //            case SHOP_PLEXIWIRE:
            //                //                throw new UnsupportedOperationException(String.format("The shop '%s' is has not been realized yet!", shop));
            //                log.info("The shop {} is has not been realized yet!", () -> shop);
            //                return null;
            default:
                throw new UnsupportedOperationException(String.format("Unexpected shop: %s!", shop));
        }
    }

    // </editor-fold>
}
