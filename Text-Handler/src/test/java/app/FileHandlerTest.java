package app;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    private FileHandler handler;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        handler = new FileHandler();
        // Create a temporary directory for testing
        tempDir = Files.createTempDirectory("testDir");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up temporary directory after each test
        Files.walk(tempDir)
                .sorted((a, b) -> b.compareTo(a)) // delete files before directories
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (IOException ignored) {}
                });
    }

    @Test
    void testCreateFileSuccess() {
        String filePath = tempDir.resolve("newfile.txt").toString();
        String result = handler.createFile(filePath);
        assertEquals("Created " + filePath, result);
        assertTrue(Files.exists(Paths.get(filePath)));
    }

    @Test
    void testCreateFileAlreadyExists() {
        String filePath = tempDir.resolve("exists.txt").toString();
        handler.createFile(filePath); // first creation
        String result = handler.createFile(filePath); // second attempt
        assertEquals("File already exists!", result);
    }

    @Test
    void testWriteToFileAndRead() {
        Path filePath = tempDir.resolve("write.txt");
        handler.createFile(filePath.toString());

        String content = "Hello world!";
        String writeResult = handler.writeToFile(filePath, content);
        assertEquals("Recorded in " + filePath.toString(), writeResult);

        String readResult = handler.readFromFile(filePath.toString());
        assertEquals(content, readResult);
    }

    @Test
    void testReadFromNonExistingFile() {
        String result = handler.readFromFile(tempDir.resolve("nofile.txt").toString());
        assertTrue(result.startsWith("Something went wrong:"));
    }

    @Test
    void testWriteToNonExistingFile() {
        Path filePath = tempDir.resolve("nofile.txt");
        // Files.writeString creates the file if it does not exist
        String result = handler.writeToFile(filePath, "data");
        assertEquals("Recorded in " + filePath.toString(), result);
        assertTrue(Files.exists(filePath));
    }
}
