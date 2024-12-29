package com.anastasia.trade_project.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateAccountToken {

    @NotNull
    private String token;

    @JsonProperty("token_expires_at")
    @JsonFormat(pattern = "YYYY-MM-DD")
    @Pattern(regexp = "(\\d{4})-(\\d{2})-(\\d{2})")
    private String tokenExpiresAt;


    public UpdateAccountToken(String token, String tokenExpiresAt) {
        this.token = token;
        this.tokenExpiresAt = tokenExpiresAt;
    }

    public UpdateAccountToken() {}
}
