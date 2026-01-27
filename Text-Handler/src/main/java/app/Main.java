package app;

import java.nio.file.Paths;

public class Main {

    // Base directory where files will be created and managed
    private static final String BASE_PATH = "files/";

    public static void main(String[] args) {
        // Create an instance of FileHandler to manage file operations
        FileHandler handler = new FileHandler();

        // Define the file name and the content to be written
        String newFileName = "myfile";
        String content = "Super information.";

        // Construct the full path for the file
        String path = BASE_PATH + newFileName + ".txt";

        // Perform file operations and print their results
        getOutput(handler.createFile(path)); // Create the file
        getOutput(handler.writeToFile(Paths.get(path), content)); // Write content to the file
        getOutput("CONTENT: " + handler.readFromFile(path)); // Read and display file content
    }

    /**
     * Utility method to print output to the console.
     * @param output the message to be printed
     */
    private static void getOutput(String output) {
        System.out.println(output);
    }
}
