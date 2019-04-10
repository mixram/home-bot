package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.Shop3D;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author mixram on 2019-04-10.
 * @since 0.2.0.0
 */
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

        //
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
