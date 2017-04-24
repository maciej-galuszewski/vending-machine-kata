package tdd.vendingMachine;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import tdd.vendingMachine.model.Denomination;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class VendingMachineTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {Collections.singletonList(Denomination.TWO_TENTHS), new BigDecimal("0.2")},
            {Arrays.asList(Denomination.FIVE, Denomination.HALF), new BigDecimal("5.5")},
            {Arrays.asList(Denomination.TWO, Denomination.ONE, Denomination.TENTH), new BigDecimal("3.1")}
        });
    }

    private List<Denomination> insertedMoney;

    private BigDecimal expectedTotalAmount;

    public VendingMachineTest(List<Denomination> insertedMoney, BigDecimal expectedTotalAmount) {
        this.insertedMoney = insertedMoney;
        this.expectedTotalAmount = expectedTotalAmount;
    }

    @Test
    public void vendingMachineShouldAcceptMoneyAndDisplayTotalAmount() {
        // given
        VendingMachine vendingMachine = new VendingMachine();

        // when
        insertedMoney.forEach(vendingMachine::insertMoney);

        // then
        BigDecimal totalAmount = vendingMachine.getTotalAmount();
        Assertions.assertThat(totalAmount).isEqualTo(expectedTotalAmount);
    }
}
