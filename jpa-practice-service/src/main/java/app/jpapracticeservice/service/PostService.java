package app.jpapracticeservice.service;

import app.jpapracticeservice.dto.PostCreateRequest;
import app.jpapracticeservice.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostDto createPost(PostCreateRequest request);

    Page<PostDto> findPostsByUserId(Long userId, Pageable pageable);
}