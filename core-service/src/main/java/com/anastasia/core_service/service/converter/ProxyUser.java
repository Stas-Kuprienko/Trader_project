package com.anastasia.core_service.service.converter;

import com.anastasia.core_service.entity.user.Account;
import com.anastasia.core_service.entity.user.User;
import com.anastasia.core_service.service.UserDataService;
import com.anastasia.trade_project.enums.Language;
import com.anastasia.trade_project.enums.Role;
import com.anastasia.trade_project.enums.Status;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ProxyUser extends User {

    private final UserDataService userDataService;
    private User user = null;

    public ProxyUser(Long id, UserDataService userDataService) {
        setId(id);
        this.userDataService = userDataService;
    }

    //TODO EXPERIMENTAL SOLUTION. NEED TO TEST

    @Override
    public Role getRole() {
        return user().getRole();
    }

    @Override
    public Language getLanguage() {
        return user().getLanguage();
    }

    @Override
    public String getName() {
        return user().getName();
    }

    @Override
    public Status getStatus() {
        return user().getStatus();
    }

    @Override
    public List<Account> getAccounts() {
        return user().getAccounts();
    }

    @Override
    public LocalDate getCreatedAt() {
        return user().getCreatedAt();
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return user().getUpdatedAt();
    }

    @Override
    public void setLogin(String login) {
        user().setLogin(login);
    }

    @Override
    public void setRole(Role role) {
        user().setRole(role);
    }

    @Override
    public void setLanguage(Language language) {
        user().setLanguage(language);
    }

    @Override
    public void setName(String name) {
        user().setName(name);
    }

    @Override
    public void setStatus(Status status) {
        user().setStatus(status);
    }

    @Override
    public void setAccounts(List<Account> accounts) {
        user().setAccounts(accounts);
    }

    @Override
    public void setCreatedAt(LocalDate createdAt) {
        user().setCreatedAt(createdAt);
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        user().setUpdatedAt(updatedAt);
    }


    private User user() {
        if (this.user == null) {
            synchronized (this) {
                this.user = userDataService.getById(getId()).block(Duration.of(10, ChronoUnit.SECONDS));
            }
        }
        return this.user;
    }
}
