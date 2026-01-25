package app;

// Main class to execute file operations
public class Main {

    private final static String BASE_PATH = "files/";

    public static void main(String[] args) {
        // Create an instance of FileHandler
        FileHandler handler = new FileHandler();

        // Define file name and content
        String fileName = "myfile";
        String fileContent = "My very important information.";

        // Write content to the file
        String result = handler.writeFile(fileName, fileContent);

        // Read content from the file
        String content = handler.readFile(BASE_PATH + fileName + ".txt");

        // Display results
        getOutput("RESULT: " + result);
        getOutput("FILE CONTENT: " + content);
    }

    // Helper method to print output
    private static void getOutput(String output) {
        System.out.println(output);
    }
}
