package com.anastasia.telegram_bot.domain.command.impl;

import com.anastasia.telegram_bot.configuration.TelegramBotConfig;
import com.anastasia.telegram_bot.domain.command.BotCommandHandler;
import com.anastasia.telegram_bot.domain.command.BotCommands;
import com.anastasia.telegram_bot.domain.command.CommandHandler;
import com.anastasia.telegram_bot.domain.element.ButtonKeys;
import com.anastasia.telegram_bot.domain.element.InlineKeyboardBuilder;
import com.anastasia.telegram_bot.domain.session.*;
import com.anastasia.telegram_bot.service.MarketDataService;
import com.anastasia.telegram_bot.utils.ChatBotUtility;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.markets.MarketPage;
import com.anastasia.trade_project.markets.Securities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.*;

@CommandHandler(command = BotCommands.MARKET)
public class MarketDataHandler extends BotCommandHandler {

    private static final String KEY_TEMPLATE = "MARKET.%s";
    private static final String KEY_TEMPLATE_2 = "MARKET.%s.%s";
    private static final String SECURITIES_TEMPLATE = "%s: %s (%s)";
    private static final byte DEFAULT_ITEM_COUNT = 10;

    private final MessageSource messageSource;
    private final MarketDataService marketDataService;
    private final ChatSessionService chatSessionService;
    private final InlineKeyboardBuilder inlineKeyboardBuilder;

    private final Map<Steps, List<InlineKeyboardButton>> keyboardButtons;


    @Autowired
    public MarketDataHandler(MessageSource messageSource,
                             MarketDataService marketDataService,
                             ChatSessionService chatSessionService,
                             InlineKeyboardBuilder inlineKeyboardBuilder) {
        this.messageSource = messageSource;
        this.marketDataService = marketDataService;
        this.chatSessionService = chatSessionService;
        this.inlineKeyboardBuilder = inlineKeyboardBuilder;
        keyboardButtons = initKeyboardMap(inlineKeyboardBuilder);
    }


    @Override
    public Mono<BotApiMethod<?>> handle(Message message, ChatSession session) {
        Locale locale = ChatBotUtility.getLocale(message);
        String text = message.getText();
        Steps step = Steps.values()[session.getContext().getStep()];
        session.getContext()
                .setCommand(BotCommands.MARKET);

        return (switch (step) {

            case EXCHANGE -> Mono.just(exchange(session, step, locale));

            case RETURN_RESULT -> returnResult(session, text, locale);

            default -> throw new IllegalArgumentException();
        }
        ).map(sendMessage -> {
            chatSessionService.save(session).subscribe();
            return sendMessage;
        });
    }

    @Override
    public Mono<BotApiMethod<?>> handle(CallbackQuery callbackQuery, ChatSession session, Locale locale) {
        int messageId = callbackQuery.getMessage().getMessageId();
        String text = ChatBotUtility.callBackData(callbackQuery)[2];
        Steps step = Steps.values()[session.getContext().getStep()];
        session.getContext()
                .setCommand(BotCommands.MARKET);

        return (switch (step) {

            case SECURITIES_TYPE -> Mono.just(securitiesType(session, messageId, text, step, locale));

            case SORTING -> Mono.just(sorting(session, messageId, text, step, locale));

            case RETURN_RESULT -> returnResult(session, text, locale);

            case PAGINATION -> pagination(session, messageId, text, locale);

            default -> throw new IllegalArgumentException();
        }
        ).map(sendMessage -> {
            chatSessionService.save(session).subscribe();
            return sendMessage;
        });
    }


    private BotApiMethod<?> handleCallbackQueryBack(ChatSession session, int messageId, Locale locale) {
        int stepInt = session.getContext().getStep();
        if (stepInt > 0) {
            stepInt -= 1;
        }
        Steps step = Steps.values()[stepInt];
        session.getContext().setStep(stepInt);
        return switch (step) {
            case EXCHANGE ->
                    exchange(session,
                            messageId,
                            step,
                            locale);
            case SECURITIES_TYPE ->
                    securitiesType(session,
                            messageId,
                            session.getAttributes().get(Steps.EXCHANGE.name()),
                            step,
                            locale);
            default -> throw new IllegalArgumentException();
        };
    }

    private SendMessage exchange(ChatSession session, Steps step, Locale locale) {
        session.getContext()
                .setStep(Steps.SECURITIES_TYPE.ordinal());
        String text = messageSource.getMessage(KEY_TEMPLATE.formatted(step), null, locale);
        var keyboard = inlineKeyboardBuilder.inlineKeyboard(keyboardButtons.get(step));
        return createSendMessage(session.getChatId(), text, keyboard);
    }

    private BotApiMethod<?> exchange(ChatSession session, int messageId, Steps step, Locale locale) {
        session.getContext()
                .setStep(Steps.SECURITIES_TYPE.ordinal());
        String text = messageSource.getMessage(KEY_TEMPLATE.formatted(step), null, locale);
        var keyboard = inlineKeyboardBuilder.inlineKeyboard(keyboardButtons.get(step));
        return editMessageText(session.getChatId(), messageId, text, keyboard);
    }

    private BotApiMethod<?> securitiesType(ChatSession session, int messageId, String messageText, Steps step, Locale locale) {
        if (messageText.equals(ButtonKeys.BACK.name())) {
            return handleCallbackQueryBack(session, messageId, locale);
        }
        Exchange exchange = Exchange.valueOf(messageText.toUpperCase());
        session.getContext()
                .setStep(Steps.SORTING.ordinal());
        session.getAttributes()
                .put(Steps.EXCHANGE.name(), exchange.name());
        String text = messageSource.getMessage(KEY_TEMPLATE.formatted(step), null, locale);
        return editMessageText(session.getChatId(), messageId, text, securitiesTypeKeyboard(locale));
    }

    private BotApiMethod<?> sorting(ChatSession session, int messageId, String messageText, Steps step, Locale locale) {
        if (messageText.equals(ButtonKeys.BACK.name())) {
            return handleCallbackQueryBack(session, messageId, locale);
        }
        SecuritiesType securitiesType = SecuritiesType.valueOf(messageText.toUpperCase());
        session.getContext()
                .setStep(Steps.RETURN_RESULT.ordinal());
        session.getAttributes()
                .put(Steps.SECURITIES_TYPE.name(), securitiesType.name());
        String text = messageSource.getMessage(KEY_TEMPLATE.formatted(step), null, locale);
        String callbackPrefix = ChatBotUtility.callBackQueryPrefix(BotCommands.MARKET, step.ordinal());
        var keyboard = inlineKeyboardBuilder.inlineKeyboard(keyboardButtons.get(step),
                List.of(inlineKeyboardBuilder.navigationButton(ButtonKeys.BACK, callbackPrefix, locale)));
        return editMessageText(session.getChatId(), messageId, text, keyboard);
    }

    private Mono<BotApiMethod<?>> returnResult(ChatSession session, String messageText, Locale locale) {
        SortingType sortingType = SortingType.valueOf(messageText.toUpperCase());
        int pageNumber = 1;
        session.getContext()
                .setStep(Steps.PAGINATION.ordinal());
        session.getAttributes()
                .put(Steps.SORTING.name(), sortingType.name());
        session.getAttributes()
                .put(Steps.PAGINATION.name(), String.valueOf(pageNumber));
        return getItems(session, pageNumber)
                .collectList()
                .map(list -> {
                    var keyboardMarkup = collectItems(locale, true, list);
                    String exchange = session.getAttributes().get(Steps.EXCHANGE.name());
                    return createSendMessage(session.getChatId(), exchange, keyboardMarkup);
                });
    }

    private Mono<BotApiMethod<?>> pagination(ChatSession session, int messageId, String messageText, Locale locale) {
        session.getContext()
                .setStep(Steps.PAGINATION.ordinal());
        ButtonKeys buttonKey = ButtonKeys.valueOf(messageText);
        int pageNumber = Integer.parseInt(session.getAttributes().get(Steps.PAGINATION.name()));
        switch (buttonKey) {
            case NEXT -> pageNumber += 1;
            case PREVIOUS -> pageNumber -= 1;
            case BACK -> {
                return Mono.just(exchange(session, Steps.EXCHANGE, locale));
            }
        }
        session.getAttributes().put(Steps.PAGINATION.name(), Integer.toString(pageNumber));
        return getItems(session, pageNumber)
                .collectList()
                .map(list -> {
                    int page = Integer.parseInt(session.getAttributes().get(Steps.PAGINATION.name()));
                    var keyboardMarkup = collectItems(locale, page == 1, list);
                    String exchange = session.getAttributes().get(Steps.EXCHANGE.name());
                    return editMessageText(session.getChatId(), messageId, exchange, keyboardMarkup);
                });
    }

    private Flux<? extends Securities> getItems(ChatSession session, int pageNumber) {
        ExchangeMarket exchange = ExchangeMarket.valueOf(session.getAttributes().get(Steps.EXCHANGE.name()));
        SecuritiesType securitiesType = SecuritiesType
                .valueOf(session.getAttributes().get(Steps.SECURITIES_TYPE.name()));
        String sortingParam = session.getAttributes().get(Steps.SORTING.name());
        MarketPage marketPage = new MarketPage(pageNumber, DEFAULT_ITEM_COUNT, sortingParam, null);

        return switch (securitiesType) {
            case STOCKS -> marketDataService.stockList(exchange, marketPage);
            case FUTURES -> marketDataService.futuresList(exchange, marketPage);
        };
    }

    private InlineKeyboardMarkup collectItems(Locale locale, boolean isFirstPage, List<? extends Securities> securities) {
        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        for (var s : securities) {
            // SECURITIES_TEMPLATE  ${ticker}: ${price} (${datetime})
            String item = SECURITIES_TEMPLATE
                    .formatted(s.getTicker(), s.getPrice().price(), s.getPrice().time().format(TelegramBotConfig.DATE_TIME_FORMAT));

            String callback = ChatBotUtility
                    .callBackQuery(BotCommands.MARKET, Steps.PAGINATION.ordinal(), s.getTicker() + ':' + s.getExchange());

            buttonRows.add(inlineKeyboardBuilder.singleInlineKeyboardButton(callback, item));
        }
        String callbackPrefix = ChatBotUtility.callBackQueryPrefix(BotCommands.MARKET, Steps.PAGINATION.ordinal());
        buttonRows.add(inlineKeyboardBuilder.navigationButtonRow(callbackPrefix, isFirstPage, locale));

        return inlineKeyboardBuilder.inlineKeyboard(buttonRows);
    }

    private InlineKeyboardMarkup securitiesTypeKeyboard(Locale locale) {
        Map<String, String> map = new HashMap<>();
        String callBackPrefix = ChatBotUtility
                .callBackQueryPrefix(BotCommands.MARKET, Steps.SECURITIES_TYPE.ordinal() + 1);
        for (var s : SecuritiesType.values()) {
            String key = KEY_TEMPLATE_2.formatted(Steps.SECURITIES_TYPE, s);
            String label = messageSource.getMessage(key, null, locale);
            map.put(callBackPrefix + s.name(), label);
        }
        callBackPrefix = ChatBotUtility
                .callBackQueryPrefix(BotCommands.MARKET, Steps.SECURITIES_TYPE.ordinal());
        InlineKeyboardButton navigationButton = inlineKeyboardBuilder
                .navigationButton(ButtonKeys.BACK, callBackPrefix, locale);
        return inlineKeyboardBuilder
                .inlineKeyboard(inlineKeyboardBuilder.inlineKeyboardButtons(map), List.of(navigationButton));
    }

    private Map<Steps, List<InlineKeyboardButton>> initKeyboardMap(InlineKeyboardBuilder inlineKeyboardBuilder) {
        Map<Steps, List<InlineKeyboardButton>> buttonRowMap = new HashMap<>(8);
        BotCommands command = BotCommands.MARKET;
        Map<String, String> map = new HashMap<>();

        //Exchange
        int step = Steps.EXCHANGE.ordinal() + 1;
        String callbackPrefix = ChatBotUtility.callBackQueryPrefix(command, step);
        map.put(callbackPrefix + Exchange.NYSE, Exchange.NYSE.name());
        map.put(callbackPrefix + Exchange.MOEX, Exchange.MOEX.name());
        var buttons = inlineKeyboardBuilder.inlineKeyboardButtons(map);
        buttonRowMap.put(Steps.EXCHANGE, buttons);
        map.clear();

        //Sorting
        step = Steps.SORTING.ordinal() + 1;
        callbackPrefix = ChatBotUtility.callBackQueryPrefix(command, step);
        map.put(callbackPrefix + SortingType.VOLUME, SortingType.VOLUME.value);
        map.put(callbackPrefix + SortingType.ALPHABET, SortingType.ALPHABET.value);
        buttons = inlineKeyboardBuilder.inlineKeyboardButtons(map);
        buttonRowMap.put(Steps.SORTING, buttons);

        return buttonRowMap;
    }


    enum Steps {
        EXCHANGE,
        SECURITIES_TYPE,
        SORTING,
        RETURN_RESULT,
        PAGINATION
    }

    enum Exchange {
        NYSE, MOEX
    }

    enum SecuritiesType {
        STOCKS, FUTURES
    }

    enum SortingType {
        VOLUME("trade volume"), ALPHABET("abc...");

        public final String value;
        SortingType(String value) {
            this.value = value;
        }

        enum Direction {
            asc, desc
        }
    }
}
