package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mixram on 2019-04-10.
 * @since 0.1.2.0
 * @deprecated use {@link DiscountsOn3DPlasticDataV2Component} instead since 0.1.3.0 because of unpredictable behaviour of
 * Spring Cache annotations :-(
 */
@Deprecated
@Log4j2
//@Component
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


    //    @CacheEvict(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, beforeInvocation = true)
    //    @CachePut(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, unless = "#result == null")
    @Deprecated
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
