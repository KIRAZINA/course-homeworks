package app;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Shopping cart component.
 * IMPORTANT: Marked as prototype scope to ensure a fresh instance
 * is returned every time it's requested from the Spring context.
 * Cart manages only Product objects - no persistence logic.
 */
@Component
@Scope("prototype")
public class Cart {
    private final List<Product> items = new ArrayList<>();

    /**
     * Add a product to the cart.
     * Product must be retrieved from repository before adding.
     * @param product The product to add (must not be null)
     * @return true if added successfully
     */
    public boolean addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return items.add(product);
    }

    /**
     * Remove a single product instance from the cart by its ID.
     * Only removes the first matching item, not all occurrences.
     * @param productId ID of the product to remove
     * @return true if an item was removed, false if not found
     */
    public boolean removeProduct(Long productId) {
        if (productId == null) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(productId)) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Product> getItems() {
        return new ArrayList<>(items);
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clear() {
        items.clear();
    }

    public int getItemCount() {
        return items.size();
    }
}