package com.anastasia.trade_project.core_client;

import com.anastasia.trade_project.dto.AccountDto;
import com.anastasia.trade_project.util.MyRestClient;
import org.springframework.web.client.RestTemplate;
import java.util.UUID;

public class AccountsResource {

    private static final String resourceUrl = "/accounts";

    private final MyRestClient<AccountDto> restClient;

    public AccountsResource(RestTemplate restTemplate, String baseUrl) {
        this.restClient = new MyRestClient<>(restTemplate, baseUrl + resourceUrl, AccountDto.class);
    }


    public AccountDto save(AccountDto account) {
        return restClient
                .post()
                .body(account)
                .complete();
    }

    public AccountDto getById(UUID id) {
        return restClient
                .get()
                .uri(MyRestClient.uriById(id))
                .complete();
    }
}
