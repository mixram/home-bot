package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import lombok.extern.log4j.Log4j2;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 * @deprecated use {@link DiscountsOn3DPlasticDataV2Component} instead since 0.1.3.0 because of unpredictable behaviour of
 * * Spring Cache annotations :-(
 */
@Deprecated
@Log4j2
//@Component
public class DiscountsOn3DPlasticDataCacheComponent implements Module3DPlasticDataSearcher {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    //

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    //

    // </editor-fold>


    @Deprecated
    //    @Cacheable(cacheNames = CacheCustomConfig.PLASTIC_3D_DATA_CACHE, unless = "#result == null")
    @Override
    public Data3DPlastic search(Shop3D shop) {
        return null;
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
