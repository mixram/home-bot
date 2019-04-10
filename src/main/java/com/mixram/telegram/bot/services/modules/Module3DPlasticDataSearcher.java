package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.Shop3D;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
public interface Module3DPlasticDataSearcher {

    Data3DPlastic search(Shop3D shop);
}
