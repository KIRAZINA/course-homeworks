package app;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CalcCostBaseTest {

    @Test
    void testCalcCostNormalCase() {
        // Base cost should be price * quota
        Product product = new Product("abc", 5, new BigDecimal("2.5"));
        CalcCostBase calculator = new CalcCostBase();

        BigDecimal result = calculator.calcCost(product);

        assertEquals(new BigDecimal("12.5"), result);
    }

    @Test
    void testCalcCostZeroQuota() {
        Product product = new Product("abc", 0, new BigDecimal("2.5"));
        CalcCostBase calculator = new CalcCostBase();

        BigDecimal result = calculator.calcCost(product);

        // Use compareTo to ignore scale differences
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }


    @Test
    void testCalcCostZeroPrice() {
        // Zero price should result in zero cost
        Product product = new Product("abc", 5, BigDecimal.ZERO);
        CalcCostBase calculator = new CalcCostBase();

        BigDecimal result = calculator.calcCost(product);

        assertEquals(BigDecimal.ZERO, result);
    }
}
