package com.anastasia.telegram_bot.domain.command.impl;

import com.anastasia.telegram_bot.domain.command.BotCommandHandler;
import com.anastasia.telegram_bot.domain.command.BotCommands;
import com.anastasia.telegram_bot.domain.command.CommandHandler;
import com.anastasia.telegram_bot.domain.element.ButtonKeys;
import com.anastasia.telegram_bot.domain.element.InlineKeyboardBuilder;
import com.anastasia.telegram_bot.domain.session.ChatSession;
import com.anastasia.telegram_bot.domain.session.ChatSessionService;
import com.anastasia.telegram_bot.service.MarketDataService;
import com.anastasia.telegram_bot.utils.ChatBotUtility;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.markets.MarketPage;
import com.anastasia.trade_project.markets.Securities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@CommandHandler(command = BotCommands.MARKET)
public class MarketDataHandler implements BotCommandHandler {

    private static final String KEY_SAMPLE = "MARKET.%s";
    private static final String KEY_SAMPLE_2 = "MARKET.%s.%s";
    private static final byte DEFAULT_ITEM_COUNT = 10;

    private final MessageSource messageSource;
    private final MarketDataService marketDataService;
    private final ChatSessionService chatSessionService;
    private final InlineKeyboardBuilder inlineKeyboardBuilder;

    private final Map<Steps, InlineKeyboardMarkup> keyboards;


    @Autowired
    public MarketDataHandler(MessageSource messageSource,
                             MarketDataService marketDataService,
                             ChatSessionService chatSessionService,
                             InlineKeyboardBuilder inlineKeyboardBuilder) {
        this.messageSource = messageSource;
        this.marketDataService = marketDataService;
        this.chatSessionService = chatSessionService;
        this.inlineKeyboardBuilder = inlineKeyboardBuilder;
        keyboards = initKeyboardMap(inlineKeyboardBuilder);
    }


    @Override
    public Mono<? extends BotApiMethodMessage> handle(Message message, ChatSession session) {
        Locale locale = ChatBotUtility.getLocale(message);
        return handle(message.getText(), session, locale);
    }

    @Override
    public Mono<? extends BotApiMethodMessage> handle(String text, ChatSession session, Locale locale) {
        Steps step = Steps.values()[session.getContext().getStep()];
        session.getContext()
                .setCommand(BotCommands.MARKET);

        return (switch (step) {

            case EXCHANGE -> Mono.just(exchange(session, step, locale));

            case SECURITIES_TYPE -> Mono.just(securitiesType(session, text, step, locale));

            case SORTING -> Mono.just(sorting(session, text, step, locale));

            case RETURN_RESULT -> returnResult(session, text, locale);

            case PAGINATION -> pagination(session, text, locale);
        }
        ).map(sendMessage -> {
            chatSessionService.save(session).subscribe();
            return sendMessage;
        });
    }


    private SendMessage exchange(ChatSession session, Steps step, Locale locale) {
        session.getContext()
                .setStep(Steps.SECURITIES_TYPE.ordinal());
        String text = messageSource.getMessage(KEY_SAMPLE.formatted(step), null, locale);
        return createSendMessage(session.getChatId(), text, keyboards.get(Steps.EXCHANGE));
    }

    private SendMessage securitiesType(ChatSession session, String messageText, Steps step, Locale locale) {
        Exchange exchange = Exchange.valueOf(messageText.toUpperCase());
        session.getContext()
                .setStep(Steps.SORTING.ordinal());
        session.getAttributes()
                .put(Steps.EXCHANGE.name(), exchange.name());
        String text = messageSource.getMessage(KEY_SAMPLE.formatted(step), null, locale);
        return createSendMessage(session.getChatId(), text, securitiesTypeKeyboard(locale));
    }

    private SendMessage sorting(ChatSession session, String messageText, Steps step, Locale locale) {
        SecuritiesType securitiesType = SecuritiesType.valueOf(messageText.toUpperCase());
        session.getContext()
                .setStep(Steps.RETURN_RESULT.ordinal());
        session.getAttributes()
                .put(Steps.SECURITIES_TYPE.name(), securitiesType.name());
        String text = messageSource.getMessage(KEY_SAMPLE.formatted(step), null, locale);
        return createSendMessage(session.getChatId(), text, keyboards.get(Steps.SORTING));
    }

    private Mono<? extends BotApiMethodMessage> returnResult(ChatSession session, String messageText, Locale locale) {
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

    private Mono<? extends BotApiMethodMessage> pagination(ChatSession session, String messageText, Locale locale) {
        session.getContext()
                .setStep(Steps.PAGINATION.ordinal());
        ButtonKeys buttonKey = ButtonKeys.valueOf(messageText);
        int pageNumber = Integer.parseInt(session.getAttributes().get(Steps.PAGINATION.name()));
        switch (buttonKey) {
            case NEXT -> pageNumber += 1;
            case PREVIOUS -> pageNumber -= 1;
            case BACK -> {
                return Mono.just(
                        sorting(session, session.getAttributes().get(Steps.SECURITIES_TYPE.name()), Steps.SORTING, locale));
            }
            case EXIT -> {
                session.getContext().setCommand(null);
                return Mono.just(createSendMessage(session.getChatId(), ""));
            }
        }
        session.getAttributes().put(Steps.PAGINATION.name(), Integer.toString(pageNumber));
        return getItems(session, pageNumber)
                .collectList()
                .map(list -> {
                    int page = Integer.parseInt(session.getAttributes().get(Steps.PAGINATION.name()));
                    var keyboardMarkup = collectItems(locale, page == 1, list);
                    String exchange = session.getAttributes().get(Steps.EXCHANGE.name());
                    return createSendMessage(session.getChatId(), exchange, keyboardMarkup);
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
        String template = messageSource.getMessage(KEY_SAMPLE.formatted(Steps.RETURN_RESULT), null, locale);
        //TODO remake
        List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        for (var s : securities) {
            // {name} *** {ticker}\nPrice: {price} ({price_datetime})\nCurrency: {currency}
            String item = template.formatted(s.getName(), s.getTicker(), s.getPrice().price(), s.getPrice().time(), s.getCurrency());

            String callback = ChatBotUtility
                    .callBackQuery(BotCommands.MARKET, Steps.PAGINATION.ordinal(), s.getTicker() + ':' + s.getExchange());

            buttonRows.add(inlineKeyboardBuilder.singleInlineKeyboardButton(callback, item));
        }
        String callbackPrefix = ChatBotUtility.callBackQueryPrefix(BotCommands.MARKET, Steps.PAGINATION.ordinal());
        buttonRows.add(inlineKeyboardBuilder.navigationButtonRow(callbackPrefix, isFirstPage, locale));

        return inlineKeyboardBuilder.inlineKeyboard(buttonRows);
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


    private InlineKeyboardMarkup securitiesTypeKeyboard(Locale locale) {
        Map<String, String> map = new HashMap<>();
        for (var s : SecuritiesType.values()) {
            String key = KEY_SAMPLE_2.formatted(Steps.SECURITIES_TYPE, s);
            String label = messageSource.getMessage(key, null, locale);
            String callbackQuery = ChatBotUtility
                    .callBackQuery(BotCommands.MARKET, Steps.SECURITIES_TYPE.ordinal() + 1, s.name());
            map.put(callbackQuery, label);
        }
        return inlineKeyboardBuilder
                .inlineKeyboard(inlineKeyboardBuilder.inlineKeyboardButtons(map));
    }

    private Map<Steps, InlineKeyboardMarkup> initKeyboardMap(InlineKeyboardBuilder inlineKeyboardBuilder) {
        Map<Steps, InlineKeyboardMarkup> keyboardMarkupMap = new HashMap<>(8);
        BotCommands command = BotCommands.MARKET;

        //Exchange
        Map<String, String> map = new HashMap<>();
        int step = Steps.EXCHANGE.ordinal() + 1;
        map.put(ChatBotUtility.callBackQuery(command, step, Exchange.NYSE.name()),
                Exchange.NYSE.name());
        map.put(ChatBotUtility.callBackQuery(command, step, Exchange.MOEX.name()),
                Exchange.MOEX.name());
        keyboardMarkupMap.put(Steps.EXCHANGE, inlineKeyboardBuilder
                        .inlineKeyboard(inlineKeyboardBuilder.inlineKeyboardButtons(map)));
        map.clear();

        //Sorting
        step = Steps.SORTING.ordinal() + 1;
        map.put(ChatBotUtility.callBackQuery(command, step, SortingType.VOLUME.name()),
                SortingType.VOLUME.value);
        map.put(ChatBotUtility.callBackQuery(command, step, SortingType.ALPHABET.name()),
                SortingType.ALPHABET.value);
        keyboardMarkupMap.put(Steps.SORTING, inlineKeyboardBuilder
                        .inlineKeyboard(inlineKeyboardBuilder.inlineKeyboardButtons(map)));

        return keyboardMarkupMap;
    }
}
