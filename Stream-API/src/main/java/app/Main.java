package app;

// Main.java
// Demonstrates grouping products by category and calculating average prices using Stream API
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Create a list of products
        List<Product> products = Arrays.asList(
                new Product("Laptop", "Electronics", 1200.0),
                new Product("Coffee Maker", "Appliances", 80.0),
                new Product("Headphones", "Electronics", 150.0),
                new Product("Blender", "Appliances", 50.0),
                new Product("Smartphone", "Electronics", 900.0),
                new Product("Microwave", "Appliances", 200.0)
        );

        // 1. Group products by category
        Map<String, List<Product>> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory));

        System.out.println("Products grouped by category:");
        productsByCategory.forEach((category, productList) -> {
            System.out.println(category + ":");
            productList.forEach(product ->
                    System.out.println("  - " + product.getName() + " ($" + product.getPrice() + ")")
            );
        });

        System.out.println(); // Empty line for readability

        // 2. Calculate average price per category
        Map<String, Double> averagePricesByCategory = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.averagingDouble(Product::getPrice)
                ));

        System.out.println("Average prices by category:");
        averagePricesByCategory.forEach((category, avgPrice) -> {
            System.out.printf("%s: $%.2f%n", category, avgPrice);
        });

        System.out.println(); // Empty line for readability

        // 3. Find category with the highest average price
        Map.Entry<String, Double> maxCategoryEntry = averagePricesByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (maxCategoryEntry != null) {
            System.out.printf("Category with the highest average price: %s ($%.2f)%n",
                    maxCategoryEntry.getKey(), maxCategoryEntry.getValue());
        } else {
            System.out.println("No category found.");
        }
    }
}
