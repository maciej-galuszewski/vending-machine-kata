package tdd.vendingMachine.model;

public class Product {

    private final ProductType productType;

    public Product(ProductType productType) {
        this.productType = productType;
    }

    public ProductType getProductType() {
        return productType;
    }
}
