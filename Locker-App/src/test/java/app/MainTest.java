package app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testMainOutput() {
        // Capture console output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Main.main(new String[]{});

        String output = out.toString().trim();

        String expected = String.join(System.lineSeparator(),
                "Initial value is 7",
                "New value is 21",
                "Initial value is 4",
                "New value is 12",
                "Initial value is 5",
                "New value is 15",
                "Initial value is 2",
                "New value is 6"
        );

        assertEquals(expected, output);
    }
}
