package com.anastasia.core_service.controller.v1;

import com.anastasia.core_service.service.AccountService;
import com.anastasia.core_service.service.converter.AccountConverter;
import com.anastasia.core_service.utility.JwtUtility;
import com.anastasia.trade_project.dto.AccountDto;
import com.anastasia.trade_project.forms.ErrorDto;
import com.anastasia.trade_project.forms.NewAccount;
import com.anastasia.trade_project.forms.UpdateAccountToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Accounts")
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountConverter accountConverter;

    @Autowired
    public AccountController(AccountService accountService,
                             AccountConverter accountConverter) {
        this.accountService = accountService;
        this.accountConverter = accountConverter;
    }


    @Operation(summary = "Create new account")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Account is created", content = @Content(schema = @Schema(implementation = AccountDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @PostMapping
    public Mono<AccountDto> createNew(@RequestBody NewAccount newAccount) {
        return accountConverter.toEntity(newAccount)
                .flatMap(accountService::create)
                .flatMap(accountConverter::toDto);
    }


    @Operation(summary = "Find all accounts by user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Account list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = AccountDto.class)))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @GetMapping
    public Flux<AccountDto> findAllByUser(@AuthenticationPrincipal Jwt jwt) {
        return Mono.just(JwtUtility.extractUserId(jwt))
                .flatMapMany(accountService::getAllByUserId)
                .flatMap(accountConverter::toDto);
    }


    @Operation(summary = "Find account by ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Account data", content = @Content(schema = @Schema(implementation = AccountDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "Account is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @GetMapping("/{id}")
    public Mono<AccountDto> findById(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        return Mono.just(JwtUtility.extractUserId(jwt))
                .flatMap(userId -> accountService.getById(id, userId))
                .flatMap(accountConverter::toDto);
    }


    @Operation(summary = "Update account token")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Account token is updated", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "Account is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @PutMapping("/{id}")
    public Mono<ResponseEntity<?>> updateToken(@AuthenticationPrincipal Jwt jwt,
                                               @PathVariable UUID id,
                                               @RequestBody UpdateAccountToken update) {
        return Mono.just(JwtUtility.extractUserId(jwt))
                .flatMap(userId -> {
                    LocalDate expiresAt = accountConverter.stringToLocalDate(update.getTokenExpiresAt());
                    return accountService.updateToken(id, userId, update.getToken(), expiresAt);
                })
                .map(unused -> ResponseEntity.ok().build());
    }


    @Operation(summary = "Delete account")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Account is deleted", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "Account is not found", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<?>> deleteById(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        return Mono.just(JwtUtility.extractUserId(jwt))
                .flatMap(userId -> accountService.delete(id, userId))
                .map(unused -> ResponseEntity.noContent().build());
    }
}
