package app.orderapi.servlet;

import app.orderapi.model.Order;
import app.orderapi.model.Product;
import app.orderapi.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServletTest {

    private OrderService mockService;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private ObjectMapper mapper;
    private OrderServlet servlet;

    @BeforeEach
    void setUp() {
        mockService = mock(OrderService.class);
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);

        // 👇 Configure mapper with JavaTimeModule for LocalDate support
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        servlet = new OrderServlet(mockService);
    }

    @Test
    void testPost_CreateOrderSuccessfully() throws Exception {
        Product product = new Product(1L, "Mechanical Keyboard", 120.50);
        Order inputOrder = new Order(null, null, 999.99, List.of(product)); // client sends wrong cost
        String jsonPayload = mapper.writeValueAsString(inputOrder);

        Order createdOrder = new Order(1L, LocalDate.now(), 120.50, List.of(product)); // server recalculates
        when(mockService.create(any(Order.class))).thenReturn(createdOrder);

        setupInputStream(jsonPayload);
        setupOutputStream();

        servlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_CREATED);
        verify(mockResponse).setContentType("application/json");
        verify(mockService).create(any(Order.class));
    }

    @Test
    void testPost_EmptyProducts_BadRequest() throws Exception {
        Order emptyOrder = new Order(null, null, 0.0, List.of());
        String jsonPayload = mapper.writeValueAsString(emptyOrder);

        when(mockService.create(any(Order.class)))
                .thenThrow(new IllegalArgumentException("Order must contain at least one product"));

        setupInputStream(jsonPayload);
        setupOutputStream();

        servlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Order must contain at least one product");
    }

    @Test
    void testGet_OrderExists() throws Exception {
        when(mockRequest.getParameter("id")).thenReturn("5");
        Product p = new Product(2L, "Mouse", 25.0);
        Order existing = new Order(5L, LocalDate.of(2024, 1, 15), 25.0, List.of(p));
        when(mockService.getById(5L)).thenReturn(Optional.of(existing));

        ByteArrayOutputStream baos = setupOutputStream();

        servlet.doGet(mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json");
        String responseJson = baos.toString(StandardCharsets.UTF_8);
        Order responseOrder = mapper.readValue(responseJson, Order.class);
        assertEquals(5L, responseOrder.getId());
        assertEquals(LocalDate.of(2024, 1, 15), responseOrder.getDate()); // date preserved
    }

    @Test
    void testGet_OrderNotFound_Returns404() throws Exception {
        when(mockRequest.getParameter("id")).thenReturn("99");
        when(mockService.getById(99L)).thenReturn(Optional.empty());

        servlet.doGet(mockRequest, mockResponse);

        // 👇 Verify 404 is returned, not 500
        verify(mockResponse).sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
        verifyNoMoreInteractions(mockResponse);
    }

    @Test
    void testPut_UpdateOrderSuccessfully() throws Exception {
        Product p = new Product(1L, "Monitor", 300.0);
        Order updatePayload = new Order(2L, LocalDate.of(1999, 1, 1), 999.99, List.of(p));
        String jsonPayload = mapper.writeValueAsString(updatePayload);

        Order updated = new Order(2L, LocalDate.of(2024, 3, 10), 300.0, List.of(p)); // date preserved, cost recalculated
        when(mockService.update(any(Order.class))).thenReturn(Optional.of(updated));

        setupInputStream(jsonPayload);
        setupOutputStream();

        servlet.doPut(mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json");
        verify(mockService).update(any(Order.class));
    }

    @Test
    void testPut_OrderNotFound_Returns404() throws Exception {
        Product p = new Product(1L, "Test", 10.0);
        Order updatePayload = new Order(999L, LocalDate.now(), 10.0, List.of(p));
        String jsonPayload = mapper.writeValueAsString(updatePayload);

        when(mockService.update(any(Order.class))).thenReturn(Optional.empty());

        setupInputStream(jsonPayload);
        setupOutputStream();

        servlet.doPut(mockRequest, mockResponse);

        // 👇 Critical: verify 404, not 500
        verify(mockResponse).sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
        verifyNoMoreInteractions(mockResponse);
    }

    @Test
    void testPut_EmptyProducts_BadRequest() throws Exception {
        Order invalidUpdate = new Order(1L, LocalDate.now(), 0.0, List.of());
        String jsonPayload = mapper.writeValueAsString(invalidUpdate);

        when(mockService.update(any(Order.class)))
                .thenThrow(new IllegalArgumentException("Order must contain at least one product"));

        setupInputStream(jsonPayload);
        setupOutputStream();

        servlet.doPut(mockRequest, mockResponse);

        verify(mockResponse).sendError(HttpServletResponse.SC_BAD_REQUEST, "Order must contain at least one product");
    }

    @Test
    void testDelete_OrderDeleted() throws Exception {
        when(mockRequest.getParameter("id")).thenReturn("3");
        when(mockService.delete(3L)).thenReturn(true);

        servlet.doDelete(mockRequest, mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_NO_CONTENT);
        verify(mockService).delete(3L);
    }

    @Test
    void testDelete_OrderNotFound() throws Exception {
        when(mockRequest.getParameter("id")).thenReturn("42");
        when(mockService.delete(42L)).thenReturn(false);

        servlet.doDelete(mockRequest, mockResponse);

        verify(mockResponse).sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
    }

    // Helper methods
    private void setupInputStream(String json) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        when(mockRequest.getInputStream()).thenReturn(new ServletInputStream() {
            @Override public int read() throws IOException { return bais.read(); }
            @Override public boolean isFinished() { return bais.available() == 0; }
            @Override public boolean isReady() { return true; }
            @Override public void setReadListener(jakarta.servlet.ReadListener l) {}
        });
    }

    private ByteArrayOutputStream setupOutputStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(mockResponse.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override public void write(int b) throws IOException { baos.write(b); }
            @Override public boolean isReady() { return true; }
            @Override public void setWriteListener(jakarta.servlet.WriteListener l) {}
        });
        return baos;
    }
}