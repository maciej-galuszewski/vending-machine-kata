package tdd.vendingMachine.model;

public enum DisplayValue {

    PRODUCT_PRICE("Product price: %s"),
    REMAINING_AMOUNT("Remaining amount: %s"),
    INVALID_SHELVE("Invalid shelve number");

    private String value;

    DisplayValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
