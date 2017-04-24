package tdd.vendingMachine;

import tdd.vendingMachine.model.Denomination;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendingMachine {

    private final List<Denomination> denominations = new ArrayList<>();

    public void insertMoney(Denomination denomination) {
        denominations.add(denomination);
    }

    public BigDecimal getTotalAmount() {
        return denominations.stream().map(Denomination::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
