package com.anastasia.telegram_bot.domain.command.impl;

import com.anastasia.telegram_bot.domain.command.BotCommand;
import com.anastasia.telegram_bot.domain.command.BotCommandHandler;
import com.anastasia.telegram_bot.domain.command.CommandHandler;
import com.anastasia.telegram_bot.domain.session.ChatSession;
import com.anastasia.telegram_bot.domain.session.ChatSessionService;
import com.anastasia.telegram_bot.utils.ChatBotUtility;
import com.anastasia.trade_project.core_client.CoreServiceClientV1;
import com.anastasia.trade_project.enums.ExchangeMarket;
import com.anastasia.trade_project.markets.MarketPage;
import com.anastasia.trade_project.markets.Securities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Locale;

@CommandHandler(command = BotCommand.MARKET)
public class MarketDataHandler implements BotCommandHandler {

    private static final String KEY_SAMPLE = "MARKET.%s";
    private static final byte DEFAULT_ITEM_COUNT = 10;

    private final MessageSource messageSource;
    private final CoreServiceClientV1 coreServiceClient;
    private final ChatSessionService chatSessionService;


    @Autowired
    public MarketDataHandler(MessageSource messageSource,
                             CoreServiceClientV1 coreServiceClient,
                             ChatSessionService chatSessionService) {
        this.messageSource = messageSource;
        this.coreServiceClient = coreServiceClient;
        this.chatSessionService = chatSessionService;
    }


    @Override
    public Mono<? extends BotApiMethodMessage> handle(Message message, ChatSession session) {
        Steps step = Steps.values()[session.getContext().getStep()];
        Locale locale = ChatBotUtility.getLocale(message);
        session.getContext()
                .setCommand(BotCommand.MARKET);

        return Mono.just(switch (step) {

                    case EXCHANGE -> exchange(session, step, locale);

                    case SECURITIES_TYPE -> securitiesType(session, message.getText(), step, locale);

                    case SORTING -> sorting(session, message.getText(), step, locale);

                    case RETURN_RESULT -> returnResult(session, message.getText());

                    case PAGINATION -> pagination(session, message.getText());
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
        return createSendMessage(session.getChatId(), text);
    }

    private SendMessage securitiesType(ChatSession session, String messageText, Steps step, Locale locale) {
        session.getContext()
                .setStep(Steps.SORTING.ordinal());
        session.getAttributes()
                .put(Steps.EXCHANGE.name(), messageText);
        String text = messageSource.getMessage(KEY_SAMPLE.formatted(step), null, locale);
        return createSendMessage(session.getChatId(), text);
    }

    private SendMessage sorting(ChatSession session, String messageText, Steps step, Locale locale) {
        session.getContext()
                .setStep(Steps.RETURN_RESULT.ordinal());
        session.getAttributes()
                .put(Steps.SECURITIES_TYPE.name(), messageText);
        String text = messageSource.getMessage(KEY_SAMPLE.formatted(step), null, locale);
        return createSendMessage(session.getChatId(), text);
    }

    private SendMessage returnResult(ChatSession session, String messageText) {
        session.getContext()
                .setStep(Steps.PAGINATION.ordinal());
        session.getAttributes()
                .put(Steps.SORTING.name(), messageText);
        return getItems(session, 1);
    }

    private SendMessage pagination(ChatSession session, String messageText) {
        session.getContext()
                .setStep(Steps.PAGINATION.ordinal());
        int pageNumber;
        try {
            pageNumber = Integer.parseInt(messageText);
        } catch (NumberFormatException e) {

            session.getContext().setStep(0);
            session.getContext().setCommand(BotCommand.CLEAR);
            throw new IllegalArgumentException(e);
        }
        return getItems(session, pageNumber);
    }

    private SendMessage getItems(ChatSession session, int pageNumber) {
        //TODO temporary solution
        StringBuilder strBuild = new StringBuilder();
        ExchangeMarket exchange = ExchangeMarket.valueOf(session.getAttributes().get(Steps.EXCHANGE.name()));
        String securitiesType = session.getAttributes().get(Steps.SECURITIES_TYPE.name());
        String sortingParam = session.getAttributes().get(Steps.SORTING.name());
        MarketPage marketPage = new MarketPage(pageNumber, DEFAULT_ITEM_COUNT, sortingParam, null);

        List<? extends Securities> list;
        if (securitiesType.equalsIgnoreCase("Stocks")) {
            list = coreServiceClient.MARKET.stockList(exchange, marketPage);
        } else if (securitiesType.equalsIgnoreCase("Futures")) {
            list = coreServiceClient.MARKET.futuresList(exchange, marketPage);
        } else {
            throw new IllegalArgumentException("Securities type is incorrect: " + securitiesType);
        }
        for (var s : list) {
            strBuild.append(s).append('\n').append('\n');
        }
        return createSendMessage(session.getChatId(), strBuild.toString());
    }


    enum Steps {
        EXCHANGE,
        SECURITIES_TYPE,
        SORTING,
        RETURN_RESULT,
        PAGINATION
    }
}
