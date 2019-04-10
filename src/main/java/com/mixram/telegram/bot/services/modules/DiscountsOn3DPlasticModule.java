package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.Shop3D;
import com.mixram.telegram.bot.utils.AsyncHelper;
import com.mixram.telegram.bot.utils.ConcurrentUtilites;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@Log4j2
@Service
public class DiscountsOn3DPlasticModule implements Module {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private final Module3DPlasticDataSearcher searcher;
    private final Module3DPlasticDataApplyer applyer;
    private final AsyncHelper asyncHelper;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsOn3DPlasticModule(@Qualifier("discountsOn3DPlasticDataComponent") Module3DPlasticDataSearcher searcher,
                                      @Qualifier("module3DPlasticDataComponent") Module3DPlasticDataApplyer applyer,
                                      AsyncHelper asyncHelper) {
        this.searcher = searcher;
        this.applyer = applyer;
        this.asyncHelper = asyncHelper;
    }

    // </editor-fold>


    @Override
    public void execute() {
        log.info("{} is started!", DiscountsOn3DPlasticModule.class :: getSimpleName);

        StopWatch sw = new StopWatch();

        sw.start("Parse data");
        Map<Shop3D, Data3DPlastic> plastics = new HashMap<>(Shop3D.values().length);
        Map<Shop3D, CompletableFuture<Data3DPlastic>> plasticsFromFuture = new HashMap<>(Shop3D.values().length);
        for (Shop3D value : Shop3D.values()) {
            plasticsFromFuture.put(value,
                                   ConcurrentUtilites.supplyAsyncWithLocalThreadContext(aVoid -> searcher.search(value)));
        }
        for (Map.Entry<Shop3D, CompletableFuture<Data3DPlastic>> futureEntry : plasticsFromFuture.entrySet()) {
            plastics.put(futureEntry.getKey(), futureEntry.getValue().join());
        }
        sw.stop();

        sw.start("Apply data");
        applyer.apply(plastics);
        sw.stop();

        log.debug("\n{}#execute: {}",
                  DiscountsOn3DPlasticModule.class :: getSimpleName,
                  sw :: prettyPrint);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    //

    // </editor-fold>

}
