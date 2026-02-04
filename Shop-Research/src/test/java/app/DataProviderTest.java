package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataProviderTest {

    @Test
    void testGetProductNames() {
        // Product names should match expected values
        DataProvider provider = new DataProvider();
        String[] names = provider.getProductNames();
        assertArrayEquals(new String[]{"apple", "grape", "mango", "apple", "orange"}, names);
    }

    @Test
    void testGetSalesAmounts() {
        // Sales amounts should match expected values
        DataProvider provider = new DataProvider();
        Double[] sales = provider.getSalesAmounts();
        assertArrayEquals(new Double[]{1520.89, 2058.35, 1807.29, 899.99, 1924.25}, sales);
    }
}
