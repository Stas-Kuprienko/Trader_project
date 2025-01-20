package com.project.ui.service;

import com.project.dto.UserDto;
import com.project.forms.NewUser;
import java.util.UUID;

public interface UserDataService {

    UserDto signUp(NewUser newUser);

    UserDto getById(UUID id);
}
