package app.jpapracticeservice.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException with 404")
    void shouldHandleResourceNotFoundWith404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("User", "id", 99L);
        ResponseEntity<Map<String, Object>> response = handler.handleResourceNotFound(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).containsKey("timestamp");
        assertThat(response.getBody().get("error")).isEqualTo("Not Found");
    }

    @Test
    @DisplayName("Should handle DuplicateResourceException with 409")
    void shouldHandleDuplicateResourceWith409() {
        DuplicateResourceException ex = new DuplicateResourceException("User", "email", "test@test.com");
        ResponseEntity<Map<String, Object>> response = handler.handleDuplicateResource(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(409);
        assertThat(response.getBody().get("error")).isEqualTo("Conflict");
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with violations map")
    void shouldHandleValidationErrorsWithViolations() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "email", "Email must be valid"));
        Method mockMethod = Object.class.getMethods()[0]; // Dummy method reference
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidationErrors(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        Map<String, String> violations = (Map<String, String>) response.getBody().get("violations");
        assertThat(violations).containsEntry("email", "Email must be valid");
    }

    @Test
    @DisplayName("Should handle ConstraintViolationException with violations map")
    void shouldHandleConstraintViolationException() {
        Set<jakarta.validation.ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolationException ex = new ConstraintViolationException(violations);
        ResponseEntity<Map<String, Object>> response = handler.handleConstraintViolation(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody().get("error")).isEqualTo("Validation Failed");
    }

    @Test
    @DisplayName("Should handle DataIntegrityViolationException with 409")
    void shouldHandleDataIntegrityViolation() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Unique constraint failed");
        ResponseEntity<Map<String, Object>> response = handler.handleDataIntegrityViolation(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(409);
        assertThat(response.getBody().get("error")).isEqualTo("Conflict");
    }
}