package app.orderapi.service;

import app.orderapi.model.Order;
import app.orderapi.model.Product;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory service handling CRUD operations for Order entities.
 * Thread-safe for basic concurrent access.
 */
public class OrderService {
    private final Map<Long, Order> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Creates a new order.
     * - Auto-generates ID
     * - Sets creation date to today (ignores client-provided date)
     * - Recalculates cost from products (ignores client-provided cost)
     * - Validates that products list is not null/empty
     */
    public Order create(Order order) {
        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }

        order.setId(idGenerator.getAndIncrement());
        order.setDate(LocalDate.now()); // 👈 Server-controlled creation date
        order.setCost(calculateTotalCost(order)); // 👈 Server-calculated cost
        storage.put(order.getId(), order);
        return order;
    }

    public Optional<Order> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    /**
     * Updates an existing order.
     * - Preserves original creation date (ignores client-provided date)
     * - Recalculates cost from products
     * - Validates products list
     */
    public Optional<Order> update(Order order) {
        if (order.getId() == null || !storage.containsKey(order.getId())) {
            return Optional.empty();
        }
        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product");
        }

        Order existing = storage.get(order.getId());
        order.setDate(existing.getDate());
        order.setCost(calculateTotalCost(order));
        storage.put(order.getId(), order);
        return Optional.of(order);
    }

    public boolean delete(Long id) {
        return storage.remove(id) != null;
    }

    private double calculateTotalCost(Order order) {
        if (order.getProducts() == null) return 0.0;
        return order.getProducts().stream()
                .mapToDouble(Product::getCost)
                .sum();
    }
}