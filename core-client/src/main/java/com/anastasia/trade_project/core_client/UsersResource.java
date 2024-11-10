package com.anastasia.trade_project.core_client;

import com.anastasia.trade_project.dto.UserDto;
import com.anastasia.trade_project.util.MyRestClient;
import org.springframework.web.client.RestTemplate;

public class UsersResource {

    private static final String resourceUrl = "/users";

    private final MyRestClient<UserDto> restClient;

    public UsersResource(RestTemplate restTemplate, String baseUrl) {
        this.restClient = new MyRestClient<>(restTemplate, baseUrl + resourceUrl, UserDto.class);
    }


}
