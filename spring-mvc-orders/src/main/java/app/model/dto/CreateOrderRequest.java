package app.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import app.model.Product;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateOrderRequest {

    @NotEmpty(message = "Order must contain at least one product")
    @Valid
    private List<Product> products;

    public CreateOrderRequest() {}

}