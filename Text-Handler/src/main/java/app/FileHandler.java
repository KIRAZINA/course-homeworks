package app;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {

    /**
     * Creates a new file at the specified path.
     * Ensures the parent directory exists before file creation.
     * @param path the path where the file should be created
     * @return status message about file creation
     */
    public String createFile(String path) {
        Path newFile = Paths.get(path);
        try {
            // Ensure the parent directory exists
            Files.createDirectories(newFile.getParent());
            Files.createFile(newFile);
        } catch (FileAlreadyExistsException e) {
            return "File already exists!";
        } catch (IOException e) {
            return "Something went wrong: " + e.getMessage();
        }
        return "Created " + path;
    }

    /**
     * Writes the given content to the specified file.
     * Overwrites existing content by default.
     * @param path the path to the file
     * @param content the content to write
     * @return status message about writing
     */
    public String writeToFile(Path path, String content) {
        try {
            Files.writeString(path, content); // Overwrites by default
        } catch (IOException e) {
            return "Something went wrong: " + e.getMessage();
        }
        return "Recorded in " + path.toString();
    }

    /**
     * Reads and returns the content of the specified file.
     * @param path the path to the file
     * @return the content of the file or error message
     */
    public String readFromFile(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (IOException e) {
            return "Something went wrong: " + e.getMessage();
        }
    }
}
