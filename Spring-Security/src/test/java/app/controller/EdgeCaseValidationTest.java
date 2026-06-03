package app.controller;

import app.config.TestSecurityConfig;
import app.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class EdgeCaseValidationTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private UserService userService;

    @Test
    @DisplayName("Edge: Empty registration fields trigger validation errors")
    void emptyFieldsValidation() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("name", "").param("email", "").param("phone", "").param("password", ""))
                .andExpect(model().attributeHasFieldErrors("user", "name", "email", "phone", "password"));
    }

    @Test
    @DisplayName("Edge: Invalid email format rejected")
    void invalidEmailValidation() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("name", "Test").param("email", "not-an-email").param("phone", "123").param("password", "pass"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }

    @Test
    @DisplayName("Edge: Invalid phone format rejected")
    void invalidPhoneValidation() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("name", "Test").param("email", "t@t.com").param("phone", "abc!@#").param("password", "pass"))
                .andExpect(model().attributeHasFieldErrors("user", "phone"));
    }

    @Test
    @DisplayName("Edge: Password shorter than 6 chars rejected")
    void shortPasswordValidation() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("name", "Test").param("email", "t@t.com").param("phone", "1234567").param("password", "123"))
                .andExpect(model().attributeHasFieldErrors("user", "password"));
    }

    @Test
    @DisplayName("Edge: Duplicate email shows business error after valid submission")
    void duplicateRegistrationPrevention() throws Exception {
        mockMvc.perform(post("/register")
                        .param("name", "Dup")
                        .param("email", "dup@test.com")
                        .param("phone", "+1111111111")
                        .param("password", "secure123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registrationSuccess"));

        when(userService.registerUser(any())).thenThrow(new IllegalArgumentException("Email is already registered."));
        mockMvc.perform(post("/register")
                        .param("name", "Dup")
                        .param("email", "dup@test.com")
                        .param("phone", "+1111111111")
                        .param("password", "secure123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "Email is already registered."));
    }
}