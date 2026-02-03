package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataRepositoryTest {

    @Test
    void testGetData() {
        DataRepository repo = new DataRepository();
        int[] data = repo.getData();

        // Verify array length
        assertEquals(4, data.length);

        // Verify values
        assertArrayEquals(new int[]{7, 4, 5, 2}, data);
    }
}
