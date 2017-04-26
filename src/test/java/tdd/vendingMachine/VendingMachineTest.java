package tdd.vendingMachine;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.ProductType;

public class VendingMachineTest {

    VendingMachine vendingMachine;

    @Before
    public void beforeMethod() {
        vendingMachine = new VendingMachine();
    }

    @Test
    public void vendingMachineShouldDisplaySelectedProductPrice() {
        // given
        ProductType testProductType = ProductType.MINERAL_WATER;
        vendingMachine.addShelve(0, new VendingMachineShelve(testProductType));

        // when
        vendingMachine.selectShelve(0);

        // then
        String displayValue = vendingMachine.getMessageFromDisplay();
        Assertions.assertThat(displayValue).isNotNull()
            .isEqualTo(String.format("Product price: %s", testProductType.getPrice()));
    }

    @Test
    public void vendingMachineShouldAcceptMoneyAndDisplayRemainingAmount() {
        // given
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.CHOCOLATE_BAR));

        // when
        vendingMachine.selectShelve(0);
        vendingMachine.insertMoneyForTransaction(Denomination.TWO_TENTHS);
        vendingMachine.insertMoneyForTransaction(Denomination.TENTH);

        // then
        String displayValue = vendingMachine.getMessageFromDisplay();
        Assertions.assertThat(displayValue).isNotNull().isEqualTo("Remaining amount: 1.7");
    }

    @Test
    public void vendingMachineShouldDisplayErrorMessageWhenShelveDoesNotExist() {
        // given
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.COLA_DRINK));

        // when
        vendingMachine.selectShelve(1);

        // then
        String displayValue = vendingMachine.getMessageFromDisplay();
        Assertions.assertThat(displayValue).isNotNull().isEqualTo("Invalid shelve number");
    }
}
