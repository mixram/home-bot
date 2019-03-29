package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.Shop3D;
import com.mixram.telegram.bot.services.services.Bot3DLongPooling;
import com.mixram.telegram.bot.utils.AsyncHelper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * @author mixram on 2019-03-29.
 * @since 0.2.0.0
 */
@Log4j2
@Service
public class DiscountsOn3DPlasticModule implements Module {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final Module3DPlasticDataSearcher searcher;
    private final AsyncHelper asyncHelper;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsOn3DPlasticModule(@Qualifier("discountsOn3DPlasticComponent") Module3DPlasticDataSearcher searcher,
                                      AsyncHelper asyncHelper) {
        this.searcher = searcher;
        this.asyncHelper = asyncHelper;
    }

    // </editor-fold>


    @Override
    public void execute() {
        log.info("{} is started!", Bot3DLongPooling.class :: getSimpleName);

        for (Shop3D value : Shop3D.values()) {
            asyncHelper.doAsync((Supplier<Void>) () -> {
                searcher.search(value);

                return null;
            });
        }

    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
