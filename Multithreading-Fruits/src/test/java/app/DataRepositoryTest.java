package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataRepositoryTest {

    @Test
    void testGetDataNotNull() {
        // Data should not be null
        DataRepository repo = new DataRepository();
        assertNotNull(repo.getData());
    }

    @Test
    void testGetDataLength() {
        // Data should contain exactly 4 fruits
        DataRepository repo = new DataRepository();
        String[] fruits = repo.getData();
        assertEquals(4, fruits.length);
    }

    @Test
    void testGetDataContent() {
        // Data should contain expected fruits
        DataRepository repo = new DataRepository();
        String[] fruits = repo.getData();
        assertArrayEquals(new String[]{"orange", "apple", "plum", "mango"}, fruits);
    }

    @Test
    void testGetDataImmutability() {
        // Changing returned array should not affect new calls
        DataRepository repo = new DataRepository();
        String[] fruits = repo.getData();
        fruits[0] = "changed";

        String[] newFruits = repo.getData();
        assertEquals("orange", newFruits[0]);
    }
}
