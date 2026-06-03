package app.integration;

import app.entity.User;
import app.repository.UserRepository;
import app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Anonymous can access /login and /register but not /users")
    void securityRules_AnonymousAccess() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk());
        mockMvc.perform(get("/register")).andExpect(status().isOk());
        mockMvc.perform(get("/users")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("Successful login redirects to /users")
    void login_Success() throws Exception {
        registerTestUser("valid@test.com", "password123");

        mockMvc.perform(formLogin("/login")
                        .user("valid@test.com")
                        .password("password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
    }

    @Test
    @DisplayName("Failed login returns to /login with error")
    void login_Failed() throws Exception {
        registerTestUser("fail@test.com", "correct");

        mockMvc.perform(formLogin("/login")
                        .user("fail@test.com")
                        .password("wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @DisplayName("Logout clears session and redirects to /login?logout")
    void logout_Success() throws Exception {
        registerTestUser("logout@test.com", "password123");

        mockMvc.perform(formLogin("/login")
                .user("logout@test.com")
                .password("password123"));

        // FIXED: Removed .with(csrf()) - logout() auto-handles CSRF in tests
        mockMvc.perform(logout("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"))
                .andExpect(unauthenticated());
    }

    private void registerTestUser(String email, String rawPass) {
        User u = new User();
        u.setName("Test");
        u.setEmail(email);
        u.setPhone("123456");
        u.setPassword(passwordEncoder.encode(rawPass));
        userRepository.save(u);
    }
}