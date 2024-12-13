package core_client_reactive;

import org.springframework.web.reactive.function.client.WebClient;

public class UsersResource {

    private static final String resourceUrl = "/users";

    private final WebClient webClient;

    UsersResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


}
