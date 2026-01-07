package app;

import java.math.BigDecimal;

// Main class for running the program
public class Main {

    public static void main(String[] args) {
        // Get input data
        String[] data = getData();

        // Create product object
        Product product = new Product(
                data[0],
                Integer.parseInt(data[1]),
                new BigDecimal(data[2])
        );

        // Calculate base cost
        CostCalculator baseCalculator = new CalcCostBase();
        BigDecimal baseCost = baseCalculator.calcCost(product);

        // Calculate cost including delivery
        CostCalculator deliveryCalculator = new CalcCostDelivery();
        BigDecimal deliveryCost = deliveryCalculator.calcCost(product);

        // Prepare output
        String baseOutput = product + "\nCost is " +
                Constants.CURRENCY + " " + baseCost + ".";
        String deliveryOutput = product + "\nCost is " +
                Constants.CURRENCY + " " + deliveryCost + ".";

        // Print results
        getOutput(baseOutput);
        getOutput(deliveryOutput);
    }

    // Input data set
    public static String[] getData() {
        return new String[]{"abc", "5", "2.5"};
    }

    // Output method
    public static void getOutput(String output) {
        System.out.println(output);
    }
}
