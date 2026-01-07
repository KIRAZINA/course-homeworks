package app;

import java.math.BigDecimal;

// Modern product model using record
public record Product(String name, int quota, BigDecimal price) {

    @Override
    public String toString() {
        return "Product: " + name +
                ", quota is " + quota + " " +
                Constants.MEASURE +
                ", price is " + Constants.CURRENCY +
                " " + price + ".";
    }
}

