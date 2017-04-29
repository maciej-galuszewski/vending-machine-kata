package tdd.vendingMachine;

import tdd.vendingMachine.model.Denomination;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VendingMachineMoneyStash {

    private final List<Denomination> storedMoney = new ArrayList<>();

    public void insertMoney(Denomination denomination) {
        storedMoney.add(denomination);
    }

    public BigDecimal getTotalAmount() {
        return Denomination.getTotalAmount(storedMoney);
    }

    public boolean isEmpty() {
        return storedMoney.isEmpty();
    }

    public List<Denomination> dropMoney() {
        List<Denomination> moneyToDrop = new ArrayList<>(storedMoney);
        storedMoney.clear();
        return moneyToDrop;
    }
}
