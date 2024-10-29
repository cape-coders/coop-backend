package com.capecoders.coop.auth;

import com.capecoders.coop.auth.adaptors.InMemoryUserRepo;
import com.capecoders.coop.auth.core.CoopUser;
import com.capecoders.coop.auth.core.DefaultAdminService;
import com.capecoders.coop.auth.core.LoginService;
import com.capecoders.coop.auth.core.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
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

    private Jwt<Header, Claims> isParsableJwt(String token) {
        return testJwtValidator.isParsableJwt(token);
    }
}
