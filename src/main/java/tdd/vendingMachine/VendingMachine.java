package tdd.vendingMachine;

import tdd.vendingMachine.changestrategy.ChangeStrategy;
import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.DisplayValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendingMachine {

    private final VendingMachineMoneyStash storedMoneyStash = new VendingMachineMoneyStash();

    private final VendingMachineMoneyStash transactionMoneyStash = new VendingMachineMoneyStash();

    private final Map<Integer, VendingMachineShelve> shelves = new HashMap<>();

    private final VendingMachineOutput output = new VendingMachineOutput();

    private final ChangeStrategy changeStrategy;

    private VendingMachineShelve selectedShelve = null;

    public VendingMachine(ChangeStrategy changeStrategy) {
        this.changeStrategy = changeStrategy;
    }

    public void insertMoneyForTransaction(Denomination denomination) {
        transactionMoneyStash.insertMoney(denomination);
        if (selectedShelve == null) {
            output.dropMoney(transactionMoneyStash.dropAllStoredMoney());
        } else {
            BigDecimal remainingAmount = selectedShelve.getProductPrice().subtract(transactionMoneyStash.getTotalAmount());
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                finalizeTransaction(remainingAmount);
            } else {
                output.setDisplayMessage(DisplayValue.REMAINING_AMOUNT, remainingAmount);
            }
        }
    }

    public void insertStoredMoney(Denomination denomination) {
        storedMoneyStash.insertMoney(denomination);
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
        output.dropMoney(transactionMoneyStash.dropAllStoredMoney());
        output.setDisplayMessage(null);
    }

    public void addShelve(int shelveNumber, VendingMachineShelve shelve) {
        shelves.put(shelveNumber, shelve);
    }

    public VendingMachineOutput getOutput() {
        return output;
    }

    private void finalizeTransaction(BigDecimal remainingAmount) {
        boolean transactionSuccessful = false;
        if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            List<Denomination> availableDenominations = new ArrayList<>(storedMoneyStash.getStoredMoney());
            availableDenominations.addAll(transactionMoneyStash.getStoredMoney());
            List<Denomination> change = changeStrategy.calculateChange(availableDenominations, remainingAmount.negate());
            if (change != null) {
                output.dropMoney(storedMoneyStash.dropMoney(change));
                transactionSuccessful = true;
            } else {
                output.setDisplayMessage(DisplayValue.CHANGE_NOT_AVAILABLE);
                output.dropMoney(transactionMoneyStash.dropAllStoredMoney());
            }
        } else {
            transactionSuccessful = true;
        }
        if (transactionSuccessful) {
            output.setDisplayMessage(null);
            output.dropProduct(selectedShelve.dropProduct());
            transactionMoneyStash.dropAllStoredMoney().forEach(storedMoneyStash::insertMoney);
        }
        selectedShelve = null;
    }
}
