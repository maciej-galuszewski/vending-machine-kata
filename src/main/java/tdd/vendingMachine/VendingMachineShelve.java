package tdd.vendingMachine;

import tdd.vendingMachine.model.Product;
import tdd.vendingMachine.model.ProductType;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class VendingMachineShelve {

    private final ProductType productType;

    private final LinkedList<Product> products = new LinkedList<>();

    public VendingMachineShelve(ProductType productType, int numberOfProducts) {
        this.productType = productType;
        IntStream.range(0, numberOfProducts).forEach(i -> products.add(new Product(productType)));
    }

    public BigDecimal getProductPrice() {
        return productType.getPrice();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }
}
