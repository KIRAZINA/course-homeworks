package app;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testToString() {
        // Product string representation should match expected format
        Product product = new Product("abc", 5, new BigDecimal("2.5"));

        String expected = "Product: abc, quota is 5 pcs., price is EUR 2.5.";
        assertEquals(expected, product.toString());
    }

    @Test
    void testRecordFields() {
        // Product record should correctly store fields
        Product product = new Product("xyz", 10, new BigDecimal("3.0"));

        assertEquals("xyz", product.name());
        assertEquals(10, product.quota());
        assertEquals(new BigDecimal("3.0"), product.price());
    }
}
