package app.exception;

import org.springframework.http.HttpStatus;

public class CalculatorException extends RuntimeException {
    private final HttpStatus status;

    public CalculatorException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
