package tdd.vendingMachine;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import tdd.vendingMachine.changestrategy.ChangeStrategy;
import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.ProductType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class VendingMachineTest {

    private VendingMachine sut;

    @Mock
    private ChangeStrategy changeStrategy;

    @Before
    public void beforeMethod() {
        sut = new VendingMachine(changeStrategy);
    }

    @Test
    public void vendingMachineShouldDisplaySelectedProductPrice() {
        // given
        ProductType testProductType = ProductType.MINERAL_WATER;
        sut.addShelve(0, new VendingMachineShelve(testProductType, 1));

        // when
        sut.selectShelve(0);

        // then
        VendingMachineOutput output = sut.getOutput();
        Assertions.assertThat(output.getDisplayMessage()).isNotNull().isEqualTo(String.format("Product price: %s", testProductType.getPrice()));
        Assertions.assertThat(output.getDroppedMoney()).isEmpty();
        Assertions.assertThat(output.getDroppedProduct()).isNull();
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
        VendingMachineOutput output = sut.getOutput();
        Assertions.assertThat(output.getDisplayMessage()).isNotNull().isEqualTo("Remaining amount: 1.7");
        Assertions.assertThat(output.getDroppedMoney()).isEmpty();
        Assertions.assertThat(output.getDroppedProduct()).isNull();
    }

    @Test
    public void vendingMachineShouldDisplayErrorMessageWhenShelveDoesNotExist() {
        // given
        sut.addShelve(0, new VendingMachineShelve(ProductType.COLA_DRINK, 1));

        // when
        sut.selectShelve(1);

        // then
        VendingMachineOutput output = sut.getOutput();
        Assertions.assertThat(output.getDisplayMessage()).isNotNull().isEqualTo("Invalid shelve number");
        Assertions.assertThat(output.getDroppedMoney()).isEmpty();
        Assertions.assertThat(output.getDroppedProduct()).isNull();
    }

    @Test
    public void vendingMachineShouldNotAcceptMoneyWhenShelveIsNotSelected() {
        // given
        Denomination testDenomination = Denomination.FIVE;

        // when
        sut.insertMoneyForTransaction(testDenomination);

        // then
        VendingMachineOutput output = sut.getOutput();
        Assertions.assertThat(output.getDisplayMessage()).isNull();
        Assertions.assertThat(output.getDroppedMoney()).isNotEmpty().containsExactly(testDenomination);
        Assertions.assertThat(output.getDroppedProduct()).isNull();
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
        VendingMachineOutput output = sut.getOutput();
        Assertions.assertThat(output.getDisplayMessage()).isNull();
        Assertions.assertThat(output.getDroppedMoney()).isNotEmpty().isEqualTo(testDenominations);
        Assertions.assertThat(output.getDroppedProduct()).isNull();
    }

    @Test
    public void vendingMachineShouldDisplayErrorMessageAfterSelectingShelveWhenProductIsNotAvailable() {
        // given
        sut.addShelve(0, new VendingMachineShelve(ProductType.CHOCOLATE_BAR, 0));

        // when
        sut.selectShelve(0);

        // then
        VendingMachineOutput output = sut.getOutput();
        Assertions.assertThat(output.getDisplayMessage()).isNotNull().isEqualTo("Product not available");
        Assertions.assertThat(output.getDroppedMoney()).isEmpty();
        Assertions.assertThat(output.getDroppedProduct()).isNull();
    }

    @Test
    public void vendingMachineShouldDropProductAndNotReturnChangeWhenExactAmountIsInserted() {
        // given
        ProductType testProductType = ProductType.MINERAL_WATER;
        List<Denomination> testDenominations = Arrays.asList(
            Denomination.ONE,
            Denomination.HALF,
            Denomination.TWO_TENTHS,
            Denomination.TENTH
        );
        sut.addShelve(0, new VendingMachineShelve(testProductType, 1));

        // when
        sut.selectShelve(0);
        testDenominations.forEach(sut::insertMoneyForTransaction);

        // then
        VendingMachineOutput output = sut.getOutput();
        Assertions.assertThat(output.getDisplayMessage()).isNull();
        Assertions.assertThat(output.getDroppedMoney()).isEmpty();
        Assertions.assertThat(output.getDroppedProduct()).isNotNull();
        Assertions.assertThat(output.getDroppedProduct().getProductType()).isEqualTo(testProductType);
        Mockito.verify(changeStrategy, Mockito.never()).calculateChange(Mockito.any(), Mockito.any());
    }

    @Test
    public void vendingMachineShouldDecrementAvailableProductsWhenProductHasBeenDropped() {
        // given
        ProductType testProductType = ProductType.MINERAL_WATER;
        List<Denomination> testDenominations = Arrays.asList(
            Denomination.ONE,
            Denomination.HALF,
            Denomination.TWO_TENTHS,
            Denomination.TENTH
        );
        sut.addShelve(0, new VendingMachineShelve(testProductType, 1));

        // when
        sut.selectShelve(0);
        testDenominations.forEach(sut::insertMoneyForTransaction);
        sut.getOutput().clearOutput();
        sut.selectShelve(0);

        // then
        VendingMachineOutput output = sut.getOutput();
        Assertions.assertThat(output.getDisplayMessage()).isNotNull().isEqualTo("Product not available");
        Assertions.assertThat(output.getDroppedMoney()).isEmpty();
        Assertions.assertThat(output.getDroppedProduct()).isNull();
    }

    @Test
    public void vendingMachineShouldDropProductAndReturnChangeWhenGreaterAmountIsInserted() {
        // given
        ProductType testProductType = ProductType.MINERAL_WATER;
        sut.addShelve(0, new VendingMachineShelve(testProductType, 1));

        Denomination changeDenomination = Denomination.TWO_TENTHS;
        sut.insertStoredMoney(changeDenomination);

        List<Denomination> change = Collections.singletonList(changeDenomination);
        Mockito.when(changeStrategy.calculateChange(Mockito.any(), Mockito.any())).thenReturn(change);

        // when
        sut.selectShelve(0);
        sut.insertMoneyForTransaction(Denomination.TWO);

        // then
        VendingMachineOutput output = sut.getOutput();
        Assertions.assertThat(output.getDisplayMessage()).isNull();
        Assertions.assertThat(output.getDroppedMoney()).isNotEmpty().isEqualTo(change);
        Assertions.assertThat(output.getDroppedProduct()).isNotNull();
        Assertions.assertThat(output.getDroppedProduct().getProductType()).isEqualTo(testProductType);
    }

    @Test
    public void vendingMachineShouldAbortTransactionWhenNoChangeIsAvailable() {
        // given
        ProductType testProductType = ProductType.MINERAL_WATER;
        List<Denomination> testDenominations = Arrays.asList(
            Denomination.ONE,
            Denomination.HALF,
            Denomination.TWO_TENTHS,
            Denomination.TWO_TENTHS
        );
        sut.addShelve(0, new VendingMachineShelve(testProductType, 1));

        Mockito.when(changeStrategy.calculateChange(Mockito.any(), Mockito.any())).thenReturn(null);

        // when
        sut.selectShelve(0);
        testDenominations.forEach(sut::insertMoneyForTransaction);

        // then
        VendingMachineOutput output = sut.getOutput();
        Assertions.assertThat(output.getDisplayMessage()).isEqualTo("Change not available");
        Assertions.assertThat(output.getDroppedMoney()).isNotEmpty().isEqualTo(testDenominations);
        Assertions.assertThat(output.getDroppedProduct()).isNull();
    }
}
