package app.exception;

import app.dto.ErrorResponse;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * Global exception handler for the calculator application.
 * 
 * Handles:
 * - CalculatorException (custom business logic exceptions)
 * - MethodArgumentNotValidException (validation failures)
 * - HttpMessageNotReadableException (invalid JSON)
 * - Generic Exception (unexpected errors)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle CalculatorException (division by zero, invalid expressions, etc.)
     */
    @ExceptionHandler(CalculatorException.class)
    public ResponseEntity<ErrorResponse> handleCalculatorException(CalculatorException exception) {
        return buildResponse(exception.getStatus(), exception.getMessage());
    }

    /**
     * Handle validation failures (@Valid annotation failures)
     * 
     * Returns 400 Bad Request when request body fields fail validation.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Handle malformed JSON in request body
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableJson() {
        return buildResponse(HttpStatus.BAD_REQUEST, "Request body must be valid JSON.");
    }

    /**
     * Handle any other unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }

    /**
     * Build error response with timestamp, status, error type, and message
     */
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return ResponseEntity.status(status).body(response);
    }
}
