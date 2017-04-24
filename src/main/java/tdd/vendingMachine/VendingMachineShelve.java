package tdd.vendingMachine;

import tdd.vendingMachine.model.ProductType;

import java.math.BigDecimal;

public class VendingMachineShelve {

    private final ProductType productType;

    public VendingMachineShelve(ProductType productType) {
        this.productType = productType;
    }

    public BigDecimal getProductPrice() {
        return productType.getPrice();
    }
}
