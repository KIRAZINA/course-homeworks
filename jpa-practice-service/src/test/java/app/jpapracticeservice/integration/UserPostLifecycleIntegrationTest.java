package app.jpapracticeservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import app.jpapracticeservice.dto.PostCreateRequest;
import app.jpapracticeservice.dto.UserCreateRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserPostLifecycleIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    /**
     * Generate guaranteed unique email using full UUID.
     * No static state = no cross-test contamination.
     */
    private String uniqueEmail() {
        return "test_" + UUID.randomUUID() + "_" + System.nanoTime() + "@example.com";
    }

    @Test
    @DisplayName("Full lifecycle: create user -> create post -> fetch with posts")
    void fullLifecycleTest() throws Exception {
        String email = uniqueEmail();
        UserCreateRequest userReq = new UserCreateRequest("IntegrationUser", email);

        String userJson = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email))
                .andReturn().getResponse().getContentAsString();

        Long userId = objectMapper.readTree(userJson).get("id").asLong();

        PostCreateRequest postReq = new PostCreateRequest("IntPost", "IntegrationContent", userId);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("IntPost"));

        mockMvc.perform(get("/api/users/{id}/with-posts", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts.length()").value(1))
                .andExpect(jsonPath("$.posts[0].title").value("IntPost"));
    }

    @Test
    @DisplayName("Should enforce unique email constraint across transactions")
    void shouldEnforceUniqueEmailConstraint() throws Exception {
        String uniqueEmail = uniqueEmail();
        UserCreateRequest req = new UserCreateRequest("UniqueTest", uniqueEmail);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    @Test
    @DisplayName("Pagination should return correct slice metadata")
    void shouldReturnCorrectPaginationMetadata() throws Exception {
        String exactName = "PageUser";

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new UserCreateRequest(exactName, uniqueEmail()))));
        }

        mockMvc.perform(get("/api/users/search/name/paginated")
                        .param("name", exactName)
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.last").value(false))
                .andExpect(jsonPath("$.first").value(true));
    }
}