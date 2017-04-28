package tdd.vendingMachine;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.ProductType;

import java.util.Arrays;
import java.util.List;

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
        vendingMachine.addShelve(0, new VendingMachineShelve(testProductType, 1));

        // when
        vendingMachine.selectShelve(0);

        // then
        Assertions.assertThat(vendingMachine.getMessageFromDisplay()).isNotNull()
            .isEqualTo(String.format("Product price: %s", testProductType.getPrice()));
        Assertions.assertThat(vendingMachine.getDroppedMoney()).isEmpty();
    }

    @Test
    public void vendingMachineShouldAcceptMoneyAndDisplayRemainingAmount() {
        // given
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.CHOCOLATE_BAR, 1));

        // when
        vendingMachine.selectShelve(0);
        vendingMachine.insertMoneyForTransaction(Denomination.TWO_TENTHS);
        vendingMachine.insertMoneyForTransaction(Denomination.TENTH);

        // then
        Assertions.assertThat(vendingMachine.getMessageFromDisplay()).isNotNull().isEqualTo("Remaining amount: 1.7");
        Assertions.assertThat(vendingMachine.getDroppedMoney()).isEmpty();
    }

    @Test
    public void vendingMachineShouldDisplayErrorMessageWhenShelveDoesNotExist() {
        // given
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.COLA_DRINK, 1));

        // when
        vendingMachine.selectShelve(1);

        // then
        Assertions.assertThat(vendingMachine.getMessageFromDisplay()).isNotNull().isEqualTo("Invalid shelve number");
        Assertions.assertThat(vendingMachine.getDroppedMoney()).isEmpty();
    }

    @Test
    public void vendingMachineShouldNotAcceptMoneyWhenShelveIsNotSelected() {
        // given
        Denomination testDenomination = Denomination.FIVE;

        // when
        vendingMachine.insertMoneyForTransaction(testDenomination);

        // then
        Assertions.assertThat(vendingMachine.getMessageFromDisplay()).isNull();
        Assertions.assertThat(vendingMachine.getDroppedMoney()).isNotEmpty().containsExactly(testDenomination);
    }

    @Test
    public void vendingMachineShouldReturnTransactionMoneyWhenInsufficientMoneyIsInsertedAndCancelButtonIsPressed() {
        // given
        List<Denomination> testDenominations = Arrays.asList(Denomination.ONE, Denomination.TENTH);
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.MINERAL_WATER, 1));

        // when
        vendingMachine.selectShelve(0);
        testDenominations.forEach(vendingMachine::insertMoneyForTransaction);
        vendingMachine.pressCancelButton();

        // then
        Assertions.assertThat(vendingMachine.getMessageFromDisplay()).isNull();
        Assertions.assertThat(vendingMachine.getDroppedMoney()).isNotEmpty().isEqualTo(testDenominations);
    }

    @Test
    public void vendingMachineShouldDisplayErrorMessageAfterSelectingShelveWhenProductIsNotAvailable() {
        // given
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.CHOCOLATE_BAR, 0));

        // when
        vendingMachine.selectShelve(0);

        // then
        Assertions.assertThat(vendingMachine.getMessageFromDisplay()).isNotNull().isEqualTo("Product not available");
        Assertions.assertThat(vendingMachine.getDroppedMoney()).isEmpty();
    }
}
