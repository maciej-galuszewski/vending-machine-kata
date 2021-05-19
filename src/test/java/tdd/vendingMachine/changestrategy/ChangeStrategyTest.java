package tdd.vendingMachine.changestrategy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import tdd.vendingMachine.model.Denomination;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ChangeStrategyTest {

    @Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][]{
            {singletonList(Denomination.FIVE),
                new BigDecimal("5"),
                singletonList(Denomination.FIVE)},
            {singletonList(Denomination.TWO),
                new BigDecimal("5"),
                null},
            {asList(Denomination.FIVE, Denomination.TENTH),
                new BigDecimal("5.1"),
                asList(Denomination.FIVE, Denomination.TENTH)},
            {asList(Denomination.FIVE, Denomination.TENTH),
                new BigDecimal("5.2"),
                null},
            {asList(Denomination.FIVE, Denomination.FIVE),
                new BigDecimal("10"),
                asList(Denomination.FIVE, Denomination.FIVE)},
            {asList(Denomination.FIVE, Denomination.TWO, Denomination.ONE, Denomination.TWO),
                new BigDecimal("10"),
                asList(Denomination.FIVE, Denomination.TWO, Denomination.TWO, Denomination.ONE)},
            {asList(Denomination.FIVE, Denomination.TWO, Denomination.TWO, Denomination.TWO),
                new BigDecimal("10"),
                null},
            {asList(Denomination.FIVE, Denomination.ONE, Denomination.ONE, Denomination.ONE, Denomination.TWO, Denomination.ONE),
                new BigDecimal("8"),
                asList(Denomination.FIVE, Denomination.TWO, Denomination.ONE)}
        });
    }

    private final List<Denomination> availableDenominations;
    private final BigDecimal amountToReturn;
    private final List<Denomination> expectedChange;

    public ChangeStrategyTest(List<Denomination> availableDenominations, BigDecimal amountToReturn, List<Denomination> expectedChange) {
        this.availableDenominations = availableDenominations;
        this.amountToReturn = amountToReturn;
        this.expectedChange = expectedChange;
    }

    @Test
    public void calculateChangeShouldReturnCorrectChange() {
        // given
        ChangeStrategy sut = new ChangeStrategy();

        // when
        List<Denomination> change = sut.calculateChange(availableDenominations, amountToReturn);

        // then
        assertThat(change).isEqualTo(expectedChange);
    }
}
