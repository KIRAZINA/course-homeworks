package app.controller;

import app.dto.CalculationRequest;
import app.dto.CalculationResponse;
import app.dto.HistoryItemResponse;
import app.service.CalculatorService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for calculator operations.
 * 
 * Base path: /api
 * Content-Type: application/json
 */
@RestController
@RequestMapping("/api")
public class CalculatorController {
    
    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    /**
     * Calculate a mathematical expression.
     * 
     * Endpoint: POST /api/calculate
     * 
     * Request Body:
     * {
     *   "expression": "2 + 3 * 4"
     * }
     * 
     * Success Response (200 OK):
     * {
     *   "expression": "2 + 3 * 4",
     *   "result": "14"
     * }
     * 
     * Error Responses:
     * - 400 Bad Request: Invalid JSON, empty expression, or syntax error
     *   Example: "Division by zero is not allowed"
     * - 422 Unprocessable Entity: Division by zero
     * - 500 Internal Server Error: Unexpected server error
     * 
     * @param request Contains the expression to calculate
     * @return CalculationResponse with expression and calculated result
     */
    @PostMapping(
        value = "/calculate",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public CalculationResponse calculate(@Valid @RequestBody CalculationRequest request) {
        return calculatorService.calculate(request);
    }

    /**
     * Retrieve calculation history.
     * 
     * Endpoint: GET /api/history
     * 
     * Success Response (200 OK):
     * [
     *   {
     *     "expression": "2 + 3 * 4",
     *     "result": "14",
     *     "timestamp": "2024-04-29T10:30:00Z"
     *   },
     *   {
     *     "expression": "100 / 5",
     *     "result": "20",
     *     "timestamp": "2024-04-29T10:25:00Z"
     *   }
     * ]
     * 
     * Note: Results are ordered newest first (reverse chronological).
     *       Maximum 10 most recent calculations are retained.
     * 
     * @return List of HistoryItemResponse objects (newest first)
     */
    @GetMapping(
        value = "/history",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<HistoryItemResponse> getHistory() {
        return calculatorService.getHistory();
    }
}

