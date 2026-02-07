package app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-style tests for Main class.
 * Captures System.out and verifies the program's console output.
 */
class MainTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        // Redirect System.out to our captor
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
        // Clean up the buffer
        outputStreamCaptor.reset();
    }

    @Test
    @DisplayName("Main should print correct numbered list and name at index 2")
    void main_PrintsExpectedOutput() {
        // Run the main method
        Main.main(new String[]{});

        // Get captured output and normalize line endings
        String actualOutput = outputStreamCaptor.toString()
                .replace("\r\n", "\n")   // Normalize Windows line endings
                .trim();                 // Remove trailing newlines if any

        // Expected output (exactly as per task specification)
        String expectedOutput =
                "Names:\n" +
                        "1) Alice\n" +
                        "2) Bob\n" +
                        "3) Lucy\n" +
                        "4) Denis\n" +
                        "5) Tom\n" +
                        "\n" +
                        "Name: Lucy is in index 2";

        // For easier debugging - print both if assertion fails
        if (!expectedOutput.equals(actualOutput)) {
            System.err.println("=== EXPECTED ===");
            System.err.println(expectedOutput);
            System.err.println("=== ACTUAL ===");
            System.err.println(actualOutput);
            System.err.println("===============");
        }

        assertEquals(expectedOutput, actualOutput,
                "Main program output does not match the expected format");
    }

    @Test
    @DisplayName("Output should contain exactly 5 numbered names")
    void main_OutputContainsFiveNames() {
        Main.main(new String[]{});

        String output = outputStreamCaptor.toString();

        // Simple checks on structure
        assertTrue(output.contains("1) Alice"));
        assertTrue(output.contains("2) Bob"));
        assertTrue(output.contains("3) Lucy"));
        assertTrue(output.contains("4) Denis"));
        assertTrue(output.contains("5) Tom"));
        assertTrue(output.contains("Name: Lucy is in index 2"));
    }
}