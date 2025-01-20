package com.project.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAccountToken {

    @NotNull
    private String token;

    @JsonProperty("token_expires_at")
    @JsonFormat(pattern = "YYYY-MM-DD")
    @Schema(pattern = "YYYY-MM-DD")
    private String tokenExpiresAt;


    public UpdateAccountToken(String token, String tokenExpiresAt) {
        this.token = token;
        this.tokenExpiresAt = tokenExpiresAt;
    }

    public UpdateAccountToken() {}
}
