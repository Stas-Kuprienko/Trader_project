package com.project.dto;

import com.project.enums.Language;
import com.project.enums.Role;
import com.project.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") @NotBlank
    @Schema(pattern = "example@email\\.com")
    private String login;

    private Role role;

    private Language language;

    private String name;

    private Status status;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "YYYY-MM-DD")
    @Schema(pattern = "YYYY-MM-DD")
    private String createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    @Schema(pattern = "YYYY-MM-DD HH:MM:SS")
    private String updatedAt;


    @Builder
    public UserDto(UUID id,
                   String login,
                   Role role,
                   Language language,
                   String name,
                   Status status,
                   String createdAt,
                   String updatedAt) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.language = language;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserDto() {}
}
