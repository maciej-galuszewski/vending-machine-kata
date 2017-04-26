package tdd.vendingMachine.model;

public enum DisplayValue {

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
