package app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration-style tests for the Logger singleton.
 * Focuses on realistic usage scenarios: multiple references,
 * consistent behavior across "different parts" of the application.
 */
class LoggerIntegrationTest {

    @Test
    @DisplayName("Multiple references from different parts of the application should point to the same instance")
    void shouldUseSingleInstanceAcrossDifferentReferences() {
        Logger partA = Logger.getInstance();
        Logger partB = Logger.getInstance();
        Logger partC = Logger.getInstance();

        assertThat(partA)
                .isSameAs(partB)
                .isSameAs(partC);
    }

    @Test
    @DisplayName("Log messages called from different references should be formatted consistently")
    void logCallsFromDifferentReferencesShouldProduceConsistentOutput() {
        Logger loggerA = Logger.getInstance();
        Logger loggerB = Logger.getInstance();

        // Capture output to verify format
        var outputStream = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outputStream));

        try {
            loggerA.log("INFO", "Application initialized");
            loggerB.log("ERROR", "Failed to connect to database");
            loggerA.log("WARN", "Configuration value is deprecated");

            String output = outputStream.toString();

            assertThat(output)
                    .contains("[INFO] Application initialized")
                    .contains("[ERROR] Failed to connect to database")
                    .contains("[WARN] Configuration value is deprecated");

            // All lines should start with timestamp pattern
            String[] lines = output.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                assertThat(line.trim())
                        .matches("^\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\] \\[(INFO|WARN|ERROR)\\].*$");
            }
        } finally {
            System.setOut(System.out);
        }
    }
}