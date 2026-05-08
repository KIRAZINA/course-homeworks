package app.orderapi.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a customer order containing multiple products.
 */
public class Order {
    private Long id;
    private LocalDate date;
    private double cost;
    private List<Product> products;

    public Order() {}

    public Order(Long id, LocalDate date, double cost, List<Product> products) {
        this.id = id;
        this.date = date;
        this.cost = cost;
        this.products = products;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}