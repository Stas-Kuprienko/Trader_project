package com.anastasia.trade_project.form;

import com.anastasia.trade_project.enums.Language;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class RegistrationForm {

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") @NotBlank
    private String login;

    private String password;

    private String name;

    private Language language;


    @Builder
    public RegistrationForm(String login,
                            String password,
                            String name,
                            Language language) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.language = language;
    }

    public RegistrationForm() {}
}
