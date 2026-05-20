package app.repository;

import app.model.Order;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderRepository {

    private final Map<Long, Order> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    // Deterministic order for consistent API responses
    public List<Order> findAll() {
        return storage.values().stream()
                .sorted(Comparator.comparing(Order::getId))
                .toList();
    }

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(idSequence.getAndIncrement());
        }
        storage.put(order.getId(), order);
        return order;
    }

    // Test utility only
    public void clear() {
        storage.clear();
        idSequence.set(1);
    }
}