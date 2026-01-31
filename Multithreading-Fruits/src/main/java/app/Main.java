package app;

// Entry point of the application
public class Main {

    public static void main(String[] args) {
        // Create a shared DataHandler instance
        DataHandler dataHandler = new DataHandler();

        // Create two threads with different names but the same DataHandler
        MyThread myThread1 = new MyThread("Thread 1", dataHandler);
        MyThread myThread2 = new MyThread("Thread 2", dataHandler);

        // Start both threads to run concurrently
        myThread1.start();
        myThread2.start();
    }
}
