package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataProviderTest {

    @Test
    void testGetData() {
        DataProvider provider = new DataProvider();
        String[] data = provider.getData();

        assertArrayEquals(new String[]{"brange", "plum", "tomato", "onibn", "grape"}, data);
    }
}
