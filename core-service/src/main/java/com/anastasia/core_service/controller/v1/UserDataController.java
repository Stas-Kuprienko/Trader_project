package com.anastasia.core_service.controller.v1;

import com.anastasia.core_service.domain.credentials.CredentialsNode;
import com.anastasia.core_service.service.UserDataService;
import com.anastasia.core_service.service.converter.UserConverter;
import com.anastasia.trade_project.dto.UserDto;
import com.anastasia.trade_project.enums.Status;
import com.anastasia.trade_project.forms.ErrorDto;
import com.anastasia.trade_project.forms.RegistrationForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserDataController {

    private final UserConverter userConverter;
    private final UserDataService userDataService;
    private final CredentialsNode credentialsNode;


    @Autowired
    public UserDataController(UserConverter userConverter,
                              UserDataService userDataService,
                              CredentialsNode credentialsNode) {
        this.userConverter = userConverter;
        this.userDataService = userDataService;
        this.credentialsNode = credentialsNode;
    }



    @Operation(summary = "Register new user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User is registered", content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @PostMapping
    public Mono<UserDto> signUp(@RequestBody RegistrationForm registration) {
        return credentialsNode
                .signUp(registration)
                .flatMap(userDataService::create)
                .flatMap(userConverter::toDto);
    }


    @Operation(summary = "Find user by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User data", content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "User is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @GetMapping("/{id}")
    public Mono<UserDto> findById(@PathVariable UUID id) {
        return userDataService
                .getById(id)
                .flatMap(userConverter::toDto);
    }


    @Operation(summary = "Update user data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User is updated", content = @Content(schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "User is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @PutMapping("/{id}")
    public Mono<UserDto> update(@PathVariable UUID id, @RequestBody UserDto userDto) {
        return userConverter
                .toEntity(userDto)
                .flatMap(user -> userDataService.update(id, user))
                .flatMap(userConverter::toDto);
    }


    @Operation(summary = "Delete user")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "User is deleted", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "User is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @DeleteMapping("/{id}")
    public Mono<?> delete(@PathVariable UUID id, @RequestParam(defaultValue = "false") boolean isHard) {
        if (isHard) {
            return userDataService
                    .delete(id)
                    .map(unused -> ResponseEntity.noContent());
        } else {
            return userDataService
                    .setStatus(id, Status.DISABLED)
                    .map(unused -> ResponseEntity.noContent());
        }
    }
}
