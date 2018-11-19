package org.yakimov.denis.currencyagregator.support;

import org.yakimov.denis.currencyagregator.models.CurrencyValue;

import java.util.Comparator;

public class CurrencyComparator implements Comparator<CurrencyValue> {
    private boolean ascend;

    public CurrencyComparator(boolean ascend) {
        this.ascend = ascend;
    }

    @Override
    public int compare(CurrencyValue o1, CurrencyValue o2) {
        if (ascend){
            return o1.getValue().compareTo(o2.getValue());
        } else {
            return o2.getValue().compareTo(o1.getValue());
        }
    }
}
