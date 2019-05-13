package com.mixram.telegram.bot.services.modules;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mixram.telegram.bot.services.domain.entity.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.services.services.bot.entity.MessageData;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
import com.mixram.telegram.bot.utils.AsyncHelper;
import com.mixram.telegram.bot.utils.CustomMessageSource;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import com.mixram.telegram.bot.utils.htmlparser.entity.ParseData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author mixram on 2019-04-10.
 * @since 0.1.1.0
 */
@Log4j2
@Component
public class Plastic3dDataComponent implements PlasticApplier {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String BROKEN_URL_MESSAGE = "telegram.bot.message.broken-url";

    private final AsyncHelper asyncHelper;
    private final TelegramAPICommunicationComponent communicationComponent;
    private final CustomMessageSource messageSource;
    private final Module3DPlasticDataSearcher searcher;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public Plastic3dDataComponent(AsyncHelper asyncHelper,
                                  TelegramAPICommunicationComponent communicationComponent,
                                  CustomMessageSource messageSource,
                                  @Qualifier("discountsOn3DPlasticDataCacheComponent") Module3DPlasticDataSearcher searcher) {
        this.asyncHelper = asyncHelper;
        this.communicationComponent = communicationComponent;
        this.messageSource = messageSource;
        this.searcher = searcher;
    }


    // </editor-fold>


    @Override
    public void apply() {
        Map<Shop3D, Data3DPlastic> plastics = getPlastics();
        log.info("PLASTICS: {}", () -> JsonUtil.toPrettyJson(plastics));

        List<String> messages = prepareBrokenUrlMessage(plastics);
        if (!CollectionUtils.isEmpty(messages)) {
            messages.forEach(this :: sendMessageToAdmin);
        }

        //TODO: need to realize scheduler logic with plastic info...
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.4.1.0
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
    private List<String> prepareBrokenUrlMessage(Map<Shop3D, Data3DPlastic> plastics) {
        List<String> urls = Optional.ofNullable(plastics).orElse(Maps.newHashMapWithExpectedSize(0)).values().stream()
                                    .map(Data3DPlastic :: getBrokenUrls)
                                    .flatMap(List :: stream)
                                    .map(ParseData :: getCommonUrl)
                                    .collect(Collectors.toList());

        if (urls.isEmpty()) {
            return Lists.newArrayListWithExpectedSize(0);
        }

        StringBuilder builder = new StringBuilder();
        urls.forEach(u -> builder.append(u).append("\n"));

        String message = messageSource.getMessage(BROKEN_URL_MESSAGE, Locale.ENGLISH,
                                                  DiscountsOn3DPlasticModule.class.getSimpleName(), builder.toString());

        return Lists.newArrayList(message);
    }

    /**
     * @since 1.0.0.0
     */
    private void sendMessageToAdmin(String message) {
        asyncHelper.doAsync((Supplier<Void>) () -> {
            MessageData messageData = MessageData.builder()
                                                 .toResponse(true)
                                                 .userResponse(true)
                                                 .message(message)
                                                 .build();
            communicationComponent.sendMessageToAdmin(messageData);

            return null;
        });
    }

    // </editor-fold>

}
