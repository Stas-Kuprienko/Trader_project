package com.anastasia.ui_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends AbstractSecurityWebApplicationInitializer implements WebMvcConfigurer {

    //    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(customizer -> customizer
//                        .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll())
//                .oauth2Login()
//                .loginPage("/login")
//                .defaultSuccessUrl("/", true)
//                .failureUrl("/login?error=true");
//    }
}
