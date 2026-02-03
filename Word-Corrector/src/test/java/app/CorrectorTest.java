package app;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CorrectorTest {

    @Test
    void testHandleDataWithCorrections() {
        Map<String, String> dictionary = new HashMap<>();
        dictionary.put("brange", "orange");
        dictionary.put("onibn", "onion");

        Corrector corrector = new Corrector(dictionary);

        String[] input = {"brange", "plum", "onibn"};
        String output = corrector.handleData(input);

        String expected = "1) orange" + System.lineSeparator() +
                "2) plum"   + System.lineSeparator() +
                "3) onion"  + System.lineSeparator();

        String[] expectedLines = expected.split("\\R");
        String[] outputLines   = output.split("\\R");
        assertArrayEquals(expectedLines, outputLines);


    }

    @Test
    void testHandleDataWithoutCorrections() {
        Map<String, String> dictionary = new HashMap<>();
        Corrector corrector = new Corrector(dictionary);

        String[] input = {"apple", "banana"};
        String output = corrector.handleData(input);

        String expected = "1) apple"  + System.lineSeparator() +
                "2) banana" + System.lineSeparator();

        String[] expectedLines = expected.split("\\R");
        String[] outputLines   = output.split("\\R");
        assertArrayEquals(expectedLines, outputLines);


    }

}
