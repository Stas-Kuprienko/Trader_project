package com.project.core_client;

import com.project.dto.UserDto;
import com.project.forms.NewUser;
import org.springframework.web.client.RestClient;
import java.util.Optional;
import java.util.UUID;

public class UsersResource extends HttpError404Handler {

    private static final String resourceUrl = "/users";

    private final RestClient restClient;

    UsersResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public UserDto signUp(NewUser newUser) {
        return restClient
                .post()
                .body(newUser)
                .retrieve()
                .body(UserDto.class);
    }

    public Optional<UserDto> getById(UUID id) {
        return catch404(
                () -> restClient
                .get()
                .uri(CoreServiceClientV1.uriById(id))
                .retrieve()
                .body(UserDto.class));
    }
}
