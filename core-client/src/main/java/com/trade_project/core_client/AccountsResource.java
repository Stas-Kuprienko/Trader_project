package com.trade_project.core_client;

import com.trade_project.dto.AccountDto;
import com.trade_project.forms.NewAccount;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountsResource extends HttpError404Handler {

    private static final String resourceUrl = "/accounts";

    private final RestClient restClient;

    AccountsResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public AccountDto createNew(NewAccount newAccount) {
        return restClient
                .post()
                .body(newAccount)
                .retrieve()
                .body(AccountDto.class);
    }

    public Optional<AccountDto> getById(UUID id) {
        return catch404(
                () -> restClient
                .get()
                .uri(CoreServiceClientV1.uriById(id))
                .retrieve()
                .body(AccountDto.class));
    }

    public List<AccountDto> getAllByUser() {
        return restClient
                .get()
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
