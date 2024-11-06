package com.anastasia.trade_project.dto;

import com.anastasia.trade_project.enums.Language;
import com.anastasia.trade_project.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") @NotBlank
    private String login;

    private Role role;

    private Language language;

    private String name;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updatedAt;


    @Builder
    public UserDto(Long id,
                   String login,
                   Role role,
                   Language language,
                   String name,
                   String createdAt,
                   String updatedAt) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.language = language;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserDto() {}
}
