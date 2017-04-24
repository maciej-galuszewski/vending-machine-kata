package tdd.vendingMachine.model;

import java.math.BigDecimal;

public enum ProductType {

    COLA_DRINK("Cola drink 0.25l", "1.5"),
    CHOCOLATE_BAR("Chocolate bar", "2.0"),
    MINERAL_WATER("Mineral water 0.33l", "1.8");

    private String name;

    private BigDecimal price;

    ProductType(String name, String price) {
        this.name = name;
        this.price = new BigDecimal(price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
