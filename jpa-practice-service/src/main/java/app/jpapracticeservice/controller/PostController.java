package app.jpapracticeservice.controller;

import app.jpapracticeservice.dto.PostCreateRequest;
import app.jpapracticeservice.dto.PostDto;
import app.jpapracticeservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid PostCreateRequest request) {
        PostDto created = postService.createPost(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostDto>> findPostsByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        Page<PostDto> posts = postService.findPostsByUserId(userId, pageable);
        return ResponseEntity.ok(posts);
    }
}