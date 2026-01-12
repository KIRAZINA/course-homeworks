package app;

// This class just gives us the starting words
// Some of them are intentionally misspelled so we can test our correction logic
public class DataProvider {

    // Provides test data — array of words, some of which are misspelled
    public String[] getData() {
        return new String[]{"brange", "plum", "tomato", "onibn", "grape"};
    }
}
