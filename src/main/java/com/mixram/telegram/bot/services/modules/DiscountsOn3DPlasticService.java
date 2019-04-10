package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.Shop3D;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mixram on 2019-03-29.
 * @since 0.2.0.0
 */
@Log4j2
@Service
class DiscountsOn3DPlasticService {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final DiscountsOn3DPlastic3DPlastService d3DPlastService;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsOn3DPlasticService(DiscountsOn3DPlastic3DPlastService d3DPlastService) {
        this.d3DPlastService = d3DPlastService;
    }

    // </editor-fold>


    /**
     * To search for discounts by shop.
     *
     * @param shop shop to search discount in.
     *
     * @return data about discounts or exception.
     *
     * @since 0.1.0.0
     */
    protected Data3DPlastic search(Shop3D shop) {
        switch (shop) {
            case SHOP_3DPLAST:
                return d3DPlastService.search();
            case SHOP_3DUA:
            case SHOP_U3DF:
            case SHOP_DASPLAST:
            case SHOP_PLEXIWIRE:
            case SHOP_MONOFILAMENT:
                //                throw new UnsupportedOperationException(String.format("The shop '%s' is has not been realized yet!", shop));
                log.info("The shop {} is has not been realized yet!", () -> shop);
                return null;
            default:
                throw new UnsupportedOperationException(String.format("Unexpected shop: %s!", shop));
        }
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>
}
