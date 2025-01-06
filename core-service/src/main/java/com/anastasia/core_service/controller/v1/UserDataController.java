package com.anastasia.core_service.controller.v1;

import com.anastasia.core_service.entity.User;
import com.anastasia.core_service.service.UserDataService;
import com.anastasia.core_service.service.converter.UserConverter;
import com.anastasia.core_service.utility.JwtUtility;
import com.anastasia.trade_project.dto.UserDto;
import com.anastasia.trade_project.enums.Status;
import com.anastasia.trade_project.forms.ErrorDto;
import com.anastasia.trade_project.forms.NewUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Tag(name = "User Data")
@RestController
@RequestMapping("/api/v1/users")
public class UserDataController {

    private final UserDataService userDataService;
    private final UserConverter userConverter;


    @Autowired
    public UserDataController(UserDataService userDataService, UserConverter userConverter) {
        this.userDataService = userDataService;
        this.userConverter = userConverter;
    }


    @Operation(summary = "Register new user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User is registered", content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @PostMapping
    public Mono<UserDto> signUp(@RequestBody @Valid NewUser newUser) {
        return userDataService
                .singUp(newUser.getLogin(),
                        newUser.getPassword(),
                        newUser.getName(),
                        newUser.getLanguage())
                .flatMap(userConverter::toDto);
    }


    @Operation(summary = "Find user by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User data", content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "User is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @GetMapping
    public Mono<UserDto> findById(@AuthenticationPrincipal Jwt jwt) {
        return Mono.just(JwtUtility.extractUserId(jwt))
                .flatMap(userDataService::getById)
                .flatMap(userConverter::toDto);
    }


    @Operation(summary = "Update user data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User is updated", content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "User is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @PutMapping
    public Mono<UserDto> update(@AuthenticationPrincipal Jwt jwt, @RequestBody UserDto userDto) {
        return Mono.zip(Mono.just(JwtUtility.extractUserId(jwt)),
                        userConverter.toEntity(userDto))
                .flatMap(tuple -> {
                    UUID id = tuple.getT1();
                    User user = tuple.getT2();
                    return userDataService.update(id, user);
                })
                .flatMap(userConverter::toDto);
    }


    @Operation(summary = "Delete user")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "User is deleted", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "User is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @DeleteMapping
    public Mono<?> deleteById(@AuthenticationPrincipal Jwt jwt, @RequestParam(defaultValue = "false") boolean isHard) {
        return Mono.just(JwtUtility.extractUserId(jwt))
                .flatMap(id -> {
                    if (isHard) {
                        return userDataService
                                .delete(id)
                                .map(unused -> ResponseEntity.noContent().build());
                    } else {
                        return userDataService
                                .setStatus(id, Status.DISABLED)
                                .map(unused -> ResponseEntity.noContent().build());
                    }
                });
    }
}
