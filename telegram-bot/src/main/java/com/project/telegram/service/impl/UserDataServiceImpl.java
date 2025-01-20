package com.project.telegram.service.impl;

import com.project.telegram.service.UserDataService;
import com.project.core_client.CoreServiceClientV1;
import com.project.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDataServiceImpl implements UserDataService {

    private final CoreServiceClientV1 coreServiceClient;

    @Autowired
    public UserDataServiceImpl(CoreServiceClientV1 coreServiceClient) {
        this.coreServiceClient = coreServiceClient;
    }


    @Override
    public Mono<UserDto> getUserByChatId(long chatId) {
        return null;
    }
}
