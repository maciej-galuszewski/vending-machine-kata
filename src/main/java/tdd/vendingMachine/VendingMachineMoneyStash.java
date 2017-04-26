package tdd.vendingMachine;

import tdd.vendingMachine.model.Denomination;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendingMachineMoneyStash {

    private final List<Denomination> money = new ArrayList<>();

    public void insertMoney(Denomination denomination) {
        money.add(denomination);
    }

    public BigDecimal getTotalAmount() {
        return money.stream().map(Denomination::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return money.isEmpty();
    }
}
