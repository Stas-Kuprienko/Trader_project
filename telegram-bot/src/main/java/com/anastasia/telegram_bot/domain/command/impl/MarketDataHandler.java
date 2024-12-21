package com.anastasia.telegram_bot.domain.command.impl;

import com.anastasia.telegram_bot.domain.command.BotCommandHandler;
import com.anastasia.telegram_bot.domain.command.BotCommands;
import com.anastasia.telegram_bot.domain.command.CommandHandler;
import com.anastasia.telegram_bot.domain.element.KeyboardMarkupBuilder;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@CommandHandler(command = BotCommands.MARKET)
public class MarketDataHandler implements BotCommandHandler {

    private static final String KEY_SAMPLE = "MARKET.%s";
    private static final byte DEFAULT_ITEM_COUNT = 10;

    private final MessageSource messageSource;
    private final MarketDataService marketDataService;
    private final ChatSessionService chatSessionService;
    private final KeyboardMarkupBuilder keyboardMarkupBuilder;

    private final Map<Steps, InlineKeyboardMarkup> keyboards;


    @Autowired
    public MarketDataHandler(MessageSource messageSource,
                             MarketDataService marketDataService,
                             ChatSessionService chatSessionService,
                             KeyboardMarkupBuilder keyboardMarkupBuilder) {
        this.messageSource = messageSource;
        this.marketDataService = marketDataService;
        this.chatSessionService = chatSessionService;
        this.keyboardMarkupBuilder = keyboardMarkupBuilder;
        keyboards = initKeyboardMap(keyboardMarkupBuilder);
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

            case RETURN_RESULT -> returnResult(session, text);

            case PAGINATION -> pagination(session, text);
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

    private Mono<? extends BotApiMethodMessage> returnResult(ChatSession session, String messageText) {
        SortingType sortingType = SortingType.valueOf(messageText.toUpperCase());
        session.getContext()
                .setStep(Steps.PAGINATION.ordinal());
        session.getAttributes()
                .put(Steps.SORTING.name(), sortingType.name());
        return getItems(session, 1)
                .collectList()
                .map(list -> collectItems(session.getChatId(), list));
    }

    private Mono<? extends BotApiMethodMessage> pagination(ChatSession session, String messageText) {
        session.getContext()
                .setStep(Steps.PAGINATION.ordinal());
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(messageText);
        } catch (NumberFormatException e) {

            session.getContext().setStep(0);
            throw new IllegalArgumentException(e);
        }
        return getItems(session, pageNumber)
                .collectList()
                .map(list -> collectItems(session.getChatId(), list));
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

    private BotApiMethodMessage collectItems(long chatId, List<? extends Securities> securities) {
        //TODO
        StringBuilder strBuild = new StringBuilder();
        for (var s : securities) {
            strBuild.append(s).append('\n').append('\n');
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(strBuild.toString());
        return sendMessage;
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
            String label = messageSource.getMessage(s.name(), null, locale);
            String callbackQuery = ChatBotUtility
                    .callBackQuery(BotCommands.MARKET.name(), Steps.SECURITIES_TYPE.ordinal(), s.name());
            map.put(callbackQuery, label);
        }
        return keyboardMarkupBuilder
                .inlineKeyboardMarkup(keyboardMarkupBuilder.inlineKeyboardButtons(map));
    }

    private Map<Steps, InlineKeyboardMarkup> initKeyboardMap(KeyboardMarkupBuilder keyboardMarkupBuilder) {
        Map<Steps, InlineKeyboardMarkup> keyboardMarkupMap = new HashMap<>(8);
        String command = BotCommands.MARKET.name();

        //Exchange
        Map<String, String> map = new HashMap<>();
        map.put(ChatBotUtility.callBackQuery(command, Steps.EXCHANGE.ordinal(), Exchange.NYSE.name()),
                Exchange.NYSE.name());
        map.put(ChatBotUtility.callBackQuery(command, Steps.EXCHANGE.ordinal(), Exchange.MOEX.name()),
                Exchange.MOEX.name());
        keyboardMarkupMap.put(Steps.EXCHANGE, keyboardMarkupBuilder
                        .inlineKeyboardMarkup(keyboardMarkupBuilder.inlineKeyboardButtons(map)));
        map.clear();

        //Sorting
        map.put(ChatBotUtility.callBackQuery(command, Steps.SORTING.ordinal(), SortingType.VOLUME.name()),
                SortingType.VOLUME.value);
        map.put(ChatBotUtility.callBackQuery(command, Steps.SORTING.ordinal(), SortingType.ALPHABET.name()),
                SortingType.ALPHABET.value);
        keyboardMarkupMap.put(Steps.SORTING, keyboardMarkupBuilder
                        .inlineKeyboardMarkup(keyboardMarkupBuilder.inlineKeyboardButtons(map)));

        return keyboardMarkupMap;
    }
}
