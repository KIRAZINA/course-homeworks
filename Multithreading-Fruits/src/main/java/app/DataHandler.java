package app;

// Handles synchronized output of fruits list
public class DataHandler {
    private final String[] fruits = new DataRepository().getData();

    public void getOutput() {
        // Build the string outside synchronized block
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (String fruit : fruits) {
            sb.append(String.format("(%d) %s ", count++, fruit));
        }

        // Critical section: only printing
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + ": " + sb);
        }
    }
}
