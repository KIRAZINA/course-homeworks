package app;

public class Main {

    public static void main(String[] args) {
        DataRepository repository = new DataRepository();
        DataHandler handler = new DataHandler();
        UIOperator uiOperator = new UIOperator();

        // Print numbered list of names
        uiOperator.getOutput(handler.formListOutput(repository.getData()));

        // Print name at index 2
        uiOperator.getOutput(handler.formOutput(repository.getData(), 2));
    }
}
