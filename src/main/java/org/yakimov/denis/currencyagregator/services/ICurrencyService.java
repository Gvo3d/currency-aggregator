package org.yakimov.denis.currencyagregator.services;

import org.yakimov.denis.currencyagregator.models.CurrencyValue;

import java.util.List;

public interface ICurrencyService {
    List<CurrencyValue> getLiveCurrency(boolean ascendByPrice);
    List<CurrencyValue> getSpecificCurrency(String currencyShortName, boolean ascendByPrice);
    boolean persistCurrency(CurrencyValue newValue);
    boolean changeAllowanceByBank(String bankName, boolean allow);
    boolean changeSpecificCurrencyAllowanceByBank(String bankName, String shortName, boolean allow);
    List<CurrencyValue> getBestPrices();
}
