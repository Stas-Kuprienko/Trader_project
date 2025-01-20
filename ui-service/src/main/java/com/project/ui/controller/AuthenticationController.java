package com.project.ui.controller;

import com.project.dto.UserDto;
import com.project.forms.NewUser;
import com.project.ui.configuration.UiServiceConfig;
import com.project.ui.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserDataService userDataService;

    @Autowired
    public AuthenticationController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }


    @GetMapping("/login")
    public String login() {
        return UiServiceConfig.LOGIN_PAGE;
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return UiServiceConfig.SIGN_UP_PAGE;
    }

    @PostMapping("/login")
    public String loginHandle(@RequestBody NewUser newUser, Model model) {
        UserDto user = userDataService.signUp(newUser);
        model.addAttribute("user", user);
        return UiServiceConfig.REDIRECT
        + '+' + UiServiceConfig.MENU_PAGE;
    }
}
