package app.jpapracticeservice.repository;

import app.jpapracticeservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameIgnoreCase(String name);

    List<User> findByEmailEndingWithIgnoreCase(String domain);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :domain))")
    List<User> findUsersByEmailDomain(@Param("domain") String domain);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.posts WHERE u.id = :id")
    Optional<User> findByIdWithPosts(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.posts WHERE LOWER(u.name) = LOWER(:name)")
    Page<User> findByNameIgnoreCaseWithPosts(@Param("name") String name, Pageable pageable);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.posts WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :domain))")
    Page<User> findByEmailEndingWithIgnoreCaseWithPosts(@Param("domain") String domain, Pageable pageable);
}