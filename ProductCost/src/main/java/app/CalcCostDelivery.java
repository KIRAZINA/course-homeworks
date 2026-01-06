package app;

// Class for calculating product cost including delivery
// This class extends CalcCostBase
public class CalcCostDelivery extends CalcCostBase {

    // Delivery price (fixed value)
    private final static double deliveryPrice = 7.0;

    // Calculation of product cost including delivery
    @Override
    public double calcCost(Product product) {
        // Base cost + delivery price
        return super.calcCost(product) + deliveryPrice;
    }
}
