package app.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {

    // Getters
    @Setter
    @Getter
    private Long id;
    @Getter
    private final LocalDateTime creationDate;
    private final List<Product> products;

    public Order() {
        this.creationDate = LocalDateTime.now();
        this.products = new ArrayList<>();
    }

    public Order(List<Product> products) {
        this.creationDate = LocalDateTime.now();
        this.products = products != null
                ? new ArrayList<>(products)
                : new ArrayList<>();
    }

    // Defensive copy — prevents external mutation
    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    // Derived field — always consistent, never stored
    public BigDecimal getTotalCost() {
        return products.stream()
                .map(Product::getCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}