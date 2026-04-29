package app.service;

import app.exception.CalculatorException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Evaluates mathematical expressions with support for:
 * - Basic operators: +, -, *, /
 * - Parentheses for grouping
 * - Unary operators: +5, -3
 * - Decimal numbers
 * 
 * Uses a recursive descent parser for correct operator precedence:
 * 1. Parentheses and unary operators (highest)
 * 2. Multiplication and Division
 * 3. Addition and Subtraction (lowest)
 * 
 * Division precision: 16 significant digits with HALF_UP rounding.
 */
@Component
public class ExpressionEvaluator {
    
    /** Precision for division operations: 16 significant digits */
    private static final MathContext DIVISION_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);
    
    /** Maximum allowed expression length to prevent DoS */
    private static final int MAX_EXPRESSION_LENGTH = 500;

    /**
     * Evaluates a mathematical expression and returns the result.
     * 
     * @param expression The expression to evaluate (e.g., "2 + 3 * (4 - 1)")
     * @return The evaluated result as BigDecimal
     * @throws CalculatorException If expression is invalid or contains division by zero
     */
    public BigDecimal evaluate(String expression) {
        if (expression == null || expression.isBlank()) {
            throw invalidExpression("Expression must not be empty or blank");
        }
        
        if (expression.length() > MAX_EXPRESSION_LENGTH) {
            throw invalidExpression("Expression exceeds maximum length of " + MAX_EXPRESSION_LENGTH + " characters");
        }
        
        Parser parser = new Parser(expression);
        BigDecimal result = parser.parseExpression();
        parser.skipWhitespace();

        if (!parser.isAtEnd()) {
            throw invalidExpression("Unexpected character '" + parser.currentChar() + "' at position " + parser.position);
        }

        return result;
    }

    private static CalculatorException invalidExpression(String message) {
        return new CalculatorException(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Recursive descent parser for mathematical expressions.
     * 
     * Grammar:
     *   Expression := Term (('+' | '-') Term)*
     *   Term       := Factor (('*' | '/') Factor)*
     *   Factor     := ('+' | '-')? (Number | '(' Expression ')')
     *   Number     := Digit+ ('.' Digit+)?
     */
    private static class Parser {
        private final String expression;
        private int position;

        Parser(String expression) {
            this.expression = expression;
        }

        /**
         * Parse expression (lowest precedence: addition and subtraction)
         */
        BigDecimal parseExpression() {
            BigDecimal value = parseTerm();

            while (true) {
                skipWhitespace();
                if (match('+')) {
                    value = value.add(parseTerm());
                } else if (match('-')) {
                    value = value.subtract(parseTerm());
                } else {
                    return value;
                }
            }
        }

        /**
         * Parse term (medium precedence: multiplication and division)
         */
        BigDecimal parseTerm() {
            BigDecimal value = parseFactor();

            while (true) {
                skipWhitespace();
                if (match('*')) {
                    value = value.multiply(parseFactor());
                } else if (match('/')) {
                    BigDecimal divisor = parseFactor();
                    if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                        throw new CalculatorException(
                            "Division by zero is not allowed", 
                            HttpStatus.UNPROCESSABLE_ENTITY
                        );
                    }
                    value = value.divide(divisor, DIVISION_CONTEXT);
                } else {
                    return value;
                }
            }
        }

        /**
         * Parse factor (highest precedence: unary operators, numbers, parenthesized expressions)
         */
        BigDecimal parseFactor() {
            skipWhitespace();

            // Handle unary plus: +5
            if (match('+')) {
                return parseFactor();
            }

            // Handle unary minus: -5
            if (match('-')) {
                return parseFactor().negate();
            }

            // Handle parenthesized expressions: (2 + 3)
            if (match('(')) {
                BigDecimal value = parseExpression();
                skipWhitespace();
                if (!match(')')) {
                    throw invalidExpression("Missing closing parenthesis ')'");
                }
                return value;
            }

            return parseNumber();
        }

        /**
         * Parse a number (integer or decimal).
         * Examples: 42, 3.14, 0.5, .5 (invalid - requires leading zero)
         */
        BigDecimal parseNumber() {
            skipWhitespace();
            int start = position;
            boolean hasDigit = false;
            boolean hasDecimalPoint = false;

            while (!isAtEnd()) {
                char character = currentChar();
                
                if (Character.isDigit(character)) {
                    hasDigit = true;
                    position++;
                } else if (character == '.') {
                    if (hasDecimalPoint) {
                        throw invalidExpression("Invalid number format: multiple decimal points");
                    }
                    hasDecimalPoint = true;
                    position++;
                } else {
                    break;
                }
            }

            if (!hasDigit) {
                throw invalidExpression("Expected a number, but found '" + 
                    (isAtEnd() ? "end of expression" : "'" + currentChar() + "'"));
            }

            // Validate trailing decimal point: "3." is invalid
            String numberStr = expression.substring(start, position);
            if (numberStr.endsWith(".")) {
                throw invalidExpression("Invalid number format: trailing decimal point");
            }

            try {
                return new BigDecimal(numberStr);
            } catch (NumberFormatException exception) {
                throw invalidExpression("Invalid number format: '" + numberStr + "'");
            }
        }

        void skipWhitespace() {
            while (!isAtEnd() && Character.isWhitespace(currentChar())) {
                position++;
            }
        }

        boolean match(char expected) {
            if (!isAtEnd() && currentChar() == expected) {
                position++;
                return true;
            }
            return false;
        }

        boolean isAtEnd() {
            return position >= expression.length();
        }

        char currentChar() {
            return expression.charAt(position);
        }
    }
}

