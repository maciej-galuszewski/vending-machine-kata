package tdd.vendingMachine.changestrategy;

import tdd.vendingMachine.model.Denomination;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ChangeStrategy {

    public List<Denomination> calculateChange(List<Denomination> availableDenominations, BigDecimal amountToReturn) {
        availableDenominations.sort(Comparator.comparing(Denomination::getAmount).reversed());

        List<List<Denomination>> results = new ArrayList<>();
        for (int i = 0; i < availableDenominations.size(); i++) {
            calculateChangeRecursively(results, availableDenominations, Arrays.asList(availableDenominations.get(i)), i, amountToReturn);
        }
        return results.stream().min(Comparator.comparingInt(List::size)).orElse(null);
    }

    private void calculateChangeRecursively(List<List<Denomination>> results,
                                            List<Denomination> availableDenominations,
                                            List<Denomination> denominationsSubTree,
                                            int startIndex,
                                            BigDecimal amountToReturn) {
        BigDecimal currentDenominationAmount = availableDenominations.get(startIndex).getAmount();
        BigDecimal subtractResult = amountToReturn.subtract(currentDenominationAmount);
        int comparisonResult = subtractResult.compareTo(BigDecimal.ZERO);
        if (comparisonResult < 0) {
            return;
        }

        denominationsSubTree.add(availableDenominations.get(startIndex));
        if (comparisonResult == 0) {
            results.add(denominationsSubTree);
            return;
        }
        for (int i = startIndex + 1; i < availableDenominations.size(); i++) {
            calculateChangeRecursively(results, availableDenominations, denominationsSubTree, i, subtractResult);
        }
    }
}
