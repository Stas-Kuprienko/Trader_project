package com.anastasia.trade_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
public class UserDto {

    private Long id;

    @Email
    private String login;

    private String role;

    private String language;

    private String name;

    @Pattern(regexp = "^(0[1-9]|1\\d|2[0-8]|29(?=/\\d\\d/(?!1[01345789]00|2[1235679]00)\\d\\d(?:[02468][048]|[13579][26]))|30(?!/02)|31(?=/0[13578]|/1[02]))-(0[1-9]|1[0-2])-([12]\\d{3})$")
    @JsonProperty("created_at")
    private String createdAt;

    @Pattern(regexp = "^(0[1-9]|1\\d|2[0-8]|29(?=/\\d\\d/(?!1[01345789]00|2[1235679]00)\\d\\d(?:[02468][048]|[13579][26]))|30(?!/02)|31(?=/0[13578]|/1[02]))-(0[1-9]|1[0-2])-([12]\\d{3}) ([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$")
    @JsonProperty("updated_at")
    private String updatedAt;


    @Builder
    public UserDto(Long id,
                   String login,
                   String role,
                   String language,
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
