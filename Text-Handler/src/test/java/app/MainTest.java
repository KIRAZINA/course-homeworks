package app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testMainOutput() throws Exception {
        // Capture console output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Main.main(new String[]{});

        String output = out.toString();

        // Check that output contains either "Created" or "File already exists!"
        assertTrue(output.contains("Created") || output.contains("File already exists!"));

        // Check that writing and reading messages are present
        assertTrue(output.contains("Recorded in"));
        assertTrue(output.contains("CONTENT: Super information."));

        // Verify that the file was actually created and contains the expected content
        Path filePath = Path.of("files/myfile.txt");
        assertTrue(Files.exists(filePath));
        assertEquals("Super information.", Files.readString(filePath));
    }

}
