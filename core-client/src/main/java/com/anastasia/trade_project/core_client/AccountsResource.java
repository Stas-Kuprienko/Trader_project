package com.anastasia.trade_project.core_client;

import com.anastasia.trade_project.dto.AccountDto;
import org.springframework.web.client.RestClient;
import java.util.UUID;

public class AccountsResource {

    private static final String resourceUrl = "/accounts";

    private final RestClient restClient;

    AccountsResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public AccountDto save(AccountDto account) {
        return restClient
                .post()
                .body(account)
                .retrieve()
                .body(AccountDto.class);
    }

    public AccountDto getById(UUID id) {
        return restClient
                .get()
                .uri(CoreServiceClientV1.uriById(id))
                .retrieve()
                .body(AccountDto.class);
    }
}
