package app.controller;

import app.model.Order;
import app.model.Product;
import app.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    // ============== PingController Tests ==============

    @Test
    @DisplayName("GET /ping should return 200 OK with body 'OK'")
    void ping_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    // ============== OrderController Create Tests ==============

    @Test
    @DisplayName("POST /orders should create order with auto-calculated totalCost and generated createdAt")
    void createOrder_shouldCalculateTotalCostAndSetCreatedAt() throws Exception {
        Product product1 = new Product();
        product1.setName("Laptop");
        product1.setPrice(BigDecimal.valueOf(999.99));

        Product product2 = new Product();
        product2.setName("Mouse");
        product2.setPrice(BigDecimal.valueOf(29.99));

        Order orderRequest = new Order();
        orderRequest.setProducts(List.of(product1, product2));

        String responseContent = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.totalCost", equalTo(1029.98)))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andReturn().getResponse().getContentAsString();

        Order createdOrder = objectMapper.readValue(responseContent, Order.class);
        assertThat(orderRepository.findById(createdOrder.getId())).isPresent();
    }

    // ============== OrderController Read Tests ==============

    @Test
    @DisplayName("GET /orders/{id} should return order with eagerly loaded products")
    void getOrderById_shouldReturnOrderWithProducts() throws Exception {
        Order savedOrder = createAndSaveOrderWithProducts();
        Long orderId = savedOrder.getId();

        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(orderId.intValue())))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].name", equalTo("Keyboard")))
                .andExpect(jsonPath("$.products[1].name", equalTo("Monitor")));
    }

    @Test
    @DisplayName("GET /orders/{id} should return 404 with structured JSON when order not found")
    void getOrderById_shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        Long nonExistentId = 99999L;

        mockMvc.perform(get("/orders/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.error", equalTo("Not Found")))
                .andExpect(jsonPath("$.message", containsString("Order not found")));
    }

    @Test
    @DisplayName("GET /orders should return all orders without duplication (validates DISTINCT JOIN FETCH)")
    void getAllOrders_shouldReturnDistinctOrdersWithProducts() throws Exception {
        createAndSaveOrderWithProducts();
        createAndSaveOrderWithProducts();

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].products", everyItem(hasSize(2))));
    }

    // ============== OrderController Update Tests ==============

    @Test
    @DisplayName("PUT /orders/{id} should update order and recalculate totalCost server-side (ignores client input)")
    void updateOrder_shouldRecalculateTotalCostServerSide() throws Exception {
        Order existingOrder = createAndSaveOrderWithProducts();
        Long orderId = existingOrder.getId();

        Product newProduct = new Product();
        newProduct.setName("Headphones");
        newProduct.setPrice(BigDecimal.valueOf(149.99));

        Order updateRequest = new Order();
        updateRequest.setProducts(List.of(newProduct));
        updateRequest.setTotalCost(BigDecimal.valueOf(9999.99));

        mockMvc.perform(put("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost", equalTo(149.99)))
                .andExpect(jsonPath("$.products", hasSize(1)))
                .andExpect(jsonPath("$.products[0].name", equalTo("Headphones")));
    }

    // ============== OrderController Delete Tests ==============

    @Test
    @DisplayName("DELETE /orders/{id} should remove order and return 204 No Content")
    void deleteOrder_shouldRemoveOrderAndReturnNoContent() throws Exception {
        Order savedOrder = createAndSaveOrderWithProducts();
        Long orderId = savedOrder.getId();

        mockMvc.perform(delete("/orders/{id}", orderId))
                .andExpect(status().isNoContent());

        assertThat(orderRepository.findById(orderId)).isEmpty();
    }

    @Test
    @DisplayName("DELETE /orders/{id} should return 404 when order not found")
    void deleteOrder_shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        Long nonExistentId = 99999L;

        mockMvc.perform(delete("/orders/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    // ============== Validation Tests ==============

    @Test
    @DisplayName("POST /orders with blank product name should return 400 with structured field errors")
    void createOrder_withBlankProductName_shouldReturnBadRequest() throws Exception {
        Product invalidProduct = new Product();
        invalidProduct.setName("");
        invalidProduct.setPrice(BigDecimal.valueOf(10.00));

        Order orderRequest = new Order();
        orderRequest.setProducts(List.of(invalidProduct));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Validation failed")))
                .andExpect(jsonPath("$.fieldErrors", aMapWithSize(greaterThan(0))))
                .andExpect(jsonPath("$.fieldErrors.*", hasItem(notNullValue())));
    }

    @Test
    @DisplayName("POST /orders with negative price should return 400 with structured field errors")
    void createOrder_withNegativePrice_shouldReturnBadRequest() throws Exception {
        Product invalidProduct = new Product();
        invalidProduct.setName("Invalid Product");
        invalidProduct.setPrice(BigDecimal.valueOf(-50.00));

        Order orderRequest = new Order();
        orderRequest.setProducts(List.of(invalidProduct));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.fieldErrors", aMapWithSize(greaterThan(0))));
    }

    @Test
    @DisplayName("PUT /orders/{id} with null totalCost should still recalculate server-side")
    void updateOrder_withNullTotalCost_shouldRecalculateServerSide() throws Exception {
        Order existingOrder = createAndSaveOrderWithProducts();
        Long orderId = existingOrder.getId();

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(BigDecimal.valueOf(75.50));

        Order updateRequest = new Order();
        updateRequest.setProducts(List.of(updatedProduct));
        updateRequest.setTotalCost(null);

        mockMvc.perform(put("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost", equalTo(75.50)));
    }

    // ============== Helper Methods ==============

    private Order createAndSaveOrderWithProducts() {
        Product product1 = new Product();
        product1.setName("Keyboard");
        product1.setPrice(BigDecimal.valueOf(79.99));

        Product product2 = new Product();
        product2.setName("Monitor");
        product2.setPrice(BigDecimal.valueOf(299.99));

        Order order = new Order();
        order.setProducts(List.of(product1, product2));

        order.setTotalCost(product1.getPrice().add(product2.getPrice()));

        return orderRepository.save(order);
    }
}