package com.mixram.telegram.bot.services.modules;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
import com.mixram.telegram.bot.utils.AsyncHelper;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import com.mixram.telegram.bot.utils.htmlparser.ParseData;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author mixram on 2019-04-10.
 * @since 0.1.1.0
 */
@Log4j2
@Component
public class Module3DPlasticDataComponent implements Module3DPlasticDataApplyer {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String BROKEN_URLS_MESSAGE_ADMIN = "\"Broken\" links have found!";

    private final AsyncHelper asyncHelper;
    private final TelegramAPICommunicationComponent communicationComponent;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public Module3DPlasticDataComponent(AsyncHelper asyncHelper,
                                        TelegramAPICommunicationComponent communicationComponent) {
        this.asyncHelper = asyncHelper;
        this.communicationComponent = communicationComponent;
    }


    // </editor-fold>


    @Override
    public void apply(Map<Shop3D, Data3DPlastic> plastics) {
        Validate.notNull(plastics, "Plastic data is not specified!");

        log.info("PLASTICS: {}", () -> JsonUtil.toPrettyJson(plastics));

        List<String> messages = prepareBrokenUrlMessage(plastics);
        if (!CollectionUtils.isEmpty(messages)) {
            messages.forEach(this :: sendMessageToAdmin);
        }

        //TODO: need to realize scheduler logic with plastic info...
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.0.0.0
     */
    private List<String> prepareBrokenUrlMessage(Map<Shop3D, Data3DPlastic> plastics) {
        List<String> urls = Optional.ofNullable(plastics).orElse(Maps.newHashMapWithExpectedSize(0)).values().stream()
                                    .map(Data3DPlastic :: getBrokenUrls)
                                    .flatMap(List :: stream)
                                    .map(ParseData :: getProductUrl)
                                    .collect(Collectors.toList());

        if (urls.isEmpty()) {
            return Lists.newArrayListWithExpectedSize(0);
        }

        StringBuilder builder = new StringBuilder()
                .append("❗❗❗️").append("\n")
                .append("<b>")
                .append(DiscountsOn3DPlasticModule.class.getSimpleName()).append("\n")
                .append(BROKEN_URLS_MESSAGE_ADMIN)
                .append("</b>").append("\n");

        urls.forEach(u -> builder.append(u).append("\n"));
        builder.append("❗❗❗️");

        return Lists.newArrayList(builder.toString());
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

    // </editor-fold>

}
