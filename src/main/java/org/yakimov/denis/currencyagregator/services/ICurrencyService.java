package org.yakimov.denis.currencyagregator.services;

import org.yakimov.denis.currencyagregator.dto.CurrencyDto;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;
import org.yakimov.denis.currencyagregator.support.WrongIncomingDataException;

import java.util.List;

public interface ICurrencyService {
    List<CurrencyDto> getSpecificCurrency(String currencyShortName, boolean isBuying, boolean ascendByPrice) throws WrongIncomingDataException;
    CurrencyDto persistCurrency(CurrencyDto newValue) throws WrongIncomingDataException;
    void persistCurrencyList(List<CurrencyValue> valueList);
    List<CurrencyDto> changeSpecificCurrencyAllowanceByBank(String bankName, String shortName, String action, Boolean allow, boolean delete) throws WrongIncomingDataException;
    List<CurrencyValue> getAllData();
}
