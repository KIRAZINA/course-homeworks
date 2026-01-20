package app;

public class Main {

    public static void main(String[] args) {
        DataProvider provider = new DataProvider();
        DataHandler dataHandler = new DataHandler();

        // Processing the array of product names
        String namesOutput = dataHandler.handleData(provider.getProductNames());
        getOutput("Products: " + namesOutput);

        // Processing the array of sales amounts
        String salesOutput = dataHandler.handleData(provider.getSalesAmounts());
        getOutput("Sales, EUR: " + salesOutput);
    }

    // Outputting the result of the program
    private static void getOutput(String output) {
        System.out.println(output);
    }
}
