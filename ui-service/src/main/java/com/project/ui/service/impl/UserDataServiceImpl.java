package com.project.ui.service.impl;

import com.project.core_client.CoreServiceClientV1;
import com.project.dto.UserDto;
import com.project.exception.NotFoundException;
import com.project.forms.NewUser;
import com.project.ui.service.UserDataService;
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
