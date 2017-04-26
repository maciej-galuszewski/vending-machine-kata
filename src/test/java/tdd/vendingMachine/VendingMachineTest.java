package tdd.vendingMachine;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import tdd.vendingMachine.model.Denomination;
import tdd.vendingMachine.model.ProductType;

public class VendingMachineTest {

    @Test
    public void vendingMachineShouldAcceptMoneyAndDisplayRemainingAmount() {
        // given
        VendingMachine vendingMachine = new VendingMachine();
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
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addShelve(0, new VendingMachineShelve(ProductType.COLA_DRINK));

        // when
        vendingMachine.selectShelve(1);

        // then
        String displayValue = vendingMachine.getMessageFromDisplay();
        Assertions.assertThat(displayValue).isNotNull().isEqualTo("Invalid shelve number");
    }
}
