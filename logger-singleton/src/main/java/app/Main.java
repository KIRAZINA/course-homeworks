package app;

/**
 * Demonstration of the Logger Singleton pattern usage.
 * Shows that multiple references point to the same instance
 * and that log messages are recorded correctly.
 */
public class Main {

    public static void main(String[] args) {
        // Obtain singleton instances from different "parts" of the application
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();

        // Verify that both variables refer to the same object
        System.out.println("logger1 == logger2 ? " + (logger1 == logger2));

        // Log messages with different severity levels
        logger1.log("INFO", "Application started.");
        logger2.log("WARN", "Low memory warning detected.");
        logger1.log("ERROR", "Unexpected error occurred during processing.");
    }
}