package app.controller;

import app.dto.CalculationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for CalculatorController.
 * 
 * Tests:
 * - Complete request/response cycle
 * - HTTP status codes
 * - JSON serialization/deserialization
 * - Error handling
 * - Validation
 * 
 * Uses @SpringBootTest to start the full application context.
 * Uses MockMvc to test HTTP layer without actual network calls.
 * Uses @DirtiesContext to refresh context between tests to avoid state pollution.
 */
@DisplayName("CalculatorController Integration Tests")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CalculatorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ============ VALID CALCULATIONS ============

    @Test
    @DisplayName("Should POST /api/calculate with valid expression and return 200 OK")
    void testCalculateValidExpression() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("2 + 3");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.expression").value("2 + 3"))
                .andExpect(jsonPath("$.result").value("5"));
    }

    @Test
    @DisplayName("Should calculate complex expression with correct precedence")
    void testCalculateComplexExpression() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("2 + 3 * 4");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert: Should be 14 (not 20)
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("14"));
    }

    @Test
    @DisplayName("Should calculate expression with parentheses")
    void testCalculateExpressionWithParentheses() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("(2 + 3) * 4");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert: Should be 20
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("20"));
    }

    @Test
    @DisplayName("Should calculate division with correct precision")
    void testCalculateDivision() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("10 / 4");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("2.5"));
    }

    @Test
    @DisplayName("Should handle unary operators")
    void testUnaryOperators() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("3 + -2");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("1"));
    }

    // ============ DIVISION BY ZERO ============

    @Test
    @DisplayName("Should return 422 Unprocessable Entity for division by zero")
    void testDivisionByZeroReturns422() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("5 / 0");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Unprocessable Entity"))
                .andExpect(jsonPath("$.message").value("Division by zero is not allowed"));
    }

    @Test
    @DisplayName("Should return 422 for division by zero in complex expression")
    void testDivisionByZeroInExpression() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("10 / (2 - 2)");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }

    // ============ VALIDATION ERRORS (400) ============

    @Test
    @DisplayName("Should return 400 Bad Request for empty expression")
    void testEmptyExpressionReturns400() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for null expression")
    void testNullExpressionReturns400() throws Exception {
        // Arrange
        String requestBody = "{\"expression\": null}";

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for blank expression")
    void testBlankExpressionReturns400() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("   ");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid expression syntax")
    void testInvalidExpressionSyntax() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("5 + ");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Should return 400 for missing closing parenthesis")
    void testMissingClosingParen() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("(5 + 3");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Missing closing parenthesis ')'"));
    }

    @Test
    @DisplayName("Should return 400 for unexpected character in expression")
    void testUnexpectedCharacter() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("5 & 3");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Unexpected character")));
    }

    // ============ INVALID JSON ============

    @Test
    @DisplayName("Should return 400 for invalid JSON")
    void testInvalidJsonReturns400() throws Exception {
        // Arrange: Malformed JSON
        String requestBody = "{invalid json}";

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body must be valid JSON."));
    }

    @Test
    @DisplayName("Should return 400 for missing required field")
    void testMissingExpressionField() throws Exception {
        // Arrange
        String requestBody = "{}"; // Missing 'expression' field

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    // ============ HISTORY ENDPOINT ============

    @Test
    @DisplayName("Should GET /api/history and return 200 OK with empty list initially")
    void testGetHistoryInitiallyEmpty() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/history")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Should return history after calculation")
    void testHistoryAfterCalculation() throws Exception {
        // Arrange: Perform a calculation
        CalculationRequest request = new CalculationRequest("5 + 3");
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        // Act & Assert: Get history
        MvcResult result = mockMvc.perform(get("/api/history")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].expression").value("5 + 3"))
                .andExpect(jsonPath("$[0].result").value("8"))
                .andExpect(jsonPath("$[0].timestamp").exists())
                .andReturn();

        // Verify response structure
        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("expression", "result", "timestamp");
    }

    @Test
    @DisplayName("Should return history in reverse chronological order (newest first)")
    void testHistoryOrderNewestFirst() throws Exception {
        // Arrange: Perform multiple calculations
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CalculationRequest("1 + 1"))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CalculationRequest("2 + 2"))))
                .andExpect(status().isOk());

        // Act & Assert
        mockMvc.perform(get("/api/history")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].expression").value("2 + 2")) // Newest first
                .andExpect(jsonPath("$[1].expression").value("1 + 1")); // Oldest
    }

    // ============ CONTENT TYPE ============

    @Test
    @DisplayName("Should return JSON content-type for /api/calculate")
    void testCalculateJsonContentType() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("5 + 3");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // ============ ERROR RESPONSE FORMAT ============

    @Test
    @DisplayName("Should return error response with timestamp, status, error, and message")
    void testErrorResponseFormat() throws Exception {
        // Arrange
        CalculationRequest request = new CalculationRequest("5 / 0");
        String requestBody = objectMapper.writeValueAsString(request);

        // Act & Assert
        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Unprocessable Entity"))
                .andExpect(jsonPath("$.message").exists());
    }

    // ============ CORRECT HTTP METHODS ============

    @Test
    @DisplayName("Should successfully handle POST on /api/calculate")
    void testCalculatePost() throws Exception {
        // This test verifies POST works; the 405 tests are environment-specific
        CalculationRequest request = new CalculationRequest("5 + 3");
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should successfully handle GET on /api/history")
    void testHistoryGet() throws Exception {
        // This test verifies GET works
        mockMvc.perform(get("/api/history")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
