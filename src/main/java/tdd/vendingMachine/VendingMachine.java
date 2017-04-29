package tdd.vendingMachine;

import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.DisplayValue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class VendingMachine {

    private final VendingMachineMoneyStash storedMoneyStash = new VendingMachineMoneyStash();

    private final VendingMachineMoneyStash transactionMoneyStash = new VendingMachineMoneyStash();

    private final Map<Integer, VendingMachineShelve> shelves = new HashMap<>();

    private final VendingMachineOutput output = new VendingMachineOutput();

    private VendingMachineShelve selectedShelve = null;

    public void insertMoneyForTransaction(Denomination denomination) {
        transactionMoneyStash.insertMoney(denomination);
        if (selectedShelve == null) {
            output.dropMoney(transactionMoneyStash.dropMoney());
        } else {
            BigDecimal remainingAmount = selectedShelve.getProductPrice().subtract(transactionMoneyStash.getTotalAmount());
            if (BigDecimal.ZERO.compareTo(remainingAmount) == 0) {
                finalizeTransaction();
            } else {
                output.setDisplayMessage(DisplayValue.REMAINING_AMOUNT, remainingAmount);
            }
        }
    }

    public void selectShelve(int shelveNumber) {
        selectedShelve = shelves.get(shelveNumber);
        if (selectedShelve == null) {
            output.setDisplayMessage(DisplayValue.INVALID_SHELVE);
        } else if (selectedShelve.isEmpty()) {
            output.setDisplayMessage(DisplayValue.PRODUCT_NOT_AVAILABLE);
        } else if (transactionMoneyStash.isEmpty()) {
            output.setDisplayMessage(DisplayValue.PRODUCT_PRICE, selectedShelve.getProductPrice());
        } else {
            output.setDisplayMessage(DisplayValue.REMAINING_AMOUNT, selectedShelve.getProductPrice());
        }
    }

    public void pressCancelButton() {
        selectedShelve = null;
        output.dropMoney(transactionMoneyStash.dropMoney());
        output.setDisplayMessage(null);
    }

    public void addShelve(int shelveNumber, VendingMachineShelve shelve) {
        shelves.put(shelveNumber, shelve);
    }

    public VendingMachineOutput getOutput() {
        return output;
    }

    private void finalizeTransaction() {
        output.setDisplayMessage(null);
        output.dropProduct(selectedShelve.dropProduct());
        transactionMoneyStash.dropMoney().forEach(storedMoneyStash::insertMoney);
        selectedShelve = null;
    }
}
