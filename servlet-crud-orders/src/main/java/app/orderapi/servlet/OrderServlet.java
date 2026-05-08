package app.orderapi.servlet;

import app.orderapi.model.Order;
import app.orderapi.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet("/api/orders")
public class OrderServlet extends HttpServlet {
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public OrderServlet() {
        this.orderService = new OrderService();
        this.objectMapper = createObjectMapper();
    }

    // Package-private for testability (same-package access)
    OrderServlet(OrderService orderService) {
        this.orderService = orderService;
        this.objectMapper = createObjectMapper();
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Order order = objectMapper.readValue(req.getInputStream(), Order.class);
            Order created = orderService.create(order);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), created);
        } catch (IllegalArgumentException e) {
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        } catch (JsonProcessingException e) {
            resp.sendError(SC_BAD_REQUEST, "Invalid JSON payload");
        } catch (Exception e) {
            resp.sendError(SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(SC_BAD_REQUEST, "Missing required parameter: id");
            return;
        }
        try {
            Long id = Long.parseLong(idParam);
            Optional<Order> orderOpt = orderService.getById(id);

            if (orderOpt.isPresent()) {
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), orderOpt.get());
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            }
        } catch (NumberFormatException e) {
            resp.sendError(SC_BAD_REQUEST, "Invalid ID format");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Order order = objectMapper.readValue(req.getInputStream(), Order.class);
            Optional<Order> updatedOpt = orderService.update(order);

            if (updatedOpt.isPresent()) {
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), updatedOpt.get());
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            }
        } catch (IllegalArgumentException e) {
            resp.sendError(SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            resp.sendError(SC_BAD_REQUEST, "Invalid JSON or missing order ID");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendError(SC_BAD_REQUEST, "Missing required parameter: id");
            return;
        }
        try {
            Long id = Long.parseLong(idParam);
            if (orderService.delete(id)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            }
        } catch (NumberFormatException e) {
            resp.sendError(SC_BAD_REQUEST, "Invalid ID format");
        }
    }
}