package app.service;

import app.dto.CalculationRequest;
import app.dto.CalculationResponse;
import app.dto.HistoryItemResponse;
import app.exception.CalculatorException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CalculatorService.
 * 
 * Tests cover:
 * - Valid calculation flows
 * - History management
 * - Validation and error handling
 * - History size limits
 * - Thread safety basics
 */
@DisplayName("CalculatorService Unit Tests")
@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

    @Mock
    private ExpressionEvaluator expressionEvaluator;

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService(expressionEvaluator);
    }

    // ============ VALID CALCULATIONS ============

    @Test
    @DisplayName("Should calculate valid expression and return CalculationResponse")
    void testCalculateValidExpression() {
        // Arrange
        CalculationRequest request = new CalculationRequest("2 + 3");
        when(expressionEvaluator.evaluate("2 + 3")).thenReturn(new BigDecimal("5"));

        // Act
        CalculationResponse response = calculatorService.calculate(request);

        // Assert
        assertThat(response)
            .isNotNull()
            .extracting(CalculationResponse::getExpression, CalculationResponse::getResult)
            .containsExactly("2 + 3", "5");
        verify(expressionEvaluator).evaluate("2 + 3");
    }

    @Test
    @DisplayName("Should format result by removing trailing zeros")
    void testResultFormattingRemovesTrailingZeros() {
        // Arrange
        CalculationRequest request = new CalculationRequest("10 / 4");
        when(expressionEvaluator.evaluate("10 / 4")).thenReturn(new BigDecimal("2.50"));

        // Act
        CalculationResponse response = calculatorService.calculate(request);

        // Assert: 2.50 should become 2.5
        assertThat(response.getResult()).isEqualTo("2.5");
    }

    @Test
    @DisplayName("Should not use scientific notation in result")
    void testResultNoScientificNotation() {
        // Arrange
        CalculationRequest request = new CalculationRequest("1000000");
        when(expressionEvaluator.evaluate("1000000")).thenReturn(new BigDecimal("1000000"));

        // Act
        CalculationResponse response = calculatorService.calculate(request);

        // Assert: Should be "1000000" not "1E+6"
        assertThat(response.getResult())
            .isEqualTo("1000000")
            .doesNotContain("E");
    }

    // ============ VALIDATION ============

    @Test
    @DisplayName("Should throw CalculatorException for null request")
    void testNullRequest() {
        CalculatorException exception = catchThrowableOfType(
            () -> calculatorService.calculate(null),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessage("Expression must not be empty")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for null expression")
    void testNullExpression() {
        CalculationRequest request = new CalculationRequest(null);
        CalculatorException exception = catchThrowableOfType(
            () -> calculatorService.calculate(request),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessage("Expression must not be empty")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for empty expression")
    void testEmptyExpression() {
        CalculationRequest request = new CalculationRequest("");
        CalculatorException exception = catchThrowableOfType(
            () -> calculatorService.calculate(request),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessage("Expression must not be empty")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for blank expression")
    void testBlankExpression() {
        CalculationRequest request = new CalculationRequest("   ");
        CalculatorException exception = catchThrowableOfType(
            () -> calculatorService.calculate(request),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessage("Expression must not be empty")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ============ HISTORY MANAGEMENT ============

    @Test
    @DisplayName("Should add calculation to history")
    void testAddToHistory() {
        // Arrange
        CalculationRequest request = new CalculationRequest("2 + 3");
        when(expressionEvaluator.evaluate("2 + 3")).thenReturn(new BigDecimal("5"));

        // Act
        calculatorService.calculate(request);
        List<HistoryItemResponse> history = calculatorService.getHistory();

        // Assert
        assertThat(history)
            .hasSize(1)
            .first()
            .extracting(HistoryItemResponse::getExpression, HistoryItemResponse::getResult)
            .containsExactly("2 + 3", "5");
    }

    @Test
    @DisplayName("Should return empty history initially")
    void testInitiallyEmptyHistory() {
        List<HistoryItemResponse> history = calculatorService.getHistory();
        assertThat(history).isEmpty();
    }

    @Test
    @DisplayName("Should maintain history in reverse chronological order (newest first)")
    void testHistoryOrderNewestFirst() {
        // Arrange & Act
        when(expressionEvaluator.evaluate("1 + 1")).thenReturn(new BigDecimal("2"));
        calculatorService.calculate(new CalculationRequest("1 + 1"));

        when(expressionEvaluator.evaluate("2 + 2")).thenReturn(new BigDecimal("4"));
        calculatorService.calculate(new CalculationRequest("2 + 2"));

        when(expressionEvaluator.evaluate("3 + 3")).thenReturn(new BigDecimal("6"));
        calculatorService.calculate(new CalculationRequest("3 + 3"));

        // Act
        List<HistoryItemResponse> history = calculatorService.getHistory();

        // Assert: Should be in reverse order (newest first)
        assertThat(history)
            .hasSize(3)
            .extracting(HistoryItemResponse::getExpression)
            .containsExactly("3 + 3", "2 + 2", "1 + 1");
    }

    @Test
    @DisplayName("Should respect history limit of 10 items")
    void testHistoryLimitOfTen() {
        // Arrange & Act: Add 15 calculations
        for (int i = 1; i <= 15; i++) {
            String expr = i + " + 0";
            when(expressionEvaluator.evaluate(expr)).thenReturn(new BigDecimal(String.valueOf(i)));
            calculatorService.calculate(new CalculationRequest(expr));
        }

        // Act
        List<HistoryItemResponse> history = calculatorService.getHistory();

        // Assert: Should only keep the last 10
        assertThat(history).hasSize(10);
        // Newest (15 + 0) should be first
        assertThat(history.get(0).getExpression()).isEqualTo("15 + 0");
        // Oldest kept (6 + 0) should be last
        assertThat(history.get(9).getExpression()).isEqualTo("6 + 0");
    }

    @Test
    @DisplayName("Should include timestamp in history")
    void testHistoryIncludesTimestamp() {
        // Arrange
        when(expressionEvaluator.evaluate("5 * 2")).thenReturn(new BigDecimal("10"));

        // Act
        calculatorService.calculate(new CalculationRequest("5 * 2"));
        List<HistoryItemResponse> history = calculatorService.getHistory();

        // Assert
        assertThat(history)
            .hasSize(1)
            .first()
            .extracting(HistoryItemResponse::getTimestamp)
            .isNotNull();
    }

    @Test
    @DisplayName("Should trim expression before processing")
    void testExpressionTrimmed() {
        // Arrange
        when(expressionEvaluator.evaluate("2 + 3")).thenReturn(new BigDecimal("5"));

        // Act
        calculatorService.calculate(new CalculationRequest("  2 + 3  "));
        List<HistoryItemResponse> history = calculatorService.getHistory();

        // Assert: Should store trimmed expression
        assertThat(history.get(0).getExpression()).isEqualTo("2 + 3");
    }

    // ============ ERROR PROPAGATION ============

    @Test
    @DisplayName("Should propagate CalculatorException from evaluator")
    void testPropagateCalculatorException() {
        // Arrange
        CalculationRequest request = new CalculationRequest("5 / 0");
        when(expressionEvaluator.evaluate("5 / 0"))
            .thenThrow(new CalculatorException("Division by zero", HttpStatus.UNPROCESSABLE_ENTITY));

        // Act & Assert
        assertThatThrownBy(() -> calculatorService.calculate(request))
            .isInstanceOf(CalculatorException.class)
            .hasMessage("Division by zero");
    }

    // ============ THREAD SAFETY (BASIC) ============

    @Test
    @DisplayName("Should handle concurrent history access")
    void testConcurrentHistoryAccess() throws InterruptedException {
        // Arrange: Create calculations from multiple threads
        when(expressionEvaluator.evaluate("1 + 1")).thenReturn(new BigDecimal("2"));
        when(expressionEvaluator.evaluate("2 + 2")).thenReturn(new BigDecimal("4"));

        // Act: Simulate concurrent access
        Thread thread1 = new Thread(() -> 
            calculatorService.calculate(new CalculationRequest("1 + 1"))
        );
        Thread thread2 = new Thread(() ->
            calculatorService.calculate(new CalculationRequest("2 + 2"))
        );

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // Assert: History should contain both calculations
        List<HistoryItemResponse> history = calculatorService.getHistory();
        assertThat(history)
            .hasSize(2)
            .extracting(HistoryItemResponse::getExpression)
            .containsExactlyInAnyOrder("1 + 1", "2 + 2");
    }

    @Test
    @DisplayName("Should maintain history size limit during concurrent access")
    void testHistorySizeLimitWithConcurrency() throws InterruptedException {
        // Arrange: Create many calculations from multiple threads
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            final int threadNum = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 4; j++) {
                    String expr = (threadNum * 10 + j) + " + 0";
                    when(expressionEvaluator.evaluate(expr))
                        .thenReturn(new BigDecimal(String.valueOf(threadNum * 10 + j)));
                    calculatorService.calculate(new CalculationRequest(expr));
                }
            });
        }

        // Act
        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();

        // Assert: History should not exceed limit (20 calculations, but only 10 kept)
        List<HistoryItemResponse> history = calculatorService.getHistory();
        assertThat(history).hasSize(10);
    }
}
