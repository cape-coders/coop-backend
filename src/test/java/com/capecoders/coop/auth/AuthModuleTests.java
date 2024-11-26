package com.capecoders.coop.auth;

import com.capecoders.coop.auth.adaptors.InMemoryUserRepo;
import com.capecoders.coop.auth.core.CoopUser;
import com.capecoders.coop.auth.core.DefaultAdminService;
import com.capecoders.coop.auth.core.LoginService;
import com.capecoders.coop.auth.core.UserRepo;
import com.capecoders.coop.auth.core.sendinvite.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AuthModuleTests {
    private final TestJwtValidator testJwtValidator = new TestJwtValidator();
    private UserRepo userRepo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    public void setUp() {
        userRepo = new InMemoryUserRepo();
    }


    @Nested
    class DefaultAdmin {
        @Test
        public void createAdminUser_onStartup_whenAdminCredentialsAvailable() {
            String email = "wow@wow.com";
            new DefaultAdminService(userRepo, passwordEncoder, email, "12345678!!!");
            List<CoopUser> allCoopUsers = userRepo.getAllUsers();
            assertFalse(allCoopUsers.isEmpty());
            assertEquals(email, allCoopUsers.get(0).getEmail());
        }

        @Test
        public void createAdminUser_onStartup_shouldNotRecreateAdminUser() {
            String email = "wow@wow.com";
            String password = "12345678!!!";
            userRepo.saveUser(new CoopUser(null, email, password));
            new DefaultAdminService(userRepo, passwordEncoder, email, password);
            List<CoopUser> allCoopUsers = userRepo.getAllUsers();
            assertEquals(1, allCoopUsers.size());
        }

        @Test
        public void defaultAdminIsAbleToLogin() {
            String email = "wow@wow.com";
            String password = "12345678!!!";
            new DefaultAdminService(userRepo, passwordEncoder, email, password);
            String token = new LoginService(userRepo, passwordEncoder).login(email, password);
            assertNotNull(token);

            assertEquals(email, Objects.requireNonNull(testJwtValidator.isParsableJwt(token)).getBody().getSubject());
        }

        @Test
        public void defaultAdminIsNotAbleToLoginWithAWrongPassword() {
            String email = "wow@wow.com";
            String password = "notMyPasswordNotMyProblem";
            new DefaultAdminService(userRepo, passwordEncoder, email, "12345678!!!");
            String token = new LoginService(userRepo, passwordEncoder).login(email, password);
            assertNull(token);
        }
        @Test
        public void shouldNotBeAbleToLoginWithNonExistingUser() {
            String email = "wow@wow.com";
            String password = "notMyPasswordNotMyProblem";
            new DefaultAdminService(userRepo, passwordEncoder, email, "12345678!!!");
            String token = new LoginService(userRepo, passwordEncoder).login("someone@wow.com", password);
            assertNull(token);
        }
    }



    @Nested
    class UserInviteAcceptInvite {
        private InviteUserService inviteUserService;
        private LoginService loginService;
        private TestUserInviteRepo userInviteRepo;
        private TestSendUserInviteEmail sendUserInviteEmail;
        @BeforeEach
        public void setUp() {
            sendUserInviteEmail = new TestSendUserInviteEmail();
            userInviteRepo = new TestUserInviteRepo();
            loginService = new LoginService(userRepo, passwordEncoder);
            inviteUserService = new InviteUserService(sendUserInviteEmail, userInviteRepo, userRepo, passwordEncoder);
        }
        @Test
        public void invitingAUser_sendsThatUserAnEmailWithALinkToJoin() {
            inviteUserService.invite("test@wow.com");
            assertEquals("test@wow.com", sendUserInviteEmail.emailSent().email());
            assertTrue(sendUserInviteEmail.emailSent().inviteLink().contains("http://localhost:8080/accept-invite?code="));
        }

        @ParameterizedTest
        @ValueSource(strings = {"notTheRealCode"})
        @NullAndEmptySource
        public void cannotAcceptInviteWithWrongCode(String aBadCode) {
            inviteUserService.invite("test@wow.com");
            assertThrows(InviteUserService.BadCodeException.class, () -> {
                inviteUserService.accept("test@wow.com", "password", aBadCode);
            });
        }

        @Test
        public void cannotAcceptInviteWithValidCodeBadWrongEmail() {
            SendUserInviteResponse invite = inviteUserService.invite("test@wow.com");
            assertThrows(InviteUserService.WrongEmailException.class, () -> {
                inviteUserService.accept("someHacker@badpeople.com", "password", invite.code());
            });
        }

        @ParameterizedTest
        @NullAndEmptySource
        public void cannotAcceptInviteWithValidEmailButMissingPassword(String password) {
            SendUserInviteResponse invite = inviteUserService.invite("test@wow.com");
            assertThrows(InviteUserService.InvalidPassword.class, () -> {
                inviteUserService.accept(invite.email(), password, invite.code());
            });
        }

        @Test
        public void canLoginAfterAcceptingInvite() {
            SendUserInviteResponse inviteResponse = inviteUserService.invite("test@wow.com");
            inviteUserService.accept("test@wow.com", "password", inviteResponse.code());
            String jwt = loginService.login("test@wow.com", "password");
            assertNotNull(jwt);
            assertEquals("test@wow.com", Objects.requireNonNull(testJwtValidator.isParsableJwt(jwt)).getBody().getSubject());
        }

        @Test
        public void shouldNotBeAbleToInviteExistingUser() {
            SendUserInviteResponse invite = inviteUserService.invite("test@wow.com");
            inviteUserService.accept(invite.email(), "password", invite.code());
            assertThrows(InviteUserService.AlreadyExistsException.class, () -> {
                inviteUserService.invite("test@wow.com");
            });
        }

    }


//    should do what with email if can't send out
}
