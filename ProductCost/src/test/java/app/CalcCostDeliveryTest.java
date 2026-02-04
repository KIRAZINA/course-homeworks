package app;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CalcCostDeliveryTest {

    @Test
    void testCalcCostWithDelivery() {
        // Delivery cost should be base cost + 7.0
        Product product = new Product("abc", 5, new BigDecimal("2.5"));
        CalcCostDelivery calculator = new CalcCostDelivery();

        BigDecimal result = calculator.calcCost(product);

        assertEquals(new BigDecimal("19.5"), result);
    }

    @Test
    void testCalcCostZeroBase() {
        // Even if base cost is zero, delivery cost should be 7.0
        Product product = new Product("abc", 0, new BigDecimal("2.5"));
        CalcCostDelivery calculator = new CalcCostDelivery();

        BigDecimal result = calculator.calcCost(product);

        assertEquals(new BigDecimal("7.0"), result);
    }
}
