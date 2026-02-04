package app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MyThreadTest {

    @Test
    void testThreadNameIsSet() {
        // Thread should keep the name passed in constructor
        DataHandler handler = new DataHandler();
        MyThread thread = new MyThread("CustomThread", handler);

        assertEquals("CustomThread", thread.getName());
    }

    @Test
    void testThreadRunProducesOutput() throws InterruptedException {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        DataHandler handler = new DataHandler();
        MyThread thread = new MyThread("TestThread", handler);

        thread.start();
        thread.join(); // Wait for thread to finish

        String output = outContent.toString();

        // Thread should print fruits list multiple times (3 iterations)
        int occurrences = output.split("TestThread:").length - 1;
        assertEquals(3, occurrences);
        assertTrue(output.contains("(1) orange (2) apple (3) plum (4) mango"));
    }

    @Test
    void testThreadInterrupted() throws InterruptedException {
        // Ensure thread handles interruption gracefully
        DataHandler handler = new DataHandler();
        MyThread thread = new MyThread("InterruptThread", handler);

        thread.start();
        thread.interrupt();
        thread.join();

        // If interrupted, thread should still terminate without exception
        assertFalse(thread.isAlive());
    }
}
