package app.jpapracticeservice.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> error = buildErrorResponse(404, "Not Found", ex.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateResource(DuplicateResourceException ex) {
        Map<String, Object> error = buildErrorResponse(409, "Conflict", ex.getMessage());
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> error = buildErrorResponse(400, "Validation Failed", "Request payload contains invalid fields");

        Map<String, String> violations = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                violations.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        error.put("violations", violations);

        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> error = buildErrorResponse(400, "Validation Failed", "Parameter validation failed");

        Map<String, String> violations = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                violations.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        error.put("violations", violations);

        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Database constraint violation";
        if (ex.getRootCause() != null && ex.getRootCause().getMessage().contains("unique")) {
            message = "Resource with provided data already exists";
        }
        Map<String, Object> error = buildErrorResponse(409, "Conflict", message);
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        Map<String, Object> error = buildErrorResponse(400, "Bad Request", ex.getMessage());
        return ResponseEntity.status(400).body(error);
    }

    private Map<String, Object> buildErrorResponse(int status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status);
        response.put("error", error);
        response.put("message", message);
        return response;
    }
}