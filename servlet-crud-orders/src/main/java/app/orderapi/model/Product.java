package app.orderapi.model;

/**
 * Represents a product with basic pricing information.
 */
public class Product {
    private Long id;
    private String name;
    private double cost;

    public Product() {}

    public Product(Long id, String name, double cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
}