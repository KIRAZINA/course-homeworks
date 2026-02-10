package app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for DataRepository class.
 */
class DataRepositoryTest {

    @Test
    @DisplayName("getData should return map with exactly four predefined entries")
    void getData_shouldReturnMapWithFourUniqueEntries() {
        DataRepository repository = new DataRepository();
        Map<Integer, String> data = repository.getData();

        assertThat(data)
                .isNotNull()
                .hasSize(4)
                .containsEntry(387, "Lucy")
                .containsEntry(231, "Alice")
                .containsEntry(394, "Bob")
                .containsEntry(172, "Tom");

        // Ensure keys and values are unique
        assertThat(data.keySet()).doesNotHaveDuplicates();
        assertThat(data.values()).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("getData should not return null or empty map")
    void getData_shouldNotBeNullOrEmpty() {
        DataRepository repository = new DataRepository();
        assertThat(repository.getData()).isNotNull().isNotEmpty();
    }
}