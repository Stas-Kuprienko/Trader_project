package com.trader_project.ui_service.service;

import com.trade_project.dto.UserDto;
import com.trade_project.forms.NewUser;
import java.util.UUID;

public interface UserDataService {

    UserDto signUp(NewUser newUser);

    UserDto getById(UUID id);
}
