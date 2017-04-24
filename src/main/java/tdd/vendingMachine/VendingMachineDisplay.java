package tdd.vendingMachine;

import tdd.vendingMachine.model.DisplayValue;

public class VendingMachineDisplay {

    private String displayValue = null;

    public void setDisplayValue(DisplayValue displayValue, Object... args) {
        this.displayValue = String.format(displayValue.getValue(), args);
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
