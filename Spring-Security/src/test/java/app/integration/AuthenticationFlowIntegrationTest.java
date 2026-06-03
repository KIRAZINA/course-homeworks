package app.integration;

import app.entity.Role;
import app.entity.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationFlowIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("Login redirects to /users after successful authentication")
    void login_RedirectsToUsersPage() throws Exception {
        // Setup
        Role userRole = roleRepository.save(new Role("ROLE_USER"));
        User user = new User();
        user.setName("Auth Flow");
        user.setEmail("auth@test.com");
        user.setPhone("+1234567890");
        user.setPassword(passwordEncoder.encode("secureFlow"));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        // Act & Assert: Login and verify redirect
        mockMvc.perform(formLogin("/login")
                        .user("auth@test.com")
                        .password("secureFlow"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(authenticated().withUsername("auth@test.com")
                        .withAuthorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))));
    }

    @Test
    @WithMockUser(username = "auth@test.com", roles = {"USER"})
    @DisplayName("Authenticated user can access /users page")
    void authenticatedUser_AccessesUsersPage() throws Exception {
        // Setup: Pre-populate users
        User user1 = new User();
        user1.setName("Test User");
        user1.setEmail("test@test.com");
        user1.setPhone("+1111111111");
        user1.setPassword("hashed");
        userRepository.save(user1);

        // Act & Assert: Access protected page with mocked authentication
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users",
                        org.hamcrest.Matchers.hasItem(
                                org.hamcrest.Matchers.hasProperty("email", org.hamcrest.Matchers.is("test@test.com"))
                        )));
    }

    @Test
    @DisplayName("Logout clears session and redirects to login")
    void logout_ClearsSession() throws Exception {
        // Setup
        Role userRole = roleRepository.save(new Role("ROLE_USER"));
        User user = new User();
        user.setName("Logout Test");
        user.setEmail("logout@test.com");
        user.setPhone("+1234567890");
        user.setPassword(passwordEncoder.encode("pass123"));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        // Login first
        mockMvc.perform(formLogin("/login")
                .user("logout@test.com")
                .password("pass123"));

        // Act & Assert: Logout and verify
        mockMvc.perform(logout("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"))
                .andExpect(unauthenticated());

        // Verify protected page redirects after logout
        mockMvc.perform(get("/users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("Unauthenticated access to /users redirects to login")
    void unauthenticatedAccess_RedirectsToLogin() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}