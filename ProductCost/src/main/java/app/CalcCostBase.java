package app;

import java.math.BigDecimal;

// Base cost calculator implementation
public class CalcCostBase implements CostCalculator {

    @Override
    public BigDecimal calcCost(Product product) {
        return product.price().multiply(BigDecimal.valueOf(product.quota()));
    }
}
