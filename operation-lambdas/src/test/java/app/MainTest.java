package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MainTest {

    private Main.MathOperation addition;
    private Main.StringManipulator toUpperCase;
    private Function<String, Integer> uppercaseCounter;
    private Supplier<Integer> random100;

    @BeforeEach
    void setUp() {
        // Initialize the same way as in the main method
        addition = new Main.MathOperation() {
            @Override
            public int operate(int a, int b) {
                return a + b;
            }
        };

        toUpperCase = input -> (input == null) ? null : input.toUpperCase();

        uppercaseCounter = Main.StringProcessor::countUppercase;

        random100 = () -> Main.RandomNumberGenerator.generateRandomNumber(1, 100);
    }

    // ────────────────────────────────────────────────────────────────
    // MathOperation (anonymous class for addition)
    // ────────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
            "5,   7,   12",
            "0,   0,   0",
            "-3,  8,   5",
            "-10, -5,  -15",
            "100, -100, 0"
    })
    @DisplayName("Addition should compute correctly for various values")
    void addition_shouldComputeCorrectly(int a, int b, int expected) {
        assertThat(addition.operate(a, b)).isEqualTo(expected);
    }

    // ────────────────────────────────────────────────────────────────
    // StringManipulator (toUpperCase lambda)
    // ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("toUpperCase converts string to uppercase")
    void toUpperCase_convertsToUppercase() {
        assertThat(toUpperCase.manipulate("hello world")).isEqualTo("HELLO WORLD");
        assertThat(toUpperCase.manipulate("HeLLo WoRLd")).isEqualTo("HELLO WORLD");
    }

    @Test
    @DisplayName("toUpperCase handles Cyrillic and accented characters correctly")
    void toUpperCase_handlesCyrillicAndAccents() {
        assertThat(toUpperCase.manipulate("привіт світ")).isEqualTo("ПРИВІТ СВІТ");
        assertThat(toUpperCase.manipulate("café naïve")).isEqualTo("CAFÉ NAÏVE");
    }

    @Test
    @DisplayName("toUpperCase returns null for null input")
    void toUpperCase_returnsNullForNullInput() {
        assertThat(toUpperCase.manipulate(null)).isNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    @DisplayName("toUpperCase handles empty and whitespace inputs")
    void toUpperCase_handlesEmptyAndWhitespace(String input) {
        String result = toUpperCase.manipulate(input);
        assertThat(result).isEqualTo(input != null ? input.toUpperCase() : null);
    }

    // ────────────────────────────────────────────────────────────────
    // StringProcessor.countUppercase
    // ────────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "Uppercase count in ''{0}'' = {1}")
    @CsvSource({
            "HeLLo WoRLd,   6",
            "ЯКЩО ТАК,      7",
            "abc,           0",
            "ABC,           3",
            "A1B2C3!,       3"
    })
    void countUppercase_countsCorrectly(String input, int expected) {
        int count = Main.StringProcessor.countUppercase(input);
        assertThat(count).isEqualTo(expected);
    }

    @Test
    void countUppercase_returnsZeroForNull() {
        assertThat(Main.StringProcessor.countUppercase(null)).isZero();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t\n"})
    void countUppercase_returnsZeroForEmptyOrWhitespace(String input) {
        assertThat(Main.StringProcessor.countUppercase(input)).isZero();
    }

    // ────────────────────────────────────────────────────────────────
    // RandomNumberGenerator
    // ────────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "generateRandomNumber({0}, {1}) returns value in range")
    @CsvSource({
            "1,   100",
            "5,   10",
            "-5,  5",
            "0,   0",
            "-10, -1"
    })
    void generateRandomNumber_returnsValueInRange(int min, int max) {
        for (int i = 0; i < 100; i++) {  // multiple attempts to increase confidence
            int result = Main.RandomNumberGenerator.generateRandomNumber(min, max);
            assertThat(result)
                    .as("Value should be >= %d and <= %d", min, max)
                    .isBetween(min, max);
        }
    }

    @Test
    @DisplayName("generateRandomNumber throws exception when min > max")
    void generateRandomNumber_throwsWhenMinGreaterThanMax() {
        assertThatThrownBy(() -> Main.RandomNumberGenerator.generateRandomNumber(10, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("min must be <= max");
    }

    // ────────────────────────────────────────────────────────────────
    // Supplier (random100)
    // ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("random100 supplier always returns number between 1 and 100")
    void random100_supplierReturnsInRange() {
        for (int i = 0; i < 50; i++) {
            int value = random100.get();
            assertThat(value).isBetween(1, 100);
        }
    }
}