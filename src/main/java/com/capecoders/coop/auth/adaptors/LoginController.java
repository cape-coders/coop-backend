package com.capecoders.coop.auth.adaptors;

import com.capecoders.coop.auth.EmailAndPassword;
import com.capecoders.coop.auth.core.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("login")
    public String login(@RequestBody EmailAndPassword request) {
        return loginService.login(request.email(), request.password());
    }
}
