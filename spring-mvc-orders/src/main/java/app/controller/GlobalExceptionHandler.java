package app.controller;

import app.exception.OrderValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        var errors = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest()
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(Map.of("error", "Validation failed", "details", errors));
    }

    @ExceptionHandler(OrderValidationException.class)
    public ResponseEntity<Map<String, String>> handleDomainValidation(
            OrderValidationException ex) {
        return ResponseEntity.badRequest()
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        // In production: log the exception here
        return ResponseEntity.internalServerError()
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(Map.of("error", "Internal server error"));
    }
}