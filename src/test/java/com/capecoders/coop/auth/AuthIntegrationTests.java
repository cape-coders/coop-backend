package com.capecoders.coop.auth;

import com.capecoders.coop.auth.core.LoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "coop-admin.email=wow@wow.com",
    "coop-admin.password=12345678!!!"
})
@AutoConfigureMockMvc
public class AuthIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LoginService loginService;

    private final TestJwtValidator jwtValidator = new TestJwtValidator();

    @Test
    public void shouldBeAbleToLoginWithDefaultUserCreds() throws Exception {
        // Construct a JSON payload for login
        String loginPayload = "{ \"email\": \"wow@wow.com\", \"password\": \"12345678!!!\" }";

        // Perform the POST request to the login endpoint
        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
            .andExpect(status().isOk())
            .andReturn();

        // Extract the JWT token from the response body
        String jwtToken = result.getResponse().getContentAsString();

        // Use the TestJwtValidator to check if the JWT is parsable
        Jwt<Header, Claims> parsableJwt = jwtValidator.isParsableJwt(jwtToken);
        assertNotNull(parsableJwt, "The JWT token should be parsable");
    }
}
