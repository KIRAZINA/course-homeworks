package app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testMainCreatesThreads() throws InterruptedException {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        // Allow threads to finish
        Thread.sleep(2500);

        String output = outContent.toString();

        // Both threads should appear in output
        assertTrue(output.contains("Thread 1: (1) orange (2) apple (3) plum (4) mango"));
        assertTrue(output.contains("Thread 2: (1) orange (2) apple (3) plum (4) mango"));
    }

    @Test
    void testMainConcurrency() throws InterruptedException {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        // Allow threads to finish
        Thread.sleep(2500);

        String output = outContent.toString();

        // Each thread should print 3 times
        int occurrencesThread1 = output.split("Thread 1:").length - 1;
        int occurrencesThread2 = output.split("Thread 2:").length - 1;

        assertEquals(3, occurrencesThread1);
        assertEquals(3, occurrencesThread2);
    }
}
