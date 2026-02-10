package app;

public class Main {

    public static void main(String[] args) {
        DataHandler handler = new DataHandler();
        UIOperator uiOperator = new UIOperator();

        // Print all names
        uiOperator.getOutput(handler.getAll());

        // Print name by ID
        uiOperator.getOutput(handler.getById(172));
    }
}
