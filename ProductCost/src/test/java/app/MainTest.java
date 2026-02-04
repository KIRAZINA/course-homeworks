package app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testMainOutput() {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString().trim();

        // Expected output with line breaks
        String normalized = output.replace("\r\n", "\n").trim();
        String expected = """
    Product: abc, quota is 5 pcs., price is EUR 2.5.
    Cost is EUR 12.5.
    Product: abc, quota is 5 pcs., price is EUR 2.5.
    Cost is EUR 19.5.
    """.trim();

        assertEquals(expected, normalized);

    }

    @Test
    void testGetData() {
        // getData should return correct array
        String[] data = Main.getData();
        assertArrayEquals(new String[]{"abc", "5", "2.5"}, data);
    }
}
