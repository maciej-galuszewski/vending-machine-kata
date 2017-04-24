package tdd.vendingMachine.model;

import java.math.BigDecimal;

public enum Denomination {

    FIVE("5"), TWO("2"), ONE("1"), HALF("0.5"), TWO_TENTHS("0.2"), TENTH("0.1");

    private BigDecimal amount;

    Denomination(String amount) {
        this.amount = new BigDecimal(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
