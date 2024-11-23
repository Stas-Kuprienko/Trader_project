package com.anastasia.trade_project.core_client;

import org.springframework.web.client.RestClient;

public class UsersResource {

    private static final String resourceUrl = "/users";

    private final RestClient restClient;

    UsersResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


}
