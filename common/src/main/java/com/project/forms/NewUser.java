package com.project.forms;

import com.project.enums.Language;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class NewUser {

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") @NotBlank
    @Schema(pattern = "example@email\\.com")
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotNull
    private Language language;


    @Builder
    public NewUser(String login,
                   String password,
                   String name,
                   Language language) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.language = language;
    }

    public NewUser() {}
}
