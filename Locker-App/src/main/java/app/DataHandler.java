package app;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// DataHandler class modifies numbers using a lock
public class DataHandler {

    // Lock object to ensure thread-safe modification
    private final Lock lock = new ReentrantLock();

    // Method multiplies the number by 3 in a thread-safe way
    public int modify(int num) {
        lock.lock(); // acquire lock
        try {
            num = num * 3; // modify number
            return num;
        } finally {
            lock.unlock(); // release lock
        }
    }
}
