package tdd.vendingMachine;

import tdd.vendingMachine.model.Denomination;

import java.math.BigDecimal;

public class VendingMachine {

    private BigDecimal totalAmount = BigDecimal.ZERO;

    public void insertMoney(Denomination denomination) {
        totalAmount = totalAmount.add(denomination.getAmount());
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
