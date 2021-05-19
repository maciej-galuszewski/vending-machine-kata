package tdd.vendingMachine.model;

import java.math.BigDecimal;

public enum ProductType {

    COLA_DRINK("1.5"),
    CHOCOLATE_BAR("2.0"),
    MINERAL_WATER("1.8");

    private final BigDecimal price;

    ProductType(String price) {
        this.price = new BigDecimal(price);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
