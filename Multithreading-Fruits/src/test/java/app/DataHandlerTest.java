package app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class DataHandlerTest {

    @Test
    void testGetOutputSingleThread() {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        DataHandler handler = new DataHandler();
        handler.getOutput();

        String output = outContent.toString().trim();

        // Output should contain all fruits with numbering
        assertTrue(output.contains("(1) orange (2) apple (3) plum (4) mango"));
    }

    @Test
    void testGetOutputThreadNameIncluded() {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        DataHandler handler = new DataHandler();
        Thread.currentThread().setName("JUnitThread");
        handler.getOutput();

        String output = outContent.toString().trim();

        // Output should start with thread name
        assertTrue(output.startsWith("JUnitThread:"));
    }

    @Test
    void testGetOutputMultiThread() throws InterruptedException {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        DataHandler handler = new DataHandler();

        Thread t1 = new Thread(handler::getOutput, "Thread A");
        Thread t2 = new Thread(handler::getOutput, "Thread B");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        String output = outContent.toString();

        // Both threads should print their names and fruits list
        assertTrue(output.contains("Thread A: (1) orange (2) apple (3) plum (4) mango"));
        assertTrue(output.contains("Thread B: (1) orange (2) apple (3) plum (4) mango"));
    }

    @Test
    void testSynchronizationEnsuresNoInterleaving() throws InterruptedException {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        DataHandler handler = new DataHandler();

        Thread t1 = new Thread(handler::getOutput, "Thread A");
        Thread t2 = new Thread(handler::getOutput, "Thread B");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        String output = outContent.toString();

        // Each line should be complete (no partial interleaving)
        for (String line : output.split("\n")) {
            assertTrue(line.contains("(1) orange (2) apple (3) plum (4) mango"));
        }
    }
}
