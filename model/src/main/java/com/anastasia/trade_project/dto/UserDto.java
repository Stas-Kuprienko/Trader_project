package com.anastasia.trade_project.dto;

import com.anastasia.trade_project.enums.Language;
import com.anastasia.trade_project.enums.Role;
import com.anastasia.trade_project.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
public class UserDto {

    private Long id;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") @NotBlank
    private String login;

    private Role role;

    private Language language;

    private String name;

    private Status status;

    private List<AccountDto> accounts;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "YYYY-MM-DD")
    @Pattern(regexp = "(\\d{4})-(\\d{2})-(\\d{2})")
    private String createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "YYYY-MM-DD HH:MM:SS")
    @Pattern(regexp = "(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})")
    private String updatedAt;


    @Builder
    public UserDto(Long id,
                   String login,
                   Role role,
                   Language language,
                   String name,
                   Status status,
                   List<AccountDto> accounts,
                   String createdAt,
                   String updatedAt) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.language = language;
        this.name = name;
        this.status = status;
        this.accounts = accounts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UserDto() {}
}
