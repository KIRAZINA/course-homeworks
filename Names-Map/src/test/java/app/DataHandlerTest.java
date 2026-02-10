package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for DataHandler class.
 */
class DataHandlerTest {

    private DataHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DataHandler();
    }

    @Test
    @DisplayName("getAll should return formatted list of all users")
    void getAll_shouldReturnFormattedList() {
        String result = handler.getAll();

        assertThat(result)
                .isNotNull()
                .startsWith("ALL NAMES:\n")
                .contains("ALL NAMES:")
                .contains("1) ")
                .contains("2) ")
                .contains("3) ")
                .contains("4) ")
                .contains("Lucy")
                .contains("Alice")
                .contains("Bob")
                .contains("Tom")
                .contains(", ")
                .doesNotContain("No data!")
                .doesNotContain("\\n");
    }

    @Test
    @DisplayName("getAll should contain exactly four entries")
    void getAll_shouldContainExactlyFourEntries() {
        String result = handler.getAll();

        long entryCount = result.lines()
                .filter(line -> line.matches("^\\d+\\)\\s*\\d+,\\s*\\w+$"))
                .count();

        assertThat(entryCount).isEqualTo(4);
    }

    @Test
    @DisplayName("getById should return correct name for existing ID")
    void getById_shouldReturnCorrectNameForExistingId() {
        assertThat(handler.getById(172))
                .isEqualTo("NAME: id 172, Tom");

        assertThat(handler.getById(231))
                .isEqualTo("NAME: id 231, Alice");

        assertThat(handler.getById(387))
                .isEqualTo("NAME: id 387, Lucy");

        assertThat(handler.getById(394))
                .isEqualTo("NAME: id 394, Bob");
    }

    @Test
    @DisplayName("getById should return 'No data!' for non-existing ID")
    void getById_shouldReturnNoDataForNonExistingId() {
        assertThat(handler.getById(999)).isEqualTo("No data!");
        assertThat(handler.getById(0)).isEqualTo("No data!");
        assertThat(handler.getById(-1)).isEqualTo("No data!");
        assertThat(handler.getById(1000)).isEqualTo("No data!");
    }

    @Test
    @DisplayName("getAll should include header and correct line breaks")
    void getAll_shouldHaveCorrectHeaderAndLineBreaks() {
        String result = handler.getAll();

        assertThat(result)
                .startsWith("ALL NAMES:\n")
                .endsWith("\n")  // last entry ends with newline
                .doesNotStartWith("\n");  // no leading empty line
    }
}