package app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for calculation request from client.
 * 
 * Constraints:
 * - expression must not be blank (null, empty, or whitespace)
 * - expression max length of 500 chars (prevents DoS)
 */
public class CalculationRequest {
    
    @NotBlank(message = "Expression must not be empty or blank")
    @Size(max = 500, message = "Expression must not exceed 500 characters")
    private String expression;

    public CalculationRequest() {
    }

    public CalculationRequest(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
