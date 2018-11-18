package org.yakimov.denis.currencyagregator.services;

import org.yakimov.denis.currencyagregator.dto.CurrencyDto;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;
import org.yakimov.denis.currencyagregator.support.WrongIncomingDataException;

import java.util.List;

public interface ICurrencyService {
    List<CurrencyValue> getLiveCurrency(boolean ascendByPrice);
    List<CurrencyDto> getSpecificCurrency(String currencyShortName, boolean isBuying, boolean ascendByPrice) throws WrongIncomingDataException;
    CurrencyDto persistCurrency(CurrencyDto newValue) throws WrongIncomingDataException;
    void persistCurrencyList(List<CurrencyValue> valueList);
    boolean changeAllowanceByBank(String bankName, boolean allow);
    boolean changeSpecificCurrencyAllowanceByBank(String bankName, String shortName, boolean allow);
    List<CurrencyValue> getBestPrices();
}
