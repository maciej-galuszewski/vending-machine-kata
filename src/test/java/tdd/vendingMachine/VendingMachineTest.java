package tdd.vendingMachine;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.ProductType;

import java.util.Arrays;
import java.util.List;

public class VendingMachineTest {

    private VendingMachine sut;

    @Before
    public void beforeMethod() {
        sut = new VendingMachine();
    }

    @Test
    public void vendingMachineShouldDisplaySelectedProductPrice() {
        // given
        ProductType testProductType = ProductType.MINERAL_WATER;
        sut.addShelve(0, new VendingMachineShelve(testProductType, 1));

        // when
        sut.selectShelve(0);

        // then
        Assertions.assertThat(sut.getMessageFromDisplay()).isNotNull()
            .isEqualTo(String.format("Product price: %s", testProductType.getPrice()));
        Assertions.assertThat(sut.getDroppedMoney()).isEmpty();
    }

    @Test
    public void vendingMachineShouldAcceptMoneyAndDisplayRemainingAmount() {
        // given
        sut.addShelve(0, new VendingMachineShelve(ProductType.CHOCOLATE_BAR, 1));

        // when
        sut.selectShelve(0);
        sut.insertMoneyForTransaction(Denomination.TWO_TENTHS);
        sut.insertMoneyForTransaction(Denomination.TENTH);

        // then
        Assertions.assertThat(sut.getMessageFromDisplay()).isNotNull().isEqualTo("Remaining amount: 1.7");
        Assertions.assertThat(sut.getDroppedMoney()).isEmpty();
    }

    @Test
    public void vendingMachineShouldDisplayErrorMessageWhenShelveDoesNotExist() {
        // given
        sut.addShelve(0, new VendingMachineShelve(ProductType.COLA_DRINK, 1));

        // when
        sut.selectShelve(1);

        // then
        Assertions.assertThat(sut.getMessageFromDisplay()).isNotNull().isEqualTo("Invalid shelve number");
        Assertions.assertThat(sut.getDroppedMoney()).isEmpty();
    }

    @Test
    public void vendingMachineShouldNotAcceptMoneyWhenShelveIsNotSelected() {
        // given
        Denomination testDenomination = Denomination.FIVE;

        // when
        sut.insertMoneyForTransaction(testDenomination);

        // then
        Assertions.assertThat(sut.getMessageFromDisplay()).isNull();
        Assertions.assertThat(sut.getDroppedMoney()).isNotEmpty().containsExactly(testDenomination);
    }

    @Test
    public void vendingMachineShouldReturnTransactionMoneyWhenInsufficientMoneyIsInsertedAndCancelButtonIsPressed() {
        // given
        List<Denomination> testDenominations = Arrays.asList(Denomination.ONE, Denomination.TENTH);
        sut.addShelve(0, new VendingMachineShelve(ProductType.MINERAL_WATER, 1));

        // when
        sut.selectShelve(0);
        testDenominations.forEach(sut::insertMoneyForTransaction);
        sut.pressCancelButton();

        // then
        Assertions.assertThat(sut.getMessageFromDisplay()).isNull();
        Assertions.assertThat(sut.getDroppedMoney()).isNotEmpty().isEqualTo(testDenominations);
    }

    @Test
    public void vendingMachineShouldDisplayErrorMessageAfterSelectingShelveWhenProductIsNotAvailable() {
        // given
        sut.addShelve(0, new VendingMachineShelve(ProductType.CHOCOLATE_BAR, 0));

        // when
        sut.selectShelve(0);

        // then
        Assertions.assertThat(sut.getMessageFromDisplay()).isNotNull().isEqualTo("Product not available");
        Assertions.assertThat(sut.getDroppedMoney()).isEmpty();
    }
}
