package app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Constructor and getters work correctly")
    void testConstructorAndGetters() {
        Product product = new Product("Test Phone", "Gadgets", 599.99);

        assertEquals("Test Phone", product.getName());
        assertEquals("Gadgets", product.getCategory());
        assertEquals(599.99, product.getPrice(), 0.0001);
    }

    @Test
    @DisplayName("toString returns correct string representation")
    void testToString() {
        Product product = new Product("Laptop", "Electronics", 1200.0);
        String expected = "Product{name='Laptop', category='Electronics', price=1200.0}";
        assertEquals(expected, product.toString());
    }

    @Test
    @DisplayName("Setters correctly update field values")
    void testSetters() {
        Product product = new Product("OldName", "OldCategory", 10.0);

        product.setName("NewName");
        product.setCategory("NewCategory");
        product.setPrice(99.99);

        assertEquals("NewName", product.getName());
        assertEquals("NewCategory", product.getCategory());
        assertEquals(99.99, product.getPrice(), 0.0001);
    }
}