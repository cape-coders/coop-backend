package com.capecoders.coop.auth.core.sendinvite;

import com.capecoders.coop.auth.core.CoopUser;
import com.capecoders.coop.auth.core.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class InviteUserService {
    private final SendUserInviteEmail sendUserInviteEmail;
    private final UserInviteRepo userInviteRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public InviteUserService(SendUserInviteEmail sendUserInviteEmail, UserInviteRepo userInviteRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.sendUserInviteEmail = sendUserInviteEmail;
        this.userInviteRepo = userInviteRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public SendUserInviteResponse invite(String emailAddress) {
        CoopUser userByEmail = userRepo.getUserByEmail(emailAddress);
        if (userByEmail != null) {
            throw new AlreadyExistsException();
        }

        UserInvite userInvite = UserInvite.create(emailAddress);
        Boolean sent = sendUserInviteEmail.sendEmail(
            new SendUserInviteRequest(
                emailAddress,
                "Click this link to join! <a href=\"http://localhost:8080/accept-invite?code=\">Join now!</a>" + userInvite.getCode(),
                "You're invited to the Coop!",
                "http://localhost:8080/accept-invite?code=" + userInvite.getCode()
            ));
        if (sent) {
            userInvite.emailSentSuccessfully();
        }
        userInvite = userInviteRepo.save(userInvite);
        return new SendUserInviteResponse(emailAddress, userInvite.getCode(), userInvite.getEmailSentAt());
    }

    public void accept(String email, String password, String code) {
        UserInvite inviteRepoByEmail = userInviteRepo.getByEmail(email);
        if (inviteRepoByEmail == null) {
            throw new WrongEmailException();
        }
        if (!Objects.equals(code, inviteRepoByEmail.getCode())) {
            throw new BadCodeException();
        }
        if (password == null || password.isEmpty()) {
            throw new InvalidPassword();
        }

        userRepo.saveUser(new CoopUser(null, email, passwordEncoder.encode(password)));

    }

    public static class BadCodeException extends RuntimeException {
        @Override
        public String getMessage() {
            return "Code sent is invalid";
        }
    }

    public static class WrongEmailException extends RuntimeException {
        @Override
        public String getMessage() {
            return "Email address is invalid";
        }
    }

    public static class InvalidPassword extends RuntimeException {
        @Override
        public String getMessage() {
            return "Password is invalid";
        }
    }

    public static class AlreadyExistsException extends RuntimeException {
        @Override
        public String getMessage() {
            return "User already exists";
        }
    }
}
