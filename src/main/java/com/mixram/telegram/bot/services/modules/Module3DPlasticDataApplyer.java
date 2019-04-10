package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.Shop3D;

import java.util.Map;

/**
 * @author mixram on 2019-04-10.
 * @since 0.1.1.0
 */
public interface Module3DPlasticDataApplyer {

    void apply(Map<Shop3D, Data3DPlastic> plastics);
}
