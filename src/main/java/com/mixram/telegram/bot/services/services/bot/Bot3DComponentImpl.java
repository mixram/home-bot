package com.mixram.telegram.bot.services.services.bot;


import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableList;
import com.mixram.telegram.bot.services.domain.entity.*;
import com.mixram.telegram.bot.services.domain.enums.Command;
import com.mixram.telegram.bot.services.domain.enums.PlasticType;
import com.mixram.telegram.bot.services.domain.enums.Shop3D;
import com.mixram.telegram.bot.services.domain.enums.WorkType;
import com.mixram.telegram.bot.services.modules.Module3DPlasticDataSearcher;
import com.mixram.telegram.bot.services.services.bot.entity.MessageData;
import com.mixram.telegram.bot.services.services.bot.enums.PlasticPresenceState;
import com.mixram.telegram.bot.services.services.tapicom.TelegramAPICommunicationComponent;
import com.mixram.telegram.bot.utils.AsyncHelper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Supplier;
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

    private static final String SALES_PATTERN_STRING = "^/SALES_.*";
    private static final String OTHER_COMMANDS_PATTERN_STRING = "^/START.*|^/INFO.*";
    private static final Pattern SALES_PATTERN = Pattern.compile(SALES_PATTERN_STRING);
    private static final Pattern OTHER_PATTERN = Pattern.compile(OTHER_COMMANDS_PATTERN_STRING);
    private static final String NO_WORK_WITH_SHOP = "К сожалению, я еще не работаю с этим магазином... \uD83D\uDE10";
    private static final String NO_DATA_FOR_SHOP = "У меня пока что нет данных о скидках в этом магазине... \uD83D\uDE1E";
    private static final String NO_DISCOUNTS = "Скидок нет, к сожалению";
    private static final List<String> MISUNDERSTANDING_MESSAGE = ImmutableList.of(
            "Моя твоя не понимать... 🤪",
            "Какая-то не понятная команда... 🧐",
            "А можно чуток точнее? 🤓",
            "Абракадабра... 😎"
    );
    private static final String PRIVATE_CHAT_NAME = "private";
    private static final String GROUP_CHAT_NAME = "group";

    private final Integer maxQuantity;
    private final Random random;
    private final WorkType workType;
    private final List<Long> allowedGroups;
    private final String adminEmail;

    private final Module3DPlasticDataSearcher searcher;
    private final TelegramAPICommunicationComponent communicationComponent;
    private final AsyncHelper asyncHelper;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CommandHolder {

        /**
         * Command to execute.
         */
        private Command command;
        /**
         * true - need full message content, false - need short message content.
         */
        private boolean full;

        @Override
        public String toString() {
            return JsonUtil.toJson(this);
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="***Util elements***">

    @Autowired
    public Bot3DComponentImpl(@Value("${bot.settings.other.max-quantity-for-full-view}") Integer maxQuantity,
                              @Value("${bot.settings.work-with}") WorkType workType,
                              @Value("${bot.settings.work-with-groups}") String allowedGroups,
                              @Value("${bot.settings.admin-email}") String adminEmail,
                              @Qualifier("discountsOn3DPlasticDataCacheV2Component") Module3DPlasticDataSearcher searcher,
                              TelegramAPICommunicationComponent communicationComponent,
                              AsyncHelper asyncHelper) {
        this.maxQuantity = maxQuantity;
        this.workType = workType;
        this.allowedGroups = JsonUtil.fromJson(allowedGroups, new TypeReference<List<Long>>() {});
        this.adminEmail = adminEmail;
        this.searcher = searcher;
        this.communicationComponent = communicationComponent;
        this.asyncHelper = asyncHelper;

        this.random = new Random();
    }


    // </editor-fold>

    @Override
    public MessageData proceedUpdate(Update update) {
        Validate.notNull(update, "Update is not specified!");

        Message message = update.getMessage();

        MessageData workCheckMessage = checkMayWorkWith(message);
        if (workCheckMessage != null) {
            return workCheckMessage;
        }

        if (noNeedToAnswer(message)) {
            return null;
        }

        final CommandHolder command;
        try {
            command = defineCommand(message.getText());
        } catch (Exception e) {
            log.warn(String.format("Error in command defining: %s!", message.getText()), e);

            return prepareMisunderstandingMessage();
        }

        infoAdmin(update);

        return prepareAnswerWithCommand(command.getCommand(), command.isFull(), message.getChat().getType());
    }


    // <editor-fold defaultstate="collapsed" desc="***Private elements***">

    /**
     * @since 1.4.0.0
     */
    private MessageData checkMayWorkWith(Message message) {
        Chat chat = message.getChat();
        switch (workType) {
            case P:
                return isPrivate(chat.getType()) ? null : prepareNoPrivateChatMessage();
            case G:
                if (isGroup(chat.getType())) {
                    if (allowedGroups.contains(chat.getChatId())) {
                        return null;
                    }
                    return prepareConcreteGroupChatMessage();
                } else {
                    return prepareNoGroupChatMessage();
                }
            case B:
                if (isGroup(chat.getType())) {
                    if (allowedGroups.contains(chat.getChatId())) {
                        return null;
                    }
                    return prepareConcreteGroupChatMessage();
                } else {
                    return null;
                }
            default:
                throw new UnsupportedOperationException(String.format("Unexpected work type: '%s'!", workType));
        }
    }

    /**
     * @since 1.4.0.0
     */
    private boolean isPrivate(String type) {
        return PRIVATE_CHAT_NAME.equalsIgnoreCase(type);
    }

    /**
     * @since 1.4.0.0
     */
    private boolean isGroup(String type) {
        return GROUP_CHAT_NAME.equalsIgnoreCase(type);
    }

    /**
     * @since 1.4.0.0
     */
    private MessageData prepareNoPrivateChatMessage() {
        StringBuilder builder = new StringBuilder()
                .append("Добрый день!").append("\n")
                .append("Я работаю только в формате \"тет-а-тет\". Пожалуйста, не подключайте меня к групповым чатам.");

        return MessageData.builder()
                          .message(builder.toString())
                          .toResponse(false)
                          .toAdmin(false)
                          .leaveChat(true)
                          .build();
    }

    /**
     * @since 1.4.0.0
     */
    private MessageData prepareNoGroupChatMessage() {
        StringBuilder builder = new StringBuilder()
                .append("Добрый день!").append("\n")
                .append("Я работаю только в групповых чатах. Пожалуйста, не подключайте меня к общению в формате \"тет-а-тет\".");

        return MessageData.builder()
                          .message(builder.toString())
                          .toResponse(false)
                          .toAdmin(false)
                          .leaveChat(true)
                          .build();
    }

    /**
     * @since 1.4.0.0
     */
    private MessageData prepareConcreteGroupChatMessage() {
        StringBuilder builder = new StringBuilder()
                .append("Добрый день!").append("\n")
                .append("Я работаю только в разрешенных групповых чатах. По вопросам добавления в Ваш чат пишите в почту: ")
                .append(adminEmail)
                .append(".");

        return MessageData.builder()
                          .message(builder.toString())
                          .toResponse(false)
                          .toAdmin(false)
                          .leaveChat(true)
                          .build();
    }

    /**
     * @since 1.3.2.0
     */
    private MessageData prepareStartAnswer() {
        StringBuilder builder = new StringBuilder()
                .append("<b>").append("Привет!").append("</b>").append("\n")
                .append("Я слежу за скидками на 3d-пластик в основных магазинах (список магазинов и пластиков постепенно пополняется) и готов информировать тебя о них 😊")
                .append("\n")
                .append("Справа в меню Телеграм есть кнопка с символом \"/\" - нажми на нее и увидишь список всех поддерживаемых мною команд.")
                .append("\n").append("\n")
                .append("Удачных покупок 👍");

        return MessageData.builder()
                          .message(builder.toString())
                          .toAdmin(false)
                          .toResponse(false)
                          .userResponse(true)
                          .build();
    }

    /**
     * @since 1.4.0.0
     */
    private MessageData prepareInfoAnswer() {
        StringBuilder builder = new StringBuilder()
                .append("<b>").append("Привет!").append("</b>").append("\n")
                .append("Я слежу за скидками на 3d-пластик в основных магазинах (список магазинов и пластиков постепенно пополняется) и готов информировать тебя о них 😊")
                .append("\n")
                .append("Как только будет появляться что-то новое - я сам напишу в этот чат, меня не нужно вызывать.")
                .append("\n")
                .append("Если же нужно \"тет-а-тет\" общение - обратись к моему коллеге @Nerd3dBot.")
                .append("\n").append("\n")
                .append("Удачных покупок 👍");

        return MessageData.builder()
                          .message(builder.toString())
                          .toAdmin(false)
                          .toResponse(false)
                          .userResponse(false)
                          .build();
    }

    /**
     * @since 1.3.0.0
     */
    private void infoAdmin(Update update) {
        asyncHelper.doAsync((Supplier<Void>) () -> {
            doInfoAdmin(update);

            return null;
        });
    }

    /**
     * @since 1.3.0.0
     */
    private void doInfoAdmin(Update update) {
        try {
            Message message = update.getMessage();

            User user = message.getUser();
            if (user != null && communicationComponent.getAdminName().equals(user.getId().toString())) {
                return;
            }
            Long adminName = message.getChat().getChatId();
            if (communicationComponent.getAdminName().equals(adminName.toString())) {
                return;
            }

            StringBuilder builder = new StringBuilder()
                    .append("<b>").append("Bot has called by user! 😊").append("</b>").append("\n");

            if (user != null) {
                builder
                        .append("<b>").append("User:").append("</b>").append("\n")
                        .append(JsonUtil.toPrettyJson(user)).append("\n");
            }
            builder
                    .append("<b>").append("Chat:").append("</b>").append("\n")
                    .append(JsonUtil.toPrettyJson(message.getChat())).append("\n");

            MessageData messageData = MessageData.builder()
                                                 .message(builder.toString())
                                                 .build();

            communicationComponent.sendMessageToAdmin(messageData);
        } catch (Exception e) {
            log.warn("Error ==> infoAdmin", e);
        }
    }

    /**
     * @since 1.0.0.0
     */
    private MessageData prepareMisunderstandingMessage() {
        return MessageData.builder()
                          .toAdmin(false)
                          .toResponse(false)
                          .message(MISUNDERSTANDING_MESSAGE.get(random.nextInt(MISUNDERSTANDING_MESSAGE.size())))
                          .build();
    }

    /**
     * @since 0.1.3.0
     */
    private boolean noNeedToAnswer(Message message) {
        User user = message.getUser();
        if (user.getIsBot()) {
            log.debug("Message from bot - ignore.");

            return true;
        }

        Chat chat = message.getChat();
        if ((workType == WorkType.G && isPrivate(chat.getType())) || (workType == WorkType.P && isGroup(chat.getType()))) {
            log.debug("Chat type '{}' does not correspond to allowed type '{}'.",
                      chat :: getType,
                      () -> workType);

            return true;
        }

        List<MessageEntity> entities = message.getEntities();
        if (entities != null && entities.get(0).getType().equalsIgnoreCase("bot_command") && entities.get(
                0).getOffset() == 0) {
            return false;
        }

        log.debug("noNeedToAnswer method ==> deafault behaviour ==> ignore.");

        return true;
    }

    /**
     * @since 0.1.3.0
     */
    private MessageData prepareAnswerWithCommand(Command command,
                                                 boolean full,
                                                 String chatType) {
        Validate.notNull(command, "Command is not specified!");

        //TODO: private bot should answer only to "tet-a-tet" questions + only to allowed commands

        switch (workType) {
            case G:
                if (Command.INFO == command) {
                    if (isGroup(chatType)) {
                        return prepareInfoAnswer();
                    } else {
                        log.debug("Command '{}' is allowed in group chats bot only!",
                                  () -> command);

                        return null;
                    }
                } else {
                    log.debug("Command '{}' is not allowed in group chats bot!",
                              () -> command);

                    return null;
                }
            case P:
                if (Command.START == command) {
                    if (isPrivate(chatType)) {
                        return prepareStartAnswer();
                    } else {
                        log.debug("Command '{}' is allowed in \"tet-a-tet\" bot only!",
                                  () -> command);

                        return null;
                    }
                } else if (Command.INFO == command) {
                    log.debug("Command '{}' is allowed in group chats bot only!",
                              () -> command);

                    return null;
                }
            case B:
                String messageToSendString;

                if (Command.D_ALL == command) {
                    StringBuilder builder = new StringBuilder();
                    for (Shop3D shop : Shop3D.values()) {
                        Data3DPlastic plastic = searcher.search(shop);
                        String messageToSendStringTemp = plastic == null || CollectionUtils.isEmpty(plastic.getData()) ?
                                                         NO_DATA_FOR_SHOP + "\n" :
                                                         prepareMessageToSendString(Command.getByShop(shop), full, plastic,
                                                                                    shop);

                        builder.append("<b>").append("***").append(shop.getName()).append("***").append("</b>").append("\n")
                               .append(messageToSendStringTemp).append("\n");
                    }

                    messageToSendString = builder.toString();
                } else {
                    Shop3D shop = command.getShop();
                    Data3DPlastic plastic = searcher.search(shop);

                    messageToSendString = plastic == null || CollectionUtils.isEmpty(plastic.getData()) ? NO_DATA_FOR_SHOP :
                                          prepareMessageToSendString(command, full, plastic, shop);
                }

                if (StringUtils.isBlank(messageToSendString)) {
                    messageToSendString = NO_DISCOUNTS;
                }

                return MessageData.builder()
                                  .toAdmin(false)
                                  .toResponse(false)
                                  .userResponse(WorkType.P == workType)
                                  .showUrlPreview(false)
                                  .message(messageToSendString)
                                  .build();
            default:
                throw new UnsupportedOperationException(String.format("Unexpected work type: '%s'!", workType));
        }
    }

    /**
     * @since 0.1.3.0
     */
    private String prepareMessageToSendString(Command command,
                                              boolean full,
                                              Data3DPlastic plastic,
                                              Shop3D shop) {
        String messageToSendString;

        switch (command) {
            case D_3DP:
            case D_3DUA:
            case D_MF:
            case D_U3DF:
                messageToSendString = full ? prepareAnswerText(plastic, shop) : prepareAnswerTextShort(plastic);

                break;
            //            case D_DAS:
            //            case D_PLEX:
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
        Map<PlasticType, PlasticPresenceState> discountsState =
                byName.entrySet().stream()
                      .collect(Collectors.toMap(Map.Entry :: getKey,
                                                e -> definePlasticState(e.getValue())));

        StringBuilder answer = new StringBuilder();
        discountsState.entrySet().stream()
                      .sorted(Comparator.comparing(Map.Entry :: getValue))
                      .forEach(s -> {
                          String text = alignText(s.getKey().getName() + ":");
                          answer
                                  .append("<code>").append(text).append("</code>").append(" ")
                                  .append(getDiscountText(s.getValue()))
                                  //                    .append("<a href=\"").append("https://www.ebay.com").append("\">")
                                  //                    .append("🔗")
                                  //                    .append("</a>")
                                  .append("\n");
                      });

        return answer.toString();
    }

    /**
     * @since 1.0.0.0
     */
    private String alignText(String text) {
        return String.format("%-12s", text);
    }

    /**
     * @since 0.1.3.0
     */
    private String getDiscountText(PlasticPresenceState presenceState) {
        switch (presenceState) {
            case DISCOUNT:
                return "💰";
            case IN_STOCK:
                return "🏢";
            case NOT_IN_STOCK:
                return "🤷‍♂";
            default:
                throw new UnsupportedOperationException(
                        String.format("Unexpected plastic presence state: '%s'!", presenceState));
        }
    }

    /**
     * @since 0.1.3.0
     */
    private String prepareAnswerText(Data3DPlastic plastic,
                                     Shop3D shop) {
        List<ParseData> data = plastic.getData();

        int counter = 0;
        StringBuilder answer = new StringBuilder();
        for (ParseData datum : data) {
            if (mayUsePlastic(datum)) {
                answer
                        .append("<b>").append(datum.getProductName()).append("</b>").append("\n")
                        .append("Обычная цена: ").append(datum.getProductOldPrice()).append("грн;\n")
                        .append("Цена со скидкой: ").append(datum.getProductSalePrice()).append("грн;\n")
                        .append("Ссылка: ").append(datum.getProductUrl()).append(".\n")
                        .append("\n")
                ;

                counter++;
            }

            if (counter == maxQuantity) {
                answer
                        .append("<b>").append("Смотри остальное тут ").append(shop.getUrl()).append("</b>");

                break;
            }
        }

        return answer.toString();
    }

    /**
     * @since 1.2.0.0
     */
    private boolean mayUsePlastic(ParseData datum) {
        return datum.isInStock() && datum.getProductOldPrice() != null && datum.getProductSalePrice() != null;
    }

    /**
     * @since 1.4.0.0
     */
    private PlasticPresenceState definePlasticState(List<ParseData> data) {
        Set<PlasticPresenceState> states = new HashSet<>(PlasticPresenceState.values().length);
        data.forEach(d -> states.add(plasticState(d)));

        if (states.contains(PlasticPresenceState.DISCOUNT)) {
            return PlasticPresenceState.DISCOUNT;
        } else if (states.contains(PlasticPresenceState.IN_STOCK)) {
            return PlasticPresenceState.IN_STOCK;
        } else {
            return PlasticPresenceState.NOT_IN_STOCK;
        }
    }

    /**
     * @since 1.4.0.0
     */
    private PlasticPresenceState plasticState(ParseData datum) {
        if (!datum.isInStock()) {
            return PlasticPresenceState.NOT_IN_STOCK;
        }

        return datum.getProductOldPrice() != null && datum.getProductSalePrice() != null ? PlasticPresenceState.DISCOUNT :
               PlasticPresenceState.IN_STOCK;
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
        text = text.toUpperCase();
        if (OTHER_PATTERN.matcher(text).matches()) {
            String commandDataString = parseCommandDataString(text);
            commandDataString = commandDataString.replaceAll("/", "");
            Command command = Command.getByName(commandDataString);
            if (command == null) {
                throw new UnsupportedOperationException(String.format("Unexpected command! '%s'", text));
            }

            return CommandHolder.builder()
                                .command(command)
                                .full(false)
                                .build();
        }
        if (SALES_PATTERN.matcher(text).matches()) {
            String commandDataString = parseCommandDataString(text);
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

        throw new UnsupportedOperationException(String.format("Unexpected pattern! '%s'", text));
    }

    /**
     * 1.4.0.0
     */
    private String parseCommandDataString(String text) {
        String commandDataString = text.split(" ")[0];
        if (commandDataString.contains("@")) {
            commandDataString = commandDataString.substring(0, commandDataString.indexOf("@"));
        }

        return commandDataString;
    }

    // </editor-fold>
}

