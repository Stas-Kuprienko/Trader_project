package com.anastasia.trade_project.core_client;

import org.springframework.web.client.RestTemplate;

public class CoreServiceClientV1 {

    public final UsersResource USERS;
    public final AccountsResource ACCOUNTS;


    public CoreServiceClientV1(RestTemplate restTemplate, String baseUrl) {
        baseUrl += "/api/v1";
        USERS = new UsersResource(restTemplate, baseUrl);
        ACCOUNTS = new AccountsResource(restTemplate, baseUrl);
    }
}
