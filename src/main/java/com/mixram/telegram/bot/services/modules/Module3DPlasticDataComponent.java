package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author mixram on 2019-04-10.
 * @since 0.1.1.0
 */
@Log4j2
@Component
public class Module3DPlasticDataComponent implements Module3DPlasticDataApplyer {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    //

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    //

    // </editor-fold>


    @Override
    public void apply(Map<Shop3D, Data3DPlastic> plastics) {
        Validate.notNull(plastics, "Plastic data is not specified!");

        log.info("PLASTICS: {}", () -> JsonUtil.toPrettyJson(plastics));

        //TODO: need to realize scheduler logic with plastic info...
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
