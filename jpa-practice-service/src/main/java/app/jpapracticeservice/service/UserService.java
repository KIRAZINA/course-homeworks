package app.jpapracticeservice.service;

import app.jpapracticeservice.dto.UserCreateRequest;
import app.jpapracticeservice.dto.UserDto;
import app.jpapracticeservice.dto.UserWithPostsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDto createUser(UserCreateRequest request);

    List<UserDto> findUsersByName(String name);

    Page<UserDto> findUsersByNamePaginated(String name, Pageable pageable);

    List<UserDto> findUsersByEmailDomain(String domain);

    Page<UserDto> findUsersByEmailDomainPaginated(String domain, Pageable pageable);

    UserWithPostsDto findUserWithPostsById(Long userId);

    void demonstrateTransactionalRollback(RollbackTestData data);

    record RollbackTestData(String userName, String userEmail, String postTitle, String postContent) {}
}