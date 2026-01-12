package app;

import java.util.HashMap;
import java.util.Map;

// Application entry point: configures corrections and runs the word corrector
public class Main {

    public static void main(String[] args) {
        // Dictionary of corrections: wrong word -> correct word
        Map<String, String> dictionary = new HashMap<>();
        dictionary.put("brange", "orange");
        dictionary.put("onibn", "onion");

        Corrector corrector = new Corrector(dictionary);

        // Get the data (words) and let Corrector fix them
        String output = corrector.handleData(new DataProvider().getData());
        getOutput(output);
    }

    private static void getOutput(String output) {
        System.out.println(output);
    }
}
