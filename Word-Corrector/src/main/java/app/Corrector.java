package app;

import java.util.Map;

// This class is responsible for fixing words
// Corrects misspelled words using a provided dictionary
public class Corrector {

    private final Map<String, String> dictionary;

    // Initializes the corrector with a map of corrections
    public Corrector(Map<String, String> dictionary) {
        this.dictionary = dictionary;
    }

    // Goes through each word, checks if it needs fixing, and builds a nice numbered list as output
    public String handleData(String[] strs) {
        StringBuilder stringBuilder = new StringBuilder();
        int count = 1;
        for (String str : strs) {
            if (dictionary.containsKey(str)) {
                str = dictionary.get(str);
            }
            stringBuilder.append(count).append(") ").append(str).append("\n");
            count++;
        }
        return stringBuilder.toString();
    }
}
