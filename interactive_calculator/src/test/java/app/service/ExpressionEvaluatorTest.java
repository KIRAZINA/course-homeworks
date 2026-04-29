package app.service;

import app.exception.CalculatorException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive unit tests for ExpressionEvaluator.
 * 
 * Tests cover:
 * - Basic arithmetic operations
 * - Operator precedence
 * - Parentheses
 * - Unary operators
 * - Edge cases and error conditions
 * - Division by zero
 * - Invalid expressions
 */
@DisplayName("ExpressionEvaluator Unit Tests")
class ExpressionEvaluatorTest {

    private ExpressionEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new ExpressionEvaluator();
    }

    // ============ BASIC OPERATIONS ============

    @Test
    @DisplayName("Should evaluate simple addition: 2 + 3 = 5")
    void testSimpleAddition() {
        BigDecimal result = evaluator.evaluate("2 + 3");
        assertThat(result).isEqualTo(new BigDecimal("5"));
    }

    @Test
    @DisplayName("Should evaluate simple subtraction: 10 - 4 = 6")
    void testSimpleSubtraction() {
        BigDecimal result = evaluator.evaluate("10 - 4");
        assertThat(result).isEqualTo(new BigDecimal("6"));
    }

    @Test
    @DisplayName("Should evaluate simple multiplication: 3 * 4 = 12")
    void testSimpleMultiplication() {
        BigDecimal result = evaluator.evaluate("3 * 4");
        assertThat(result).isEqualTo(new BigDecimal("12"));
    }

    @Test
    @DisplayName("Should evaluate simple division: 20 / 4 = 5")
    void testSimpleDivision() {
        BigDecimal result = evaluator.evaluate("20 / 4");
        assertThat(result).isEqualTo(new BigDecimal("5"));
    }

    @Test
    @DisplayName("Should evaluate single number: 42")
    void testSingleNumber() {
        BigDecimal result = evaluator.evaluate("42");
        assertThat(result).isEqualTo(new BigDecimal("42"));
    }

    @Test
    @DisplayName("Should evaluate decimal number: 3.14")
    void testDecimalNumber() {
        BigDecimal result = evaluator.evaluate("3.14");
        assertThat(result).isEqualTo(new BigDecimal("3.14"));
    }

    // ============ OPERATOR PRECEDENCE ============

    @Test
    @DisplayName("Should respect operator precedence: 2 + 3 * 4 = 14 (not 20)")
    void testPrecedenceMultiplicationBeforeAddition() {
        BigDecimal result = evaluator.evaluate("2 + 3 * 4");
        assertThat(result).isEqualTo(new BigDecimal("14"));
    }

    @Test
    @DisplayName("Should respect operator precedence: 20 / 4 + 2 = 7 (not 2)")
    void testPrecedenceDivisionBeforeAddition() {
        BigDecimal result = evaluator.evaluate("20 / 4 + 2");
        assertThat(result).isEqualTo(new BigDecimal("7"));
    }

    @Test
    @DisplayName("Should respect operator precedence: 10 - 2 * 3 = 4 (not 24)")
    void testPrecedenceMultiplicationBeforeSubtraction() {
        BigDecimal result = evaluator.evaluate("10 - 2 * 3");
        assertThat(result).isEqualTo(new BigDecimal("4"));
    }

    @Test
    @DisplayName("Should evaluate complex expression respecting precedence: 2 + 3 * 4 - 5 / 2 = 11.5")
    void testComplexPrecedence() {
        BigDecimal result = evaluator.evaluate("2 + 3 * 4 - 5 / 2");
        // 2 + 12 - 2.5 = 11.5
        assertThat(result).isEqualTo(new BigDecimal("11.5"));
    }

    // ============ PARENTHESES ============

    @Test
    @DisplayName("Should evaluate parentheses: (2 + 3) * 4 = 20")
    void testParenthesesOverridePrecedence() {
        BigDecimal result = evaluator.evaluate("(2 + 3) * 4");
        assertThat(result).isEqualTo(new BigDecimal("20"));
    }

    @Test
    @DisplayName("Should evaluate nested parentheses: ((2 + 3) * 4) - 5 = 15")
    void testNestedParentheses() {
        BigDecimal result = evaluator.evaluate("((2 + 3) * 4) - 5");
        assertThat(result).isEqualTo(new BigDecimal("15"));
    }

    @Test
    @DisplayName("Should evaluate multiple parentheses: (2 + 3) * (4 - 1) = 15")
    void testMultipleParentheses() {
        BigDecimal result = evaluator.evaluate("(2 + 3) * (4 - 1)");
        assertThat(result).isEqualTo(new BigDecimal("15"));
    }

    @Test
    @DisplayName("Should evaluate expression with trailing operations after parentheses: (5 + 3) * 2 = 16")
    void testParenthesesWithFollowingOp() {
        BigDecimal result = evaluator.evaluate("(5 + 3) * 2");
        assertThat(result).isEqualTo(new BigDecimal("16"));
    }

    // ============ UNARY OPERATORS ============

    @Test
    @DisplayName("Should evaluate unary minus: -5")
    void testUnaryMinus() {
        BigDecimal result = evaluator.evaluate("-5");
        assertThat(result).isEqualTo(new BigDecimal("-5"));
    }

    @Test
    @DisplayName("Should evaluate unary plus: +5")
    void testUnaryPlus() {
        BigDecimal result = evaluator.evaluate("+5");
        assertThat(result).isEqualTo(new BigDecimal("5"));
    }

    @Test
    @DisplayName("Should evaluate unary minus in expression: 3 + -2 = 1")
    void testUnaryMinusInExpression() {
        BigDecimal result = evaluator.evaluate("3 + -2");
        assertThat(result).isEqualTo(new BigDecimal("1"));
    }

    @Test
    @DisplayName("Should evaluate double unary minus: --5 = 5")
    void testDoubleUnaryMinus() {
        BigDecimal result = evaluator.evaluate("--5");
        assertThat(result).isEqualTo(new BigDecimal("5"));
    }

    @Test
    @DisplayName("Should evaluate unary minus with parentheses: -(5 + 3) = -8")
    void testUnaryMinusWithParentheses() {
        BigDecimal result = evaluator.evaluate("-(5 + 3)");
        assertThat(result).isEqualTo(new BigDecimal("-8"));
    }

    @Test
    @DisplayName("Should evaluate unary minus in multiplication: -2 * 3 = -6")
    void testUnaryMinusInMultiplication() {
        BigDecimal result = evaluator.evaluate("-2 * 3");
        assertThat(result).isEqualTo(new BigDecimal("-6"));
    }

    // ============ WHITESPACE HANDLING ============

    @Test
    @DisplayName("Should handle whitespace in expression: '2  +  3' = 5")
    void testExpressionWithWhitespace() {
        BigDecimal result = evaluator.evaluate("2  +  3");
        assertThat(result).isEqualTo(new BigDecimal("5"));
    }

    @Test
    @DisplayName("Should handle leading whitespace: '   5 + 3' = 8")
    void testLeadingWhitespace() {
        BigDecimal result = evaluator.evaluate("   5 + 3");
        assertThat(result).isEqualTo(new BigDecimal("8"));
    }

    @Test
    @DisplayName("Should handle trailing whitespace: '5 + 3   ' = 8")
    void testTrailingWhitespace() {
        BigDecimal result = evaluator.evaluate("5 + 3   ");
        assertThat(result).isEqualTo(new BigDecimal("8"));
    }

    // ============ DIVISION BY ZERO ============

    @Test
    @DisplayName("Should throw CalculatorException for division by zero: 5 / 0")
    void testDivisionByZero() {
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate("5 / 0"),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessage("Division by zero is not allowed")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    @DisplayName("Should throw CalculatorException for division by zero in complex expression: 10 / (2 - 2)")
    void testDivisionByZeroInComplexExpression() {
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate("10 / (2 - 2)"),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessage("Division by zero is not allowed")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // ============ INVALID EXPRESSIONS ============

    @Test
    @DisplayName("Should throw CalculatorException for null expression")
    void testNullExpression() {
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate(null),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessage("Expression must not be empty or blank")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for empty expression")
    void testEmptyExpression() {
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate(""),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessage("Expression must not be empty or blank")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for blank expression")
    void testBlankExpression() {
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate("   "),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessage("Expression must not be empty or blank")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for missing operand: 5 +")
    void testMissingOperand() {
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate("5 +"),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessageContaining("Expected a number")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for missing closing parenthesis: (5 + 3")
    void testMissingClosingParen() {
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate("(5 + 3"),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessageContaining("Missing closing parenthesis")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for unexpected closing parenthesis: (5 + 3))")
    void testExtraClosingParen() {
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate("(5 + 3))"),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessageContaining("Unexpected character")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for multiple decimal points: 3.14.159")
    void testMultipleDecimalPoints() {
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate("3.14.159"),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessageContaining("multiple decimal points")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for invalid character: 5 & 3")
    void testInvalidCharacter() {
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate("5 & 3"),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessageContaining("Unexpected character")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should throw CalculatorException for expression exceeding max length")
    void testExpressionTooLong() {
        String longExpression = "1 + 2 + ".repeat(100); // Creates very long expression
        CalculatorException exception = catchThrowableOfType(
            () -> evaluator.evaluate(longExpression),
            CalculatorException.class
        );
        assertThat(exception)
            .hasMessageContaining("exceeds maximum length")
            .extracting(e -> e.getStatus())
            .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ============ EDGE CASES ============

    @Test
    @DisplayName("Should evaluate zero: 0")
    void testZero() {
        BigDecimal result = evaluator.evaluate("0");
        assertThat(result).isEqualTo(new BigDecimal("0"));
    }

    @Test
    @DisplayName("Should evaluate negative zero: -0")
    void testNegativeZero() {
        BigDecimal result = evaluator.evaluate("-0");
        assertThat(result).isEqualTo(new BigDecimal("0"));
    }

    @Test
    @DisplayName("Should evaluate very small decimal: 0.000001")
    void testVerySmallDecimal() {
        BigDecimal result = evaluator.evaluate("0.000001");
        assertThat(result).isEqualTo(new BigDecimal("0.000001"));
    }

    @Test
    @DisplayName("Should evaluate very large number: 999999999999")
    void testVeryLargeNumber() {
        BigDecimal result = evaluator.evaluate("999999999999");
        assertThat(result).isEqualTo(new BigDecimal("999999999999"));
    }

    @Test
    @DisplayName("Should evaluate addition of zeros: 0 + 0")
    void testAdditionOfZeros() {
        BigDecimal result = evaluator.evaluate("0 + 0");
        assertThat(result).isEqualTo(new BigDecimal("0"));
    }

    @Test
    @DisplayName("Should evaluate multiplication by zero: 999 * 0")
    void testMultiplicationByZero() {
        BigDecimal result = evaluator.evaluate("999 * 0");
        assertThat(result).isEqualTo(new BigDecimal("0"));
    }

    @Test
    @DisplayName("Should evaluate long chain of operations: 1 + 1 + 1 + 1 + 1 = 5")
    void testLongChain() {
        BigDecimal result = evaluator.evaluate("1 + 1 + 1 + 1 + 1");
        assertThat(result).isEqualTo(new BigDecimal("5"));
    }

    @ParameterizedTest(name = "Should evaluate: {0}")
    @ValueSource(strings = {"1", "10", "100", "0.5", "3.14159"})
    void testValidNumbers(String number) {
        assertThat(evaluator.evaluate(number))
            .isNotNull()
            .isInstanceOf(BigDecimal.class);
    }
}
