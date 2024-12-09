package com.anastasia.trade_project.core_client;

public class CoreServiceClientV1 {

    public final UsersResource USERS;
    public final AccountsResource ACCOUNTS;
    public final MarketDataResource MARKET;


    public CoreServiceClientV1(String baseUrl) {
        baseUrl += "/api/v1";
        USERS = new UsersResource(baseUrl);
        ACCOUNTS = new AccountsResource(baseUrl);
        MARKET = new MarketDataResource(baseUrl);
    }


    public static String uriById(Object id) {
        return "/%s".formatted(id.toString());
    }
}
