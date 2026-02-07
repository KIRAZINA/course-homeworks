package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DataHandler class.
 */
class DataHandlerTest {

    private DataHandler handler;
    private List<String> sampleNames;

    @BeforeEach
    void setUp() {
        handler = new DataHandler();
        sampleNames = Arrays.asList("Alice", "Bob", "Lucy", "Denis", "Tom");
    }

    // ───────────────────────────────────────────────
    // Tests for formOutput(List<String>, int)
    // ───────────────────────────────────────────────

    @ParameterizedTest(name = "index {0} → {1}")
    @CsvSource({
            "0, Alice",
            "1, Bob",
            "2, Lucy",
            "3, Denis",
            "4, Tom"
    })
    @DisplayName("formOutput should return correct name with index message")
    void formOutput_ReturnsCorrectNameAndIndex(int index, String expectedName) {
        String result = handler.formOutput(sampleNames, index);
        String expected = "Name: " + expectedName + " is in index " + index;

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("formOutput should handle negative index correctly")
    void formOutput_NegativeIndex_ReturnsWrongIndexMessage() {
        String result = handler.formOutput(sampleNames, -1);
        assertEquals("Wrong index!", result);
    }

    @Test
    @DisplayName("formOutput should handle index out of bounds (too large)")
    void formOutput_IndexTooLarge_ReturnsWrongIndexMessage() {
        String result = handler.formOutput(sampleNames, 5);
        assertEquals("Wrong index!", result);
    }

    @Test
    @DisplayName("formOutput should throw NullPointerException when list is null")
    void formOutput_NullList_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                handler.formOutput(null, 0));
    }

    // ───────────────────────────────────────────────
    // Tests for formListOutput(List<String>)
    // ───────────────────────────────────────────────

    @Test
    @DisplayName("formListOutput should format list correctly with sample data")
    void formListOutput_FormatsCorrectlyWithSampleData() {
        String result = handler.formListOutput(sampleNames);

        String expected =
                "\nNames:\n" +
                        "1) Alice\n" +
                        "2) Bob\n" +
                        "3) Lucy\n" +
                        "4) Denis\n" +
                        "5) Tom\n";

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("formListOutput should handle empty list correctly")
    void formListOutput_EmptyList_ReturnsOnlyHeader() {
        String result = handler.formListOutput(Collections.emptyList());

        String expected = "\nNames:\n";

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("formListOutput should handle single-element list")
    void formListOutput_SingleElement_FormatsCorrectly() {
        List<String> single = List.of("Zoe");
        String result = handler.formListOutput(single);

        String expected = "\nNames:\n1) Zoe\n";

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("formListOutput should throw NullPointerException when list is null")
    void formListOutput_NullList_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                handler.formListOutput(null));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 10})
    @DisplayName("formListOutput output should contain correct number prefix")
    void formListOutput_ContainsCorrectNumbering(int size) {
        List<String> list = new java.util.ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add("Name" + i);
        }

        String result = handler.formListOutput(list);

        for (int i = 0; i < size; i++) {
            assertTrue(result.contains((i + 1) + ") Name" + i),
                    "Should contain numbering: " + (i + 1) + ") Name" + i);
        }
    }
}