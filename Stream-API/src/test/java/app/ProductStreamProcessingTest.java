package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ProductStreamProcessingTest {

    private List<Product> products;

    @BeforeEach
    void setUp() {
        products = Arrays.asList(
                new Product("Laptop", "Electronics", 1200.0),
                new Product("Coffee Maker", "Appliances", 80.0),
                new Product("Headphones", "Electronics", 150.0),
                new Product("Blender", "Appliances", 50.0),
                new Product("Smartphone", "Electronics", 900.0),
                new Product("Microwave", "Appliances", 200.0)
        );
    }

    @Test
    @DisplayName("Products are correctly grouped by category")
    void shouldGroupProductsByCategory() {
        Map<String, List<Product>> grouped = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory));

        assertEquals(2, grouped.size());
        assertTrue(grouped.containsKey("Electronics"));
        assertTrue(grouped.containsKey("Appliances"));

        assertEquals(3, grouped.get("Electronics").size());
        assertEquals(3, grouped.get("Appliances").size());

        // Check specific product exists in group
        assertTrue(grouped.get("Electronics").stream()
                .anyMatch(p -> "Laptop".equals(p.getName()) && p.getPrice() == 1200.0));
    }

    @Test
    @DisplayName("Average price per category is calculated correctly")
    void shouldCalculateAveragePricePerCategory() {
        Map<String, Double> averages = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.averagingDouble(Product::getPrice)
                ));

        assertEquals(2, averages.size());

        assertEquals(750.0, averages.get("Electronics"), 0.001);
        assertEquals(110.0, averages.get("Appliances"), 0.001);
    }

    @Test
    @DisplayName("Finds category with highest average price")
    void shouldFindCategoryWithHighestAveragePrice() {
        Map<String, Double> averages = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.averagingDouble(Product::getPrice)
                ));

        Optional<Map.Entry<String, Double>> maxEntry = averages.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        assertTrue(maxEntry.isPresent());
        assertEquals("Electronics", maxEntry.get().getKey());
        assertEquals(750.0, maxEntry.get().getValue(), 0.001);
    }

    @Test
    @DisplayName("Handles empty list correctly - no max category")
    void emptyList_shouldHaveNoMaxCategory() {
        List<Product> emptyList = Collections.emptyList();

        Map<String, Double> averages = emptyList.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.averagingDouble(Product::getPrice)
                ));

        Optional<Map.Entry<String, Double>> maxEntry = averages.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        assertFalse(maxEntry.isPresent());
    }

    @Test
    @DisplayName("Single category is correctly identified as highest")
    void singleCategory_isDetectedAsHighest() {
        List<Product> singleCategoryList = Arrays.asList(
                new Product("Book A", "Books", 30.0),
                new Product("Book B", "Books", 50.0)
        );

        Map<String, Double> averages = singleCategoryList.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.averagingDouble(Product::getPrice)
                ));

        Optional<Map.Entry<String, Double>> maxEntry = averages.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        assertTrue(maxEntry.isPresent());
        assertEquals("Books", maxEntry.get().getKey());
        assertEquals(40.0, maxEntry.get().getValue(), 0.001);
    }

    @Test
    @DisplayName("Equal average prices - any of max categories is acceptable")
    void equalAverages_anyMaxCategoryIsValid() {
        List<Product> equalAvgList = Arrays.asList(
                new Product("Item1", "A", 100.0),
                new Product("Item2", "A", 200.0),
                new Product("Item3", "B", 150.0)
        );

        Map<String, Double> averages = equalAvgList.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.averagingDouble(Product::getPrice)
                ));

        // A: 150.0, B: 150.0 → either is fine
        Optional<Map.Entry<String, Double>> maxEntry = averages.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        assertTrue(maxEntry.isPresent());
        assertEquals(150.0, maxEntry.get().getValue(), 0.001);
        assertTrue("A".equals(maxEntry.get().getKey()) || "B".equals(maxEntry.get().getKey()));
    }
}