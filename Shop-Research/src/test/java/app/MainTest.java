package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class MainTest {

    @Test
    void testMainOutput() {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString().trim();

        // Expected full output with line breaks
        String expected = """
                Products: (1) apple (2) grape (3) mango (4) apple (5) orange 
                Sales, EUR: (1) 1520.89 (2) 2058.35 (3) 1807.29 (4) 899.99 (5) 1924.25 
                """.trim();

        assertTrue(output.contains("Products: (1) apple (2) grape (3) mango (4) apple (5) orange"));
        assertTrue(output.contains("Sales, EUR: (1) 1520.89 (2) 2058.35 (3) 1807.29 (4) 899.99 (5) 1924.25"));

    }
}
