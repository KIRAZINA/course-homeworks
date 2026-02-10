package app;

import java.util.HashMap;
import java.util.Map;

public class DataRepository {

    // Method returns a map of user IDs and names
    public Map<Integer, String> getData() {
        Map<Integer, String> map = new HashMap<>();

        // Adding predefined users
        map.put(387, "Lucy");
        map.put(231, "Alice");
        map.put(394, "Bob");
        map.put(172, "Tom");

        return map;
    }
}
