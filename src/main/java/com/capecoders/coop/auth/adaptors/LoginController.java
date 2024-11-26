package com.capecoders.coop.auth.adaptors;

import com.capecoders.coop.auth.EmailAndPassword;
import com.capecoders.coop.auth.core.LoginService;
import com.capecoders.coop.auth.core.sendinvite.InviteUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private final LoginService loginService;
    private final InviteUserService inviteUserService;

    public LoginController(LoginService loginService, InviteUserService inviteUserService) {
        this.loginService = loginService;
        this.inviteUserService = inviteUserService;
    }


    @PostMapping("login")
    public String login(@RequestBody EmailAndPassword request) {
        return loginService.login(request.email(), request.password());
    }

    @PostMapping("invite")
    public void invite(@RequestBody InviteUserPayload request) {
        inviteUserService.invite(request.email());
    }
}
