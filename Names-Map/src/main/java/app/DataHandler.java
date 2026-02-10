package app;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DataHandler {

    // Repository instance to access user data (initialized once)
    private final Map<Integer, String> map = new DataRepository().getData();

    // Method generates numbered list of all names
    public String getAll() {
        StringBuilder sb = new StringBuilder();
        AtomicInteger count = new AtomicInteger(0);

        map.forEach((id, name) ->
                sb.append(String.format("%d) %d, %s%n",
                        count.incrementAndGet(), id, name))
        );

        // Cleaner: no leading \n, no need for .toString()
        return "ALL NAMES:\n" + sb;
    }

    // Method returns name by specific ID
    public String getById(int id) {
        String name = map.get(id);
        return name != null
                ? String.format("NAME: id %d, %s", id, name)
                : "No data!";
    }
}
