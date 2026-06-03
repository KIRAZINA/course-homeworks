package app.controller;

import app.config.TestSecurityConfig;
import app.dto.UserRegistrationDto;
import app.entity.User;
import app.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    @DisplayName("GET /login should return login view")
    void showLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("GET /register should return registration form with empty DTO")
    void showRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("POST /register with valid data should redirect to login")
    void processRegistration_Success() throws Exception {
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(new User());

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("name", "John Doe")
                        .param("email", "john@test.com")
                        .param("phone", "+1111111111")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registrationSuccess"));
        verify(userService).registerUser(any(UserRegistrationDto.class));
    }

    @Test
    @DisplayName("POST /register with invalid data should return register view with errors")
    void processRegistration_ValidationErrors() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("name", "")
                        .param("email", "invalid-email")
                        .param("phone", "short")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("user", "name", "email", "phone", "password"));
        verify(userService, never()).registerUser(any());
    }

    @Test
    @DisplayName("POST /register with duplicate email should show error message")
    void processRegistration_DuplicateEmail() throws Exception {
        when(userService.registerUser(any(UserRegistrationDto.class)))
                .thenThrow(new IllegalArgumentException("Email is already registered."));

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("name", "Jane Doe")
                        .param("email", "exists@test.com")
                        .param("phone", "+2222222222")
                        .param("password", "securePass"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "Email is already registered."));
    }
}