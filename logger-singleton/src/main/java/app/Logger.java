package app;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Thread-safe Singleton implementation of a Logger class using the Bill Pugh (Initialization-on-demand holder) idiom.
 * This class provides a single, globally accessible instance for logging messages with timestamp and severity level.
 */
public class Logger implements Serializable {

    // Formatter for consistent timestamp in log entries
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Includes protection against reflection-based instantiation.
     */
    private Logger() {
        if (Holder.INSTANCE != null) {
            throw new IllegalStateException("Logger instance already created. Use getInstance() method.");
        }
    }

    /**
     * Static inner class that holds the single instance.
     * The instance is created only when the class is first referenced (lazy initialization).
     * This approach provides thread-safety without synchronization overhead.
     */
    private static class Holder {
        private static final Logger INSTANCE = new Logger();
    }

    /**
     * Returns the single instance of the Logger class.
     *
     * @return the singleton Logger instance
     */
    public static Logger getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Ensures that deserialization returns the existing singleton instance instead of creating a new one.
     *
     * @return the existing Logger instance
     * @throws ObjectStreamException if an error occurs during deserialization
     */
    protected Object readResolve() throws ObjectStreamException {
        return getInstance();
    }

    /**
     * Logs a message with a timestamp and severity level to the console.
     *
     * @param level   the severity level (e.g., "INFO", "WARN", "ERROR")
     * @param message the message to be logged
     */
    public void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        System.out.println("[" + timestamp + "] [" + level + "] " + message);
    }
}