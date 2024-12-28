package com.anastasia.core_service.entity;

import com.anastasia.trade_project.enums.Language;
import com.anastasia.trade_project.enums.Role;
import com.anastasia.trade_project.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter
@Table(name = "users", schema = "person")
public class User {

    @Id
    private UUID id;

    private String login;

    private Role role;

    private Language language;

    private String name;

    private Status status;

    @Column("created_at")
    private LocalDate createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;


    @Builder
    public User(UUID id,
                String login,
                Role role,
                Language language,
                String name,
                Status status,
                LocalDate createdAt,
                LocalDateTime updatedAt) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.language = language;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User() {}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(login, user.login)&&
                role == user.role &&
                language == user.language &&
                Objects.equals(name, user.name) &&
                Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, role, language, name, createdAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", language=" + language +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}