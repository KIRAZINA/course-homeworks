package app;

// Main class for running the program
public class Main {

    public static void main(String[] args) {
        // Get input data
        String[] data = getData();

        // Create product object from input data
        Product product = new Product(
                data[0],
                Integer.parseInt(data[1]),
                Double.parseDouble(data[2])
        );

        // Calculate base cost
        CalcCostBase costBase = new CalcCostBase();
        double baseCost = costBase.calcCost(product);

        // Calculate cost including delivery
        CalcCostDelivery costDelivery = new CalcCostDelivery();
        double deliveryCost = costDelivery.calcCost(product);

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
