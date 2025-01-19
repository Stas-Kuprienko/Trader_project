package com.trader_project.smart_service.domain.price_stream.finam_grpc;

import com.trader_project.smart_service.Smart;
import com.trader_project.smart_service.domain.price_stream.PriceStream;
import com.trader_project.smart_service.domain.price_stream.PriceStreamProvider;
import com.trader_project.smart_service.domain.strategy.TradeStrategy;
import com.trader_project.smart_service.util.EventDriver;
import com.trader_project.smart_service.util.GrpcCredentials;
import grpc.tradeapi.v1.EventsGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import proto.tradeapi.v1.Events;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class FinamPriceStreamProvider implements PriceStreamProvider {

    private static final String ORDER_BOOK_REQUEST_ID = "32ef5786-e887";

    private final ScheduledExecutorService scheduler;
    private final EventsGrpc.EventsStub stub;
    private final Map<Smart.Security, PriceStreamBox> priceStreamStore;


    @Autowired
    public FinamPriceStreamProvider(@Qualifier("scheduler") ScheduledExecutorService scheduler,
                                    @Qualifier("managedChannelFinam") ManagedChannel managedChannelFinam,
                                    @Qualifier("finamGrpcCredentials") GrpcCredentials finamGrpcCredentials) {
        this.scheduler = scheduler;
        this.stub = EventsGrpc.newStub(managedChannelFinam).withCallCredentials(finamGrpcCredentials);
        priceStreamStore = new ConcurrentHashMap<>();
    }


    @Override
    public PriceStream subscribe(Smart.Security security, TradeStrategy strategy) {
        PriceStreamBox priceStreamBox = priceStreamStore.get(security);
        if (priceStreamBox == null) {
            var subscribeRequest = buildSubscribeRequest(security);
            var unsubscribeRequest = buildUnsubscribeRequest(security);
            priceStreamBox = initPriceStream(subscribeRequest, unsubscribeRequest);
            priceStreamStore.put(security, priceStreamBox);
        }
        priceStreamBox.priceStream.subscribe(strategy);
        return priceStreamBox.priceStream;
    }

    @Override
    public void unsubscribe(Smart.Security security, TradeStrategy strategy) {
        PriceStreamBox priceStreamBox = priceStreamStore.get(security);
        if (priceStreamBox != null) {
            priceStreamBox.priceStream.unsubscribe(strategy);
            if (priceStreamBox.priceStream.isUseless()) {
                var observer = stub.getEvents(priceStreamBox.streamObserver);
                observer.onCompleted();
                observer.onNext(priceStreamBox.unsubscription);
                observer.onCompleted();
                priceStreamStore.remove(security);
            }
        }
    }


    private PriceStreamBox initPriceStream(Events.SubscriptionRequest subscription,
                                           Events.SubscriptionRequest unsubscription) {

        FinamOrderBookStreamObserver streamObserver = new FinamOrderBookStreamObserver(new FinamOrderBookEntry());
        var streamObserverDriver = new EventDriver(scheduler,
                () -> stub.getEvents(streamObserver).onNext(subscription));

        PriceStream priceStream =  new GrpcFinamPriceStream(streamObserver.entry(), streamObserverDriver);
        return new PriceStreamBox(priceStream, subscription, unsubscription, streamObserver);
    }

    private Events.SubscriptionRequest buildSubscribeRequest(Smart.Security security) {
        var orderBookSubscribeRequest = Events.OrderBookSubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityCode(security.getTicker())
                .setSecurityBoard(security.getBoard())
                .build();
        return Events.SubscriptionRequest.newBuilder()
                .setOrderBookSubscribeRequest(orderBookSubscribeRequest).build();
    }

    private Events.SubscriptionRequest buildUnsubscribeRequest(Smart.Security security) {
        var orderBookUnsubscribeRequest = Events.OrderBookUnsubscribeRequest.newBuilder()
                .setRequestId(ORDER_BOOK_REQUEST_ID)
                .setSecurityCode(security.getTicker())
                .setSecurityBoard(security.getBoard())
                .build();
        return Events.SubscriptionRequest.newBuilder()
                .setOrderBookUnsubscribeRequest(orderBookUnsubscribeRequest).build();
    }


    record PriceStreamBox(PriceStream priceStream,
                          Events.SubscriptionRequest subscription,
                          Events.SubscriptionRequest unsubscription,
                          StreamObserver<Events.Event> streamObserver) {}
}
