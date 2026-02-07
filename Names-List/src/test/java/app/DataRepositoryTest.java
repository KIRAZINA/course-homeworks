package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DataRepository class.
 */
class DataRepositoryTest {

    private DataRepository repository;

    @BeforeEach
    void setUp() {
        repository = new DataRepository();
    }

    @Test
    @DisplayName("getData should return a list with exactly 5 names")
    void getData_ReturnsCorrectSize() {
        List<String> names = repository.getData();
        assertEquals(5, names.size(), "List should contain exactly 5 names");
    }

    @Test
    @DisplayName("getData should return the correct names in the correct order")
    void getData_ReturnsCorrectNamesAndOrder() {
        List<String> expected = List.of("Alice", "Bob", "Lucy", "Denis", "Tom");
        List<String> actual = repository.getData();

        assertEquals(expected, actual, "Names list does not match expected content");
    }

    @Test
    @DisplayName("getData should return a new ArrayList instance each time (not cached)")
    void getData_ReturnsNewInstanceEachCall() {
        List<String> list1 = repository.getData();
        List<String> list2 = repository.getData();

        assertNotSame(list1, list2, "Each call should return a new list instance");
        assertEquals(list1, list2, "But the content should be the same");
    }

    @Test
    @DisplayName("Returned list should be modifiable (as per ArrayList contract)")
    void returnedList_IsModifiable() {
        List<String> names = repository.getData();
        int originalSize = names.size();

        names.add("Extra");
        assertEquals(originalSize + 1, names.size(), "Should be able to modify the returned list");
    }
}