package com.anastasia.core_service.controller.v1;

import com.anastasia.core_service.service.UserDataService;
import com.anastasia.core_service.service.converter.UserConverter;
import com.anastasia.trade_project.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserDataController {

    private final UserConverter userConverter;
    private final UserDataService userDataService;

    @Autowired
    public UserDataController(UserConverter userConverter, UserDataService userDataService) {
        this.userConverter = userConverter;
        this.userDataService = userDataService;
    }


    @PostMapping
    public Mono<UserDto> createNew(@RequestBody UserDto userDto) {
        return userConverter
                .toEntity(userDto)
                .flatMap(userDataService::create)
                .flatMap(userConverter::toDto);
    }

    @GetMapping("/{id}")
    public Mono<UserDto> findById(@PathVariable UUID id) {
        return userDataService
                .getById(id)
                .flatMap(userConverter::toDto);
    }
}
