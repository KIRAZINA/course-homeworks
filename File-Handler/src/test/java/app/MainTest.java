package app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private static final String BASE_PATH = "files/";

    @Test
    void testMainExecution() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString().replace("\r\n", "\n").trim();

        assertTrue(output.contains("RESULT: Success."));
        assertTrue(output.contains("FILE CONTENT: My very important information."));

        // Verify file was actually created
        assertTrue(Files.exists(Paths.get(BASE_PATH + "myfile.txt")));
    }

    @Test
    void testMainOutputFormat() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.main(new String[]{});

        String output = outContent.toString().replace("\r\n", "\n").trim();

        // Output should contain two lines
        String[] lines = output.split("\n");
        assertEquals(2, lines.length);
        assertTrue(lines[0].startsWith("RESULT:"));
        assertTrue(lines[1].startsWith("FILE CONTENT:"));
    }
}
