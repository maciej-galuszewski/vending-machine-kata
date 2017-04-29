package tdd.vendingMachine;

import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.DisplayValue;

import java.util.ArrayList;
import java.util.List;

public class VendingMachineOutput {

    private final List<Denomination> droppedMoney = new ArrayList<>();

    private String displayMessage = null;

    public void setDisplayMessage(DisplayValue displayMessage, Object... args) {
        if (displayMessage == null) {
            this.displayMessage = null;
        } else {
            this.displayMessage = String.format(displayMessage.getValue(), args);
        }
    }

    public List<Denomination> getDroppedMoney() {
        return droppedMoney;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void dropMoney(List<Denomination> moneyToDrop) {
        droppedMoney.addAll(moneyToDrop);
    }
}
