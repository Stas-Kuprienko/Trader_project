package com.anastasia.ui_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            String loginPage = "/auth/%s".formatted(UiServiceConfig.LOGIN_PAGE);
            http
                    .authorizeHttpRequests(exchange -> exchange

                            .requestMatchers("/**").permitAll()
                    ).oauth2Login(
                            login -> login.loginPage(loginPage)
                                    .defaultSuccessUrl("/menu", true)
                                    .failureUrl(loginPage + "?error=true")
                    ).logout(logout -> logout.logoutSuccessUrl("/"));

            return http.build();
        }
}
