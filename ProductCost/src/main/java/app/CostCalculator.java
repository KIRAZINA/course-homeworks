package app;

import java.math.BigDecimal;

// Strategy interface for cost calculation
public interface CostCalculator {
    BigDecimal calcCost(Product product);
}
