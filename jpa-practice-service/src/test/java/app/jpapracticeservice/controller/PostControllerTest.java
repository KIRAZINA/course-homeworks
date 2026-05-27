package app.jpapracticeservice.controller;

import app.jpapracticeservice.dto.PostDto;
import app.jpapracticeservice.service.PostService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private PostService postService;

    @Test
    @DisplayName("Should create post and return 201")
    void shouldCreatePostAndReturn201() throws Exception {
        PostDto response = new PostDto(5L, "Title", "Content", 1L);
        when(postService.createPost(any())).thenReturn(response);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new app.jpapracticeservice.dto.PostCreateRequest("Title", "Content", 1L))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    @DisplayName("Should paginate posts by user ID")
    void shouldPaginatePostsByUserId() throws Exception {
        List<PostDto> emptyList = new ArrayList<>();
        Page<PostDto> emptyPage = new PageImpl<>(emptyList, PageRequest.of(0, 5), 0);
        when(postService.findPostsByUserId(any(), any())).thenReturn(emptyPage);

        mockMvc.perform(get("/api/posts/user/1")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.size").value(5));
    }
}