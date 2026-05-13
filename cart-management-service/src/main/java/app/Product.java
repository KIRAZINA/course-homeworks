package app;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

/**
 * Immutable domain object representing a store product.
 * Uses BigDecimal for price to avoid floating-point precision issues.
 * Validates constructor arguments to ensure domain integrity.
 */
public final class Product {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public Product(Long id, String name, BigDecimal price) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        this.id = id;
        this.name = name.trim();
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }

    @Override
    public String toString() {
        return String.format(Locale.US, "[ID: %d | Name: %-20s | Price: $%.2f]",
                id, name, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}