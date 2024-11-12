package com.capecoders.coop.auth;

import com.capecoders.coop.auth.adaptors.InMemoryUserRepo;
import com.capecoders.coop.auth.core.CoopUser;
import com.capecoders.coop.auth.core.DefaultAdminService;
import com.capecoders.coop.auth.core.LoginService;
import com.capecoders.coop.auth.core.UserRepo;
import com.capecoders.coop.auth.core.sendinvite.InviteUserService;
import com.capecoders.coop.auth.core.sendinvite.TestSendUserInviteEmail;
import com.capecoders.coop.auth.core.sendinvite.TestUserInviteRepo;
import com.capecoders.coop.auth.core.sendinvite.UserInvite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AuthModuleTests {
    private final TestJwtValidator testJwtValidator = new TestJwtValidator();
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    public void setUp() {
        userRepo = new InMemoryUserRepo();
    }
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

    @Test
    public void shouldBeAbleToInviteANewUser() {
        TestSendUserInviteEmail testSendUserInviteEmail = new TestSendUserInviteEmail(true);
        TestUserInviteRepo testUserInviteRepo = new TestUserInviteRepo();
        UserInvite userInvite = testUserInviteRepo.getByEmail("test@wow.com");
        assertNull(userInvite);
        Boolean didItSend = new InviteUserService(testSendUserInviteEmail, testUserInviteRepo).invite("test@wow.com");
        assertTrue(didItSend);
        UserInvite userInviteAfterInvite = testUserInviteRepo.getByEmail("test@wow.com");
        assertNotNull(userInviteAfterInvite);
        assertNotNull(userInviteAfterInvite.getId());
        assertNotNull(userInviteAfterInvite.getCode());
        assertEquals("test@wow.com", userInviteAfterInvite.getEmail());
    }

    @Test
    public void shouldReturnFalseIfCouldNotSendEmail() {
        TestSendUserInviteEmail testSendUserInviteEmail = new TestSendUserInviteEmail(false);
        TestUserInviteRepo testUserInviteRepo = new TestUserInviteRepo();
        Boolean didItSend = new InviteUserService(testSendUserInviteEmail, testUserInviteRepo).invite("test@wow.com");
        assertFalse(didItSend);
        assertNull(testUserInviteRepo.getByEmail("test@wow.com"));
    }

    @Test
    public void shouldOnlySaveInviteOncePerEmail() {
        TestSendUserInviteEmail testSendUserInviteEmail = new TestSendUserInviteEmail(true);
        TestUserInviteRepo testUserInviteRepo = new TestUserInviteRepo();
        new InviteUserService(testSendUserInviteEmail, testUserInviteRepo).invite("test@wow.com");
        new InviteUserService(testSendUserInviteEmail, testUserInviteRepo).invite("test@wow.com");
        testUserInviteRepo.emailExistsOnlyOnce("test@wow.com");
    }
}
