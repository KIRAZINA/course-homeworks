package app.jpapracticeservice.controller;

import app.jpapracticeservice.dto.UserDto;
import app.jpapracticeservice.exception.DuplicateResourceException;
import app.jpapracticeservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private UserService userService;

    @Test
    @DisplayName("Should create user and return 201")
    void shouldCreateUserAndReturn201() throws Exception {
        UserDto mockResponse = new UserDto(1L, "Test", "test@example.com");
        when(userService.createUser(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new app.jpapracticeservice.dto.UserCreateRequest("Test", "test@example.com"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    @DisplayName("Should return 400 for invalid payload")
    void shouldReturn400ForInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"email\": \"invalid\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.violations.name").exists())
                .andExpect(jsonPath("$.violations.email").exists());
    }

    @Test
    @DisplayName("Should return 409 on duplicate email")
    void shouldReturn409OnDuplicateEmail() throws Exception {
        when(userService.createUser(any())).thenThrow(new DuplicateResourceException("User", "email", "dup@example.com"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new app.jpapracticeservice.dto.UserCreateRequest("Dup", "dup@example.com"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    @Test
    @DisplayName("Should handle pagination parameters")
    void shouldHandlePaginationParameters() throws Exception {
        List<UserDto> emptyList = new ArrayList<>();
        Page<UserDto> emptyPage = new PageImpl<>(emptyList, PageRequest.of(0, 10), 0);
        when(userService.findUsersByNamePaginated(any(), any())).thenReturn(emptyPage);

        mockMvc.perform(get("/api/users/search/name/paginated")
                        .param("name", "test")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}