package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tdd.vendingMachine.changestrategy.ChangeStrategy;
import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.ProductType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        assertThat(output.getDisplayMessage()).isNotNull().isEqualTo(String.format("Product price: %s", testProductType.getPrice()));
        assertThat(output.getDroppedMoney()).isEmpty();
        assertThat(output.getDroppedProduct()).isNull();
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
        assertThat(output.getDisplayMessage()).isNotNull().isEqualTo("Remaining amount: 1.7");
        assertThat(output.getDroppedMoney()).isEmpty();
        assertThat(output.getDroppedProduct()).isNull();
    }

    @Test
    public void vendingMachineShouldDisplayErrorMessageWhenShelveDoesNotExist() {
        // given
        sut.addShelve(0, new VendingMachineShelve(ProductType.COLA_DRINK, 1));

        // when
        sut.selectShelve(1);

        // then
        VendingMachineOutput output = sut.getOutput();
        assertThat(output.getDisplayMessage()).isNotNull().isEqualTo("Invalid shelve number");
        assertThat(output.getDroppedMoney()).isEmpty();
        assertThat(output.getDroppedProduct()).isNull();
    }

    @Test
    public void vendingMachineShouldNotAcceptMoneyWhenShelveIsNotSelected() {
        // given
        Denomination testDenomination = Denomination.FIVE;

        // when
        sut.insertMoneyForTransaction(testDenomination);

        // then
        VendingMachineOutput output = sut.getOutput();
        assertThat(output.getDisplayMessage()).isNull();
        assertThat(output.getDroppedMoney()).isNotEmpty().containsExactly(testDenomination);
        assertThat(output.getDroppedProduct()).isNull();
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
        assertThat(output.getDisplayMessage()).isNull();
        assertThat(output.getDroppedMoney()).isNotEmpty().isEqualTo(testDenominations);
        assertThat(output.getDroppedProduct()).isNull();
    }

    @Test
    public void vendingMachineShouldDisplayErrorMessageAfterSelectingShelveWhenProductIsNotAvailable() {
        // given
        sut.addShelve(0, new VendingMachineShelve(ProductType.CHOCOLATE_BAR, 0));

        // when
        sut.selectShelve(0);

        // then
        VendingMachineOutput output = sut.getOutput();
        assertThat(output.getDisplayMessage()).isNotNull().isEqualTo("Product not available");
        assertThat(output.getDroppedMoney()).isEmpty();
        assertThat(output.getDroppedProduct()).isNull();
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
        assertThat(output.getDisplayMessage()).isNull();
        assertThat(output.getDroppedMoney()).isEmpty();
        assertThat(output.getDroppedProduct()).isNotNull();
        assertThat(output.getDroppedProduct().getProductType()).isEqualTo(testProductType);
        verify(changeStrategy, never()).calculateChange(any(), any());
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
        assertThat(output.getDisplayMessage()).isNotNull().isEqualTo("Product not available");
        assertThat(output.getDroppedMoney()).isEmpty();
        assertThat(output.getDroppedProduct()).isNull();
    }

    @Test
    public void vendingMachineShouldDropProductAndReturnChangeWhenGreaterAmountIsInserted() {
        // given
        ProductType testProductType = ProductType.MINERAL_WATER;
        sut.addShelve(0, new VendingMachineShelve(testProductType, 1));

        Denomination changeDenomination = Denomination.TWO_TENTHS;
        sut.insertStoredMoney(changeDenomination);

        List<Denomination> change = Collections.singletonList(changeDenomination);
        when(changeStrategy.calculateChange(any(), any())).thenReturn(change);

        // when
        sut.selectShelve(0);
        sut.insertMoneyForTransaction(Denomination.TWO);

        // then
        VendingMachineOutput output = sut.getOutput();
        assertThat(output.getDisplayMessage()).isNull();
        assertThat(output.getDroppedMoney()).isNotEmpty().isEqualTo(change);
        assertThat(output.getDroppedProduct()).isNotNull();
        assertThat(output.getDroppedProduct().getProductType()).isEqualTo(testProductType);
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

        when(changeStrategy.calculateChange(any(), any())).thenReturn(null);

        // when
        sut.selectShelve(0);
        testDenominations.forEach(sut::insertMoneyForTransaction);

        // then
        VendingMachineOutput output = sut.getOutput();
        assertThat(output.getDisplayMessage()).isEqualTo("Change not available");
        assertThat(output.getDroppedMoney()).isNotEmpty().isEqualTo(testDenominations);
        assertThat(output.getDroppedProduct()).isNull();
    }
}
