package tdd.vendingMachine.model;

public enum DisplayMessage {

    PRODUCT_PRICE("Product price: %s"),
    REMAINING_AMOUNT("Remaining amount: %s"),
    INVALID_SHELVE("Invalid shelve number"),
    PRODUCT_NOT_AVAILABLE("Product not available"),
    CHANGE_NOT_AVAILABLE("Change not available");

    private String value;

    DisplayMessage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
