package tdd.vendingMachine;

import tdd.vendingMachine.changestrategy.ChangeStrategy;
import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.DisplayMessage;

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
            return;
        }

        BigDecimal remainingAmount = selectedShelve.getProductPrice().subtract(transactionMoneyStash.getTotalAmount());
        parseTransactionStatus(remainingAmount);
    }

    public void insertStoredMoney(Denomination denomination) {
        storedMoneyStash.insertMoney(denomination);
    }

    public void selectShelve(int shelveNumber) {
        selectedShelve = shelves.get(shelveNumber);
        if (selectedShelve == null) {
            output.setDisplayMessage(DisplayMessage.INVALID_SHELVE);
        } else if (selectedShelve.isEmpty()) {
            output.setDisplayMessage(DisplayMessage.PRODUCT_NOT_AVAILABLE);
        } else if (transactionMoneyStash.isEmpty()) {
            output.setDisplayMessage(DisplayMessage.PRODUCT_PRICE, selectedShelve.getProductPrice());
        } else {
            output.setDisplayMessage(DisplayMessage.REMAINING_AMOUNT, selectedShelve.getProductPrice());
        }
    }

    public void pressCancelButton() {
        cancelTransaction(null);
    }

    private void cancelTransaction(DisplayMessage displayMessage) {
        selectedShelve = null;
        output.dropMoney(transactionMoneyStash.dropAllStoredMoney());
        output.setDisplayMessage(displayMessage);
    }

    public void addShelve(int shelveNumber, VendingMachineShelve shelve) {
        shelves.put(shelveNumber, shelve);
    }

    public VendingMachineOutput getOutput() {
        return output;
    }

    private void parseTransactionStatus(BigDecimal remainingAmount) {
        int comparisonResult = remainingAmount.compareTo(BigDecimal.ZERO);
        if (comparisonResult > 0) {
            output.setDisplayMessage(DisplayMessage.REMAINING_AMOUNT, remainingAmount);
            return;
        }

        List<Denomination> change = null;
        if (comparisonResult < 0) {
            change = calculateChange(remainingAmount.negate());
            if (change == null) {
                cancelTransaction(DisplayMessage.CHANGE_NOT_AVAILABLE);
                return;
            }
        }

        transactionMoneyStash.dropAllStoredMoney().forEach(storedMoneyStash::insertMoney);
        output.setDisplayMessage(null);
        output.dropProduct(selectedShelve.dropProduct());
        if (comparisonResult < 0) {
            output.dropMoney(storedMoneyStash.dropMoney(change));
        }
        selectedShelve = null;
    }

    private List<Denomination> calculateChange(BigDecimal changeToReturn) {
        List<Denomination> availableDenominations = new ArrayList<>(storedMoneyStash.getStoredMoney());
        availableDenominations.addAll(transactionMoneyStash.getStoredMoney());
        return changeStrategy.calculateChange(availableDenominations, changeToReturn);
    }
}
