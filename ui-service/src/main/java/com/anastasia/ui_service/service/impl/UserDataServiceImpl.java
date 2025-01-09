package com.anastasia.ui_service.service.impl;

import com.anastasia.trade_project.core_client.CoreServiceClientV1;
import com.anastasia.trade_project.dto.UserDto;
import com.anastasia.trade_project.exception.NotFoundException;
import com.anastasia.trade_project.forms.NewUser;
import com.anastasia.ui_service.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserDataServiceImpl implements UserDataService {

    private final CoreServiceClientV1 coreServiceClient;

    @Autowired
    public UserDataServiceImpl(CoreServiceClientV1 coreServiceClient) {
        this.coreServiceClient = coreServiceClient;
    }


    @Override
    public UserDto signUp(NewUser newUser) {
        return coreServiceClient
                .USERS
                .signUp(newUser);
    }

    @Override
    public UserDto getById(UUID id) {
        return coreServiceClient
                .USERS
                .getById(id)
                .orElseThrow(() -> NotFoundException.byID(UserDto.class, id));
    }
}
