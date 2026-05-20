package app.model;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Product {

    // Getters
    @Setter
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be positive")
    private BigDecimal cost;

    public Product() {}

    public Product(String name, BigDecimal cost) {
        this.name = name;
        this.cost = cost;
    }

}