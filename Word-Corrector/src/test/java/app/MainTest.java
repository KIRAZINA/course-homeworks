package app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testMainOutput() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Main.main(new String[]{});

        String output = out.toString();

        String[] expectedLines = {
                "1) orange",
                "2) plum",
                "3) tomato",
                "4) onion",
                "5) grape"
        };
        String[] outputLines = output.strip().split("\\R");
        assertArrayEquals(expectedLines, outputLines);
    }
}
