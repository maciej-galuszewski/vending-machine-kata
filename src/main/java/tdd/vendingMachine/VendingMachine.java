package tdd.vendingMachine;

import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.DisplayValue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class VendingMachine {

    private final VendingMachineMoneyStash transactionMoneyStash = new VendingMachineMoneyStash();

    private final VendingMachineDisplay display = new VendingMachineDisplay();

    private final Map<Integer, VendingMachineShelve> shelves = new HashMap<>();

    private VendingMachineShelve selectedShelve = null;

    public void insertMoneyForTransaction(Denomination denomination) {
        transactionMoneyStash.insertMoney(denomination);
        BigDecimal remainingAmount = selectedShelve.getProductPrice().subtract(transactionMoneyStash.getTotalAmount());
        display.setDisplayValue(DisplayValue.REMAINING_AMOUNT, remainingAmount);
    }

    public void selectShelve(int shelveNumber) {
        selectedShelve = shelves.get(shelveNumber);
        if (selectedShelve == null) {
            display.setDisplayValue(DisplayValue.INVALID_SHELVE);
        } else if (transactionMoneyStash.isEmpty()) {
            display.setDisplayValue(DisplayValue.PRODUCT_PRICE, selectedShelve.getProductPrice());
        } else {
            display.setDisplayValue(DisplayValue.REMAINING_AMOUNT, selectedShelve.getProductPrice());
        }
    }

    public void addShelve(int shelveNumber, VendingMachineShelve shelve) {
        shelves.put(shelveNumber, shelve);
    }

    public String getMessageFromDisplay() {
        return display.getDisplayValue();
    }
}
