package com.mixram.telegram.bot.services.services;

import com.mixram.telegram.bot.services.domain.Data3DPlastic;
import com.mixram.telegram.bot.services.domain.entity.*;
import com.mixram.telegram.bot.services.domain.enums.Command;
import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.services.modules.Module3DPlasticDataSearcher;
import com.mixram.telegram.bot.utils.databinding.JsonUtil;
import com.mixram.telegram.bot.utils.htmlparser.ParseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author mixram on 2019-04-10.
 * @since ...
 */
@Log4j2
@Component
public class Bot3DComponentImpl implements Bot3DComponent {

    // <editor-fold defaultstate="collapsed" desc="***API elements***">

    private static final String PARSE_PATTERN_STRING = "^/SALES_.*";
    private static final Pattern PARSE_PATTERN = Pattern.compile(PARSE_PATTERN_STRING);
    private static final String MISUNDERSTANDING_MESSAGE = "Моя твоя не понимать... \uD83E\uDD2A";
    private static final String NO_WORK_WITH_SHOP = "К сожалению, я еще не работаю с этим магазином... \uD83D\uDE10";
    private static final String NO_DATA_FOR_SHOP = "У меня пока что нет данных о скидках в этом магазине... \uD83D\uDE1E";
    private static final String NO_DISCOUNTS = "Скидок нет, к сожалению";

    private final Module3DPlasticDataSearcher searcher;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CommandHolder {

        private Command command;
        private boolean full;

        @Override
        public String toString() {
            return JsonUtil.toJson(this);
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public Bot3DComponentImpl(@Qualifier("discountsOn3DPlasticDataCacheV2Component") Module3DPlasticDataSearcher searcher) {
        this.searcher = searcher;
    }


    // </editor-fold>

    @Override
    public String proceedUpdate(Update update) {
        Validate.notNull(update, "Update is not specified!");

        Message message = update.getMessage();
        if (noNeedToAnswer(message)) {
            log.debug("No need to answer - ignore.");

            return null;
        }

        final CommandHolder command;
        try {
            command = defineCommand(message.getText());
        } catch (Exception e) {
            return MISUNDERSTANDING_MESSAGE; //TODO: to make a set of different answers
        }

        return prepareAnswerWithCommand(command.getCommand(), command.isFull());
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 0.1.3.0
     */
    private boolean noNeedToAnswer(Message message) {
        User user = message.getUser();
        if (user != null && user.getIsBot()) {
            log.debug("Message from bot - ignore.");

            return true;
        }

        Chat chat = message.getChat();
        if (chat != null && "private".equalsIgnoreCase(chat.getType())) {
            return false;
        }

        List<MessageEntity> entities = message.getEntities();
        if (entities != null && entities.get(0).getType().equalsIgnoreCase("bot_command") && entities.get(
                0).getOffset() == 0) {
            log.debug("Message not a command - ignore.");

            return false;
        }

        return true;
    }

    /**
     * @since 0.1.3.0
     */
    private String prepareAnswerWithCommand(Command command,
                                            boolean full) {
        Validate.notNull(command, "Command is not specified!");

        String messageToSendString;

        if (Command.D_ALL == command) {
            StringBuilder builder = new StringBuilder();
            for (Shop3D shop : Shop3D.values()) {
                Data3DPlastic plastic = searcher.search(shop);
                String messageToSendStringTemp = plastic == null || CollectionUtils.isEmpty(plastic.getData()) ?
                                                 NO_DATA_FOR_SHOP + "\n" :
                                                 prepareMessageToSendString(Command.getByShop(shop), full, plastic);

                builder.append("<b>").append(shop.getName()).append(":").append("</b>").append("\n")
                       .append(messageToSendStringTemp).append("\n");
            }

            messageToSendString = builder.toString();
        } else {
            Data3DPlastic plastic = searcher.search(command.getShop());
            messageToSendString = plastic == null || CollectionUtils.isEmpty(plastic.getData()) ? NO_DATA_FOR_SHOP :
                                  prepareMessageToSendString(command, full, plastic);
        }

        if (StringUtils.isBlank(messageToSendString)) {
            messageToSendString = NO_DISCOUNTS;
        }

        return messageToSendString;
    }

    /**
     * @since 0.1.3.0
     */
    private String prepareMessageToSendString(Command command,
                                              boolean full,
                                              Data3DPlastic plastic) {
        String messageToSendString;

        switch (command) {
            case D_3DP:
                messageToSendString = full ? prepareAnswerText(plastic) : prepareAnswerTextShort(plastic);

                break;
            case D_MF:
            case D_DAS:
            case D_3DUA:
            case D_PLEX:
            case D_U3DF:
            default:
                messageToSendString = NO_WORK_WITH_SHOP;
        }

        return messageToSendString;
    }

    /**
     * @since 0.1.3.0
     */
    private String prepareAnswerTextShort(Data3DPlastic plastic) {
        Map<PlasticType, List<ParseData>> byName =
                plastic.getData().stream()
                       .filter(ParseData :: isInStock)
                       .collect(Collectors.groupingBy(ParseData :: getType, HashMap ::new,
                                                      Collectors.toCollection(ArrayList ::new)));
        Map<PlasticType, Boolean> discountsState =
                byName.entrySet().stream()
                      .collect(Collectors.toMap(Map.Entry :: getKey,
                                                e -> e.getValue().stream()
                                                      .anyMatch(p -> p.getProductOldPrice() != null)));

        StringBuilder answer = new StringBuilder();
        discountsState.forEach((k, v) -> answer.append(k.getName()).append(": ").append(getDiscountText(v)).append("\n"));

        return answer.toString();
    }

    /**
     * @since 0.1.3.0
     */
    private String getDiscountText(Boolean hasDiscount) {
        return hasDiscount ? "✅" : "❌";
    }

    /**
     * @since 0.1.3.0
     */
    private String prepareAnswerText(Data3DPlastic plastic) {
        List<ParseData> data = plastic.getData();

        int counter = 0;
        StringBuilder answer = new StringBuilder();
        for (ParseData datum : data) {
            if (datum.isInStock() && datum.getProductOldPrice() != null) {
                answer
                        .append("<b>").append(datum.getProductName()).append("</b>").append("\n")
                        .append("Обычная цена: ").append(datum.getProductOldPrice()).append("грн;\n")
                        .append("Цена со скидкой: ").append(datum.getProductSalePrice()).append("грн;\n")
                        .append("Ссылка: ").append(datum.getProductUrl()).append(".\n")
                        .append("======================================================").append("\n")
                ;

                counter++;
            }

            if (counter > 5) {
                answer
                        .append("<b>").append("И еще есть...").append("</b>");

                break;
            }
        }

        return answer.toString();
    }

    /**
     * @since 0.1.3.0
     */
    private boolean hasNoCommand(List<MessageEntity> entities) {
        return entities == null || !entities.get(0).getType().equalsIgnoreCase("bot_command") || entities.get(
                0).getOffset() != 0;
    }

    /**
     * @since 0.1.3.0
     */
    private CommandHolder defineCommand(String text) {
        if (!PARSE_PATTERN.matcher(text.toUpperCase()).matches()) {
            throw new UnsupportedOperationException(String.format("Unexpected pattern! '%s'", text));
        }

        String commandDataString = text.split(" ")[0];
        String[] commandElements = commandDataString.split("_");
        Command command = Command.getByName(commandElements[1].toUpperCase());
        boolean full = commandElements.length == 3 && "f".equalsIgnoreCase(commandElements[2]);

        if (command == null) {
            throw new UnsupportedOperationException(String.format("Unexpected command! '%s'", text));
        }

        return CommandHolder.builder()
                            .command(command)
                            .full(full)
                            .build();
    }

    // </editor-fold>
}

