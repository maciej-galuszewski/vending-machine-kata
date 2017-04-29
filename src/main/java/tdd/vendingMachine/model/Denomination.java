package tdd.vendingMachine.model;

import java.math.BigDecimal;
import java.util.List;

public enum Denomination {

    FIVE("5"), TWO("2"), ONE("1"), HALF("0.5"), TWO_TENTHS("0.2"), TENTH("0.1");

    private BigDecimal amount;

    Denomination(String amount) {
        this.amount = new BigDecimal(amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static BigDecimal getTotalAmount(List<Denomination> denominationList) {
        return denominationList.stream().map(Denomination::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
