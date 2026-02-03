package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataHandlerTest {

    @Test
    void testModify() {
        DataHandler handler = new DataHandler();

        // Check multiplication by 3
        assertEquals(21, handler.modify(7));
        assertEquals(12, handler.modify(4));
        assertEquals(15, handler.modify(5));
        assertEquals(6, handler.modify(2));
    }

    @Test
    void testThreadSafety() throws InterruptedException {
        DataHandler handler = new DataHandler();
        int input = 10;

        // Run modification in multiple threads
        Runnable task = () -> {
            int result = handler.modify(input);
            assertEquals(30, result);
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
