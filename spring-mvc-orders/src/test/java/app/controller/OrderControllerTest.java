package app.controller;

import app.model.dto.CreateOrderRequest;
import app.model.dto.OrderResponse;
import app.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller tests - focus on HTTP status codes, not JSON details.
 * JSON structure verified in IntegrationTest instead.
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /orders returns 200")
    void getAllOrders_returnsOk() throws Exception {
        given(orderService.getAllOrders()).willReturn(List.of());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /orders/{id} returns 200 when found")
    void getOrderById_returnsOk() throws Exception {
        var order = new app.model.Order(List.of(new app.model.Product("Test", BigDecimal.ONE)));
        order.setId(1L);
        given(orderService.getOrderById(1L)).willReturn(Optional.of(new OrderResponse(order)));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /orders/{id} returns 404 when not found")
    void getOrderById_returnsNotFound() throws Exception {
        given(orderService.getOrderById(999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /orders returns 201 for valid request")
    void createOrder_returnsCreated() throws Exception {
        var request = new CreateOrderRequest();
        request.setProducts(List.of(new app.model.Product("Item", BigDecimal.TEN)));

        var order = new app.model.Order(request.getProducts());
        order.setId(1L);
        given(orderService.createOrder(request)).willReturn(new OrderResponse(order));

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /orders returns 400 for invalid request")
    void createOrder_returnsBadRequest() throws Exception {
        // Empty products violates @NotEmpty annotation
        var request = new CreateOrderRequest();
        request.setProducts(List.of());

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}