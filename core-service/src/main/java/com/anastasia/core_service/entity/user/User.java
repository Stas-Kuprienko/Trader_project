package com.anastasia.core_service.entity.user;

import com.anastasia.core_service.entity.enums.Language;
import com.anastasia.core_service.entity.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Table("users")
public class User {

    @Id
    private Long id;

    @Email
    private String login;

    private Role role;

    private Language language;

    private String name;

    private List<Account> accounts = new ArrayList<>();


    @Builder
    public User(Long id,
                String login,
                Role role,
                Language language,
                String name,
                List<Account> accounts) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.language = language;
        this.name = name;
        this.accounts = accounts;
    }

    public User() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(login, user.login)&&
                role == user.role &&
                language == user.language &&
                Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, role, language, name);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", language=" + language +
                ", name='" + name + '\'' +
                '}';
    }
}