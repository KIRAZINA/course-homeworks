package app;

// Main class runs the application
public class Main {

    public static void main(String[] args) {
        // Get numbers from repository
        int[] numbers = new DataRepository().getData();
        // Create handler for modifying numbers
        DataHandler dataHandler = new DataHandler();

        // Iterate through numbers and modify them
        for (int num : numbers) {
            System.out.println("Initial value is " + num);
            int newNum = dataHandler.modify(num);
            System.out.println("New value is " + newNum);
        }
    }
}
