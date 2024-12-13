package core_client_reactive;

import com.anastasia.trade_project.dto.AccountDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.UUID;

public class AccountsResource {

    private static final String resourceUrl = "/accounts";

    private final WebClient webClient;

    AccountsResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public Mono<AccountDto> save(AccountDto account) {
        return webClient
                .post()
                .bodyValue(account)
                .retrieve()
                .bodyToMono(AccountDto.class);
    }

    public Mono<AccountDto> getById(UUID id) {
        return webClient
                .get()
                .uri(CoreServiceClientReactiveV1.uriById(id))
                .retrieve()
                .bodyToMono(AccountDto.class);
    }
}
