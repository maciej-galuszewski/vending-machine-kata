package tdd.vendingMachine.changestrategy;

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
public class ChangeStrategyTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {Collections.singletonList(Denomination.FIVE),
                new BigDecimal("5"),
                Collections.singletonList(Denomination.FIVE)},
            {Collections.singletonList(Denomination.TWO),
                new BigDecimal("5"),
                null},
            {Arrays.asList(Denomination.FIVE, Denomination.TENTH),
                new BigDecimal("5.1"),
                Arrays.asList(Denomination.FIVE, Denomination.TENTH)},
            {Arrays.asList(Denomination.FIVE, Denomination.TENTH),
                new BigDecimal("5.2"),
                null},
            {Arrays.asList(Denomination.FIVE, Denomination.FIVE),
                new BigDecimal("10"),
                Arrays.asList(Denomination.FIVE, Denomination.FIVE)},
            {Arrays.asList(Denomination.FIVE, Denomination.TWO, Denomination.ONE, Denomination.TWO),
                new BigDecimal("10"),
                Arrays.asList(Denomination.FIVE, Denomination.TWO, Denomination.TWO, Denomination.ONE)},
            {Arrays.asList(Denomination.FIVE, Denomination.TWO, Denomination.TWO, Denomination.TWO),
                new BigDecimal("10"),
                null},
            {Arrays.asList(Denomination.FIVE, Denomination.ONE, Denomination.ONE, Denomination.ONE, Denomination.TWO, Denomination.ONE),
                new BigDecimal("8"),
                Arrays.asList(Denomination.FIVE, Denomination.TWO, Denomination.ONE)}
        });
    }

    private ChangeStrategy sut;

    private List<Denomination> availableDenominations;

    private BigDecimal amountToReturn;

    private List<Denomination> expectedChange;

    public ChangeStrategyTest(List<Denomination> availableDenominations, BigDecimal amountToReturn, List<Denomination> expectedChange) {
        this.availableDenominations = availableDenominations;
        this.amountToReturn = amountToReturn;
        this.expectedChange = expectedChange;
    }

    @Test
    public void calculateChangeShouldReturnCorrectChange() {
        // given
        sut = new ChangeStrategy();

        // when
        List<Denomination> change = sut.calculateChange(availableDenominations, amountToReturn);

        // then
        Assertions.assertThat(change).isEqualTo(expectedChange);
    }
}
