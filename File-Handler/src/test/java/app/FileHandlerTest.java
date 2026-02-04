package app;

import org.junit.jupiter.api.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    private static final String BASE_PATH = "files/";
    private FileHandler handler;

    @BeforeEach
    void setUp() {
        handler = new FileHandler();
        new File(BASE_PATH).mkdirs(); // ensure directory exists
    }

    @Test
    void testWriteFileSuccess() throws Exception {
        String fileName = "testfile";
        String content = "Hello, world!";
        String result = handler.writeFile(fileName, content);

        assertEquals("Success.", result);
        assertTrue(Files.exists(Paths.get(BASE_PATH + fileName + ".txt")));

        String fileContent = Files.readString(Paths.get(BASE_PATH + fileName + ".txt"));
        assertEquals(content, fileContent);
    }

    @Test
    void testReadFileSuccess() throws Exception {
        String fileName = "readfile";
        String content = "Read me!";
        Files.writeString(Paths.get(BASE_PATH + fileName + ".txt"), content);

        String result = handler.readFile(BASE_PATH + fileName + ".txt");
        assertEquals(content, result);
    }

    @Test
    void testReadFileFailure() {
        String result = handler.readFile(BASE_PATH + "nonexistent.txt");
        assertTrue(result.startsWith("Failed to read file:"));
    }

    @Test
    void testWriteFileFailure() {
        // Simulate failure by writing to invalid path
        FileHandler badHandler = new FileHandler() {
            @Override
            public String writeFile(String fileName, String fileContent) {
                try (java.io.FileWriter fw = new java.io.FileWriter("/invalidpath/" + fileName + ".txt")) {
                    fw.write(fileContent);
                    return "Success.";
                } catch (Exception e) {
                    return "Failed to write file: " + e.getMessage();
                }
            }
        };

        String result = badHandler.writeFile("badfile", "content");
        assertTrue(result.startsWith("Failed to write file:"));
    }

    @Test
    void testWriteEmptyContent() throws Exception {
        String fileName = "emptyfile";
        String result = handler.writeFile(fileName, "");
        assertEquals("Success.", result);

        String fileContent = Files.readString(Paths.get(BASE_PATH + fileName + ".txt"));
        assertEquals("", fileContent);
    }

    @Test
    void testReadEmptyFile() throws Exception {
        String fileName = "emptyread";
        Files.writeString(Paths.get(BASE_PATH + fileName + ".txt"), "");

        String result = handler.readFile(BASE_PATH + fileName + ".txt");
        assertEquals("", result);
    }

    @Test
    void testWriteLargeContent() throws Exception {
        String fileName = "largefile";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("Line ").append(i).append("\n");
        }
        String content = sb.toString();

        String result = handler.writeFile(fileName, content);
        assertEquals("Success.", result);

        String fileContent = Files.readString(Paths.get(BASE_PATH + fileName + ".txt"));
        assertEquals(content, fileContent);
    }
}
