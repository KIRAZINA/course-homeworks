package app.model.dto;

import app.model.Order;
import app.model.Product;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {

    private final Long id;
    private final LocalDateTime creationDate;
    private final BigDecimal totalCost;
    private final List<Product> products;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.creationDate = order.getCreationDate();
        this.totalCost = order.getTotalCost();
        this.products = order.getProducts();
    }

}