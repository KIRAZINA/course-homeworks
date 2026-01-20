package app;

// Handles input data of different types
public class DataHandler {

    /**
     * Generic non-static method that takes an array of generic type
     * and returns a formatted String representation of the array.
     * Works correctly only for types with meaningful toString().
     */
    public <T> String handleData(T[] items) {
        // Defensive check for null input
        if (items == null || items.length == 0) {
            return "(no data)";
        }

        StringBuilder sb = new StringBuilder();
        int count = 0;

        // Iterating through the generic array
        for (T item : items) {
            count++;
            sb.append("(").append(count).append(") ").append(item).append(" ");
        }

        return sb.toString();
    }
}
