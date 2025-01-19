package com.trader_project.telegram_bot.service.impl;

import com.trader_project.telegram_bot.service.UserDataService;
import com.trade_project.core_client.CoreServiceClientV1;
import com.trade_project.dto.UserDto;
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
