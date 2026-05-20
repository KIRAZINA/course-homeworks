package app;

import app.model.dto.CreateOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Full order lifecycle: create → get by id → get all")
    void fullOrderLifecycle_worksCorrectly() throws Exception {
        // 1. Create order
        String createJson = """
                {
                  "products": [
                    {"name": "Monitor", "cost": 299.99},
                    {"name": "Cable", "cost": 15.50}
                  ]
                }
                """;

        var createResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalCost").value(315.49))
                .andReturn();

        // Extract created order ID from response
        var response = objectMapper.readTree(
                createResult.getResponse().getContentAsString());
        Long orderId = response.get("id").asLong();

        // 2. Get order by ID
        mockMvc.perform(get("/orders/" + orderId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products").value(org.hamcrest.Matchers.hasSize(2)));

        // 3. Get all orders includes the new one
        mockMvc.perform(get("/orders").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.id==%d)]", orderId).exists());
    }

    @Test
    @DisplayName("creationDate is serialized as ISO string, not timestamp")
    void creationDate_serializedAsIsoString() throws Exception {
        String json = """
                {"products": [{"name": "Test", "cost": 10}]}
                """;

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.creationDate").value(
                        org.hamcrest.Matchers.containsString("T")));
    }
}