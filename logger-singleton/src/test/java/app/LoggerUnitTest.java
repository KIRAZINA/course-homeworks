package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for the Logger singleton class.
 * Verifies core singleton behavior, thread-safety, reflection protection,
 * and correct formatting of log messages.
 */
@Execution(ExecutionMode.CONCURRENT)
class LoggerUnitTest {

    private Logger logger;

    @BeforeEach
    void setUp() {
        logger = Logger.getInstance();
    }

    @Test
    @DisplayName("getInstance() should always return the same instance")
    void shouldReturnTheSameInstance() {
        Logger secondInstance = Logger.getInstance();

        assertThat(logger).isSameAs(secondInstance);
        assertThat(logger).isEqualTo(secondInstance);
    }

    @Test
    @DisplayName("Multiple threads should receive the same singleton instance")
    void shouldBeThreadSafeWhenAccessedConcurrently() throws InterruptedException {
        final int threadCount = 100;
        Thread[] threads = new Thread[threadCount];
        final Logger[] instances = new Logger[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> instances[index] = Logger.getInstance());
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Logger firstInstance = instances[0];
        for (Logger instance : instances) {
            assertThat(instance).isSameAs(firstInstance);
        }
    }

    @Test
    @DisplayName("Reflection should not allow creating additional instances")
    void shouldPreventInstantiationViaReflection() throws Exception {
        Constructor<Logger> constructor = Logger.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Throwable thrown = catchThrowable(() -> constructor.newInstance());

        assertThat(thrown)
                .isInstanceOf(InvocationTargetException.class);

        assertThat(thrown.getCause())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Logger instance already created. Use getInstance() method.");
    }

    @Test
    @DisplayName("Logged message should contain timestamp, level and message in correct format")
    void logShouldProduceCorrectlyFormattedOutput() {
        // Capture System.out
        var outputStream = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outputStream));

        try {
            logger.log("INFO", "Test message 123");

            String output = outputStream.toString().trim();

            // Expected pattern: [yyyy-MM-dd HH:mm:ss] [LEVEL] message
            assertThat(output)
                    .matches(Pattern.compile(
                            "^\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\] \\[INFO\\] Test message 123$"
                    ));

            // Verify that timestamp is parseable
            String timestampPart = output.substring(1, 20);
            LocalDateTime.parse(timestampPart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } finally {
            // Restore original System.out
            System.setOut(System.out);
        }
    }
}