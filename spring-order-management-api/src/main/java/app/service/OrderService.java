package app.service;

import app.exception.ResourceNotFoundException;
import app.model.Order;
import app.model.Product;
import app.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer containing business logic for order management.
 */
@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Retrieves an order by its ID with products fetched eagerly.
     * @throws ResourceNotFoundException if order is not found
     */
    public Order getOrderById(Long id) {
        return orderRepository.findByIdWithProducts(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    /**
     * Retrieves all orders from the database.
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAllWithProducts();
    }

    /**
     * Creates a new order with automatic total cost calculation.
     */
    @Transactional
    public Order createOrder(Order order) {
        BigDecimal totalCost = BigDecimal.ZERO;

        if (order.getProducts() != null) {
            for (Product product : order.getProducts()) {
                if (product.getPrice() != null) {
                    totalCost = totalCost.add(product.getPrice());
                }
                product.setOrder(order);
            }
        }

        order.setTotalCost(totalCost);
        return orderRepository.save(order);
    }

    /**
     * Deletes an order by its ID.
     * @throws ResourceNotFoundException if order is not found
     */
    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }

    /**
     * Updates an existing order with new details.
     * Total cost is recalculated server-side based on product prices.
     * Uses collection replacement instead of clear() to avoid UnsupportedOperationException.
     * @throws ResourceNotFoundException if order is not found
     */
    @Transactional
    public Order updateOrder(Long id, Order orderDetails) {
        Order order = getOrderById(id);

        // Replace products collection instead of clearing to avoid Hibernate PersistentBag issues
        if (orderDetails.getProducts() != null) {
            List<Product> newProducts = new ArrayList<>();
            for (Product product : orderDetails.getProducts()) {
                product.setOrder(order);
                newProducts.add(product);
            }
            order.setProducts(newProducts);
        }

        // Recalculate total cost server-side - never trust client input for financial data
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Product product : order.getProducts()) {
            if (product.getPrice() != null) {
                totalCost = totalCost.add(product.getPrice());
            }
        }
        order.setTotalCost(totalCost);

        return orderRepository.save(order);
    }
}