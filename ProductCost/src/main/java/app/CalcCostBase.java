package app;

// Class for calculating base cost of product
public class CalcCostBase {

    // Base calculation: quota * price
    public double calcCost(Product product) {
        return product.getQuota() * product.getPrice();
    }
}
