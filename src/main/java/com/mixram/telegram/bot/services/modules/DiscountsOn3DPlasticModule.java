package com.mixram.telegram.bot.services.modules;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
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
import java.util.function.Supplier;

/**
 * @author mixram on 2019-03-29.
 * @since 0.1.1.0
 */
@Log4j2
@Service
public class DiscountsOn3DPlasticModule implements Module {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String MODULE_START_MESSAGE_ADMIN = "Start discounts data update";
    private static final String MODULE_FINISH_MESSAGE_ADMIN = "Discounts data update has finished";

    private final Module3DPlasticDataSearcher searcher;
    private final Module3DPlasticDataApplyer applyer;
    private final AsyncHelper asyncHelper;
    private final TelegramAPICommunicationComponent communicationComponent;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public DiscountsOn3DPlasticModule(Module3DPlasticDataSearcher searcher,
                                      @Qualifier("module3DPlasticDataComponent") Module3DPlasticDataApplyer applyer,
                                      AsyncHelper asyncHelper,
                                      TelegramAPICommunicationComponent communicationComponent) {
        this.searcher = searcher;
        this.applyer = applyer;
        this.asyncHelper = asyncHelper;
        this.communicationComponent = communicationComponent;
    }

    // </editor-fold>


    @Override
    public void execute() {
        log.info("{} is started!", DiscountsOn3DPlasticModule.class :: getSimpleName);

        StopWatch sw = new StopWatch();

        sw.start("Message to admin");
        sendMessageToAdmin(prepareStartMessage());
        sw.stop();

        sw.start("Parse data");
        Map<Shop3D, Data3DPlastic> plastics = getPlastics();
        //        Map<Shop3D, Data3DPlastic> plastics = getPlasticsAsync();
        sw.stop();

        sw.start("Apply data");
        applyer.apply(plastics);
        sw.stop();

        String swString = sw.prettyPrint();

        sendMessageToAdmin(prepareFinishMessage(swString));

        log.debug("\n{}", () -> DiscountsOn3DPlasticModule.class.getSimpleName() + "#execute: " + swString);
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.2.1.0
     */
    private Map<Shop3D, Data3DPlastic> getPlasticsAsync() {
        Map<Shop3D, Data3DPlastic> plastics = new HashMap<>(Shop3D.values().length);

        Map<Shop3D, CompletableFuture<Data3DPlastic>> plasticsFromFuture = new HashMap<>(Shop3D.values().length);
        for (Shop3D shop : Shop3D.values()) {
            plasticsFromFuture.put(shop,
                                   ConcurrentUtilites.supplyAsyncWithLocalThreadContext(aVoid -> searcher.search(shop)));
        }
        for (Map.Entry<Shop3D, CompletableFuture<Data3DPlastic>> futureEntry : plasticsFromFuture.entrySet()) {
            plastics.put(futureEntry.getKey(), futureEntry.getValue().join());
        }

        return plastics;
    }

    /**
     * @since 1.2.1.0
     */
    private Map<Shop3D, Data3DPlastic> getPlastics() {
        Map<Shop3D, Data3DPlastic> plastics = new HashMap<>(Shop3D.values().length);

        for (Shop3D shop : Shop3D.values()) {
            plastics.put(shop, searcher.search(shop));
        }

        return plastics;
    }

    /**
     * @since 1.0.0.0
     */
    private void sendMessageToAdmin(String message) {
        asyncHelper.doAsync((Supplier<Void>) () -> {
            communicationComponent.sendMessageToAdmin(message);

            return null;
        });
    }

    /**
     * @since 1.0.0.0
     */
    private String prepareStartMessage() {
        return new StringBuilder()
                .append("🔥🔥🔥").append("\n")
                .append("<b>")
                .append(DiscountsOn3DPlasticModule.class.getSimpleName()).append("\n")
                .append(MODULE_START_MESSAGE_ADMIN)
                .append("</b>").append("\n")
                .append("🔥🔥🔥")
                .toString();
    }

    /**
     * @since 1.0.0.0
     */
    private String prepareFinishMessage(String swData) {
        return new StringBuilder()
                .append("💥💥💥").append("\n")
                .append("<b>")
                .append(DiscountsOn3DPlasticModule.class.getSimpleName()).append("\n")
                .append(MODULE_FINISH_MESSAGE_ADMIN).append("\n")
                .append("</b>")
                .append(swData.replaceAll("-----------------------------------------",
                                          "---------------------------------------"))
                .append("💥💥💥")
                .toString();
    }

    // </editor-fold>

}
