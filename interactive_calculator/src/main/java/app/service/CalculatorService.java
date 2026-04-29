package app.service;

import app.dto.CalculationRequest;
import app.dto.CalculationResponse;
import app.dto.HistoryItemResponse;
import app.exception.CalculatorException;
import app.model.CalculationHistoryItem;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service for calculator operations including expression evaluation and history tracking.
 * 
 * Thread Safety:
 * - All history operations are synchronized via explicit synchronized block
 * - History is backed by a LinkedList for efficient add/remove at ends
 * - Atomic operations ensure consistency between size checks and removals
 */
@Service
public class CalculatorService {
    
    /** Maximum number of history items to retain */
    private static final int HISTORY_LIMIT = 10;
    
    /** Lock object for synchronizing history operations */
    private final Object historyLock = new Object();
    
    private final ExpressionEvaluator expressionEvaluator;
    private final LinkedList<CalculationHistoryItem> history = new LinkedList<>();

    public CalculatorService(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
    }

    /**
     * Calculate the given expression and add result to history.
     * 
     * @param request Contains the expression to calculate
     * @return Response with original expression and calculated result
     * @throws CalculatorException If expression is null, empty, or mathematically invalid
     */
    public CalculationResponse calculate(CalculationRequest request) {
        // Validation happens via @Valid annotation in controller
        // But we add defensive check here as well
        if (request == null || request.getExpression() == null || request.getExpression().isBlank()) {
            throw new CalculatorException(
                "Expression must not be empty", 
                HttpStatus.BAD_REQUEST
            );
        }

        String expression = request.getExpression().trim();
        
        // Evaluate the expression (may throw CalculatorException)
        BigDecimal value = expressionEvaluator.evaluate(expression);
        
        // Format result for display
        String result = formatResult(value);
        
        // Add to history (thread-safe)
        addToHistory(expression, result);
        
        return new CalculationResponse(expression, result);
    }

    /**
     * Retrieve all calculation history in reverse chronological order (newest first).
     * 
     * @return List of HistoryItemResponse objects (immutable copy)
     */
    public List<HistoryItemResponse> getHistory() {
        synchronized (historyLock) {
            return history.stream()
                    .map(item -> new HistoryItemResponse(
                        item.getExpression(), 
                        item.getResult(), 
                        item.getTimestamp()
                    ))
                    .toList();
        }
    }

    /**
     * Add calculation to history, maintaining HISTORY_LIMIT max size.
     * 
     * New items are added to the front (most recent first).
     * Oldest items are removed when limit is exceeded.
     * 
     * Thread-safe: uses explicit synchronization block.
     */
    private void addToHistory(String expression, String result) {
        synchronized (historyLock) {
            history.addFirst(new CalculationHistoryItem(
                expression, 
                result, 
                Instant.now()
            ));
            
            // Maintain size limit: remove oldest items if needed
            while (history.size() > HISTORY_LIMIT) {
                history.removeLast();
            }
        }
    }

    /**
     * Format BigDecimal result for display.
     * 
     * - Removes trailing zeros: 3.50 → 3.5
     * - Uses plain string (no scientific notation): 1000000 (not 1E+6)
     * 
     * @param value The BigDecimal to format
     * @return Formatted string representation
     */
    private String formatResult(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }
}

