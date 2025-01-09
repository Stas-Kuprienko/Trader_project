package com.anastasia.ui_service.service;

import com.anastasia.trade_project.dto.UserDto;
import com.anastasia.trade_project.forms.NewUser;
import java.util.UUID;

public interface UserDataService {

    UserDto signUp(NewUser newUser);

    UserDto getById(UUID id);
}
