package tdd.vendingMachine;

import org.junit.Before;
import org.junit.Test;
import tdd.vendingMachine.changestrategy.ChangeStrategy;
import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.ProductType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private VendingMachine vendingMachine;

    @Before
    public void beforeMethod() {
        vendingMachine = new VendingMachine(new ChangeStrategy());
    }

    @Test
    public void correctChangeTest() {
        // given
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.MINERAL_WATER, 1));
        vendingMachine.insertStoredMoney(Denomination.TWO_TENTHS);

        // when
        vendingMachine.selectShelve(0);
        vendingMachine.insertMoneyForTransaction(Denomination.TWO);

        // then
        VendingMachineOutput output = vendingMachine.getOutput();
        assertThat(output.getDroppedProduct()).isNotNull();
        assertThat(output.getDroppedProduct().getProductType()).isEqualTo(ProductType.MINERAL_WATER);
        assertThat(output.getDisplayMessage()).isNull();
        assertThat(output.getDroppedMoney()).isEqualTo(Collections.singletonList(Denomination.TWO_TENTHS));
    }

    @Test
    public void noChangeAvailableTest() {
        // given
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.MINERAL_WATER, 1));
        Denomination testDenomination = Denomination.TWO;

        // when
        vendingMachine.selectShelve(0);
        vendingMachine.insertMoneyForTransaction(testDenomination);

        // then
        VendingMachineOutput output = vendingMachine.getOutput();
        assertThat(output.getDroppedProduct()).isNull();
        assertThat(output.getDisplayMessage()).isNotNull().isEqualTo("Change not available");
        assertThat(output.getDroppedMoney()).isEqualTo(Collections.singletonList(testDenomination));
    }

    @Test
    public void vendingMachineShouldStoreTransactionMoneyAndUseThemToReturnChange() {
        // given
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.MINERAL_WATER, 1));
        vendingMachine.addShelve(1, new VendingMachineShelve(ProductType.COLA_DRINK, 1));
        List<Denomination> testDenominations = Arrays.asList(
            Denomination.ONE,
            Denomination.HALF,
            Denomination.TWO_TENTHS,
            Denomination.TENTH
        );

        // when
        vendingMachine.selectShelve(0);
        testDenominations.forEach(vendingMachine::insertMoneyForTransaction);
        vendingMachine.getOutput().clearOutput();
        vendingMachine.selectShelve(1);
        vendingMachine.insertMoneyForTransaction(Denomination.TWO);

        // then
        VendingMachineOutput output = vendingMachine.getOutput();
        assertThat(output.getDroppedProduct()).isNotNull();
        assertThat(output.getDroppedProduct().getProductType()).isEqualTo(ProductType.COLA_DRINK);
        assertThat(output.getDisplayMessage()).isNull();
        assertThat(output.getDroppedMoney()).isEqualTo(Collections.singletonList(Denomination.HALF));
    }

    @Test
    public void vendingMachineShouldUseTransactionMoneyForReturningChange() {
        // given
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.CHOCOLATE_BAR, 1));

        // when
        vendingMachine.selectShelve(0);
        vendingMachine.insertMoneyForTransaction(Denomination.ONE);
        vendingMachine.insertMoneyForTransaction(Denomination.TWO);

        // then
        VendingMachineOutput output = vendingMachine.getOutput();
        assertThat(output.getDroppedProduct()).isNotNull();
        assertThat(output.getDroppedProduct().getProductType()).isEqualTo(ProductType.CHOCOLATE_BAR);
        assertThat(output.getDisplayMessage()).isNull();
        assertThat(output.getDroppedMoney()).isEqualTo(Collections.singletonList(Denomination.ONE));
    }
}
