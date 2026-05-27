package app.jpapracticeservice.controller;

import app.jpapracticeservice.dto.*;
import app.jpapracticeservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCreateRequest request) {
        UserDto created = userService.createUser(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<UserDto>> findUsersByName(@RequestParam String name) {
        List<UserDto> users = userService.findUsersByName(name);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/name/paginated")
    public ResponseEntity<Page<UserDto>> findUsersByNamePaginated(
            @RequestParam String name,
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        Page<UserDto> users = userService.findUsersByNamePaginated(name, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/email-domain")
    public ResponseEntity<List<UserDto>> findUsersByEmailDomain(@RequestParam String domain) {
        List<UserDto> users = userService.findUsersByEmailDomain(domain);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search/email-domain/paginated")
    public ResponseEntity<Page<UserDto>> findUsersByEmailDomainPaginated(
            @RequestParam String domain,
            @PageableDefault(size = 20, page = 0) Pageable pageable) {
        Page<UserDto> users = userService.findUsersByEmailDomainPaginated(domain, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/with-posts")
    public ResponseEntity<UserWithPostsDto> findUserWithPosts(@PathVariable Long id) {
        UserWithPostsDto userWithPosts = userService.findUserWithPostsById(id);
        return ResponseEntity.ok(userWithPosts);
    }

    @PostMapping("/rollback-test")
    public ResponseEntity<String> demonstrateRollback(@RequestBody @Valid RollbackTestRequest request) {
        UserService.RollbackTestData data = new UserService.RollbackTestData(
                request.userName(),
                request.userEmail(),
                request.postTitle(),
                request.postContent()
        );
        userService.demonstrateTransactionalRollback(data);
        return ResponseEntity.ok("Rollback test completed");
    }
}