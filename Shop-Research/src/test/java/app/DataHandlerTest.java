package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class DataHandlerTest {

    @Test
    void testHandleDataWithNullInput() {
        // Null input should return "(no data)"
        DataHandler handler = new DataHandler();
        assertEquals("(no data)", handler.handleData(null));
    }

    @Test
    void testHandleDataWithEmptyArray() {
        // Empty array should return "(no data)"
        DataHandler handler = new DataHandler();
        assertEquals("(no data)", handler.handleData(new String[]{}));
    }

    @Test
    void testHandleDataWithStrings() {
        // Array of strings should be formatted correctly
        DataHandler handler = new DataHandler();
        String[] items = {"apple", "banana"};
        String result = handler.handleData(items);
        assertEquals("(1) apple (2) banana ", result);
    }

    @Test
    void testHandleDataWithNumbers() {
        // Array of numbers should be formatted correctly
        DataHandler handler = new DataHandler();
        Integer[] numbers = {10, 20, 30};
        String result = handler.handleData(numbers);
        assertEquals("(1) 10 (2) 20 (3) 30 ", result);
    }
}
