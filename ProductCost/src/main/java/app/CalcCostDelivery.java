package app;

import java.math.BigDecimal;

// Delivery cost calculator implementation
public class CalcCostDelivery extends CalcCostBase {

    // Delivery price as constant
    private static final BigDecimal DELIVERY_PRICE = BigDecimal.valueOf(7.0);

    @Override
    public BigDecimal calcCost(Product product) {
        return super.calcCost(product).add(DELIVERY_PRICE);
    }
}
