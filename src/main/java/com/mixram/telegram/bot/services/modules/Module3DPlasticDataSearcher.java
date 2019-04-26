package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.entity.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
public interface Module3DPlasticDataSearcher {

    Data3DPlastic search(Shop3D shop);

    Data3DPlastic searchOld(Shop3D shop);
}
