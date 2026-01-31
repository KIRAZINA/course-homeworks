package app;

// Custom thread class
public class MyThread extends Thread {
    private final DataHandler dataHandler;

    public MyThread(String name, DataHandler dataHandler) {
        super(name); // Set thread name
        this.dataHandler = dataHandler;
    }

    @Override
    public void run() {
        // Demonstrate repeated work to show concurrency
        for (int i = 0; i < 3; i++) {
            dataHandler.getOutput();
            try {
                Thread.sleep(500); // simulate work and allow interleaving
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
