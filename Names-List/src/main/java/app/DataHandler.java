package app;

import java.util.List;

public class DataHandler {

    // Method forms output for a name at a specific index
    public String formOutput(List<String> list, int index) {
        try {
            String name = list.get(index);
            return "Name: " + name + " is in index " + index;
        } catch (IndexOutOfBoundsException e) {
            return "Wrong index!";
        }
    }

    // Method forms output for a numbered list of names
    public String formListOutput(List<String> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nNames:\n");
        for (int i = 0; i < list.size(); i++) {
            sb.append(i + 1)
                    .append(") ")
                    .append(list.get(i))
                    .append("\n");
        }
        return sb.toString();
    }
}
