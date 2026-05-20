package app.service;

import app.model.Order;
import app.model.Product;
import app.model.dto.CreateOrderRequest;
import app.model.dto.OrderResponse;
import app.repository.OrderRepository;
import app.exception.OrderValidationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<OrderResponse> getAllOrders() {
        return repository.findAll().stream()
                .map(OrderResponse::new)
                .toList();
    }

    public Optional<OrderResponse> getOrderById(Long id) {
        return repository.findById(id).map(OrderResponse::new);
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        validateProducts(request.getProducts());
        Order order = new Order(request.getProducts());
        Order saved = repository.save(order);
        return new OrderResponse(saved);
    }

    // Domain-level validation (beyond what annotations can express)
    private void validateProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new OrderValidationException("Order must contain products");
        }
        for (Product p : products) {
            if (p.getCost() == null || p.getCost().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new OrderValidationException("Invalid cost for product: " + p.getName());
            }
        }
    }
}