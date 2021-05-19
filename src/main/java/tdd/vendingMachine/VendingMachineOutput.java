package tdd.vendingMachine;

import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.DisplayMessage;
import tdd.vendingMachine.model.Product;

import java.util.ArrayList;
import java.util.List;

public class VendingMachineOutput {

    private final List<Denomination> droppedMoney = new ArrayList<>();
    private Product droppedProduct = null;
    private String displayMessage = null;

    public void dropMoney(List<Denomination> moneyToDrop) {
        droppedMoney.addAll(moneyToDrop);
    }

    public void dropProduct(Product product) {
        this.droppedProduct = product;
    }

    public void setDisplayMessage(DisplayMessage displayMessage, Object... args) {
        if (displayMessage == null) {
            this.displayMessage = null;
        } else {
            this.displayMessage = String.format(displayMessage.getValue(), args);
        }
    }

    public void clearOutput() {
        droppedMoney.clear();
        droppedProduct = null;
        displayMessage = null;
    }

    public List<Denomination> getDroppedMoney() {
        return droppedMoney;
    }

    public Product getDroppedProduct() {
        return droppedProduct;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }
}
