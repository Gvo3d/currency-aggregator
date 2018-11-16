package org.yakimov.denis.currencyagregator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;

import java.util.List;

@Service
public class CurrencyService implements ICurrencyService {
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyService.class);

    @Override
    public List<CurrencyValue> getLiveCurrency(boolean ascendByPrice) {
        return null;
    }

    @Override
    public List<CurrencyValue> getSpecificCurrency(String currencyShortName, boolean ascendByPrice) {
        return null;
    }

    @Override
    public boolean persistCurrency(CurrencyValue newValue) {
        return false;
    }

    @Override
    public boolean changeAllowanceByBank(String bankName, boolean allow) {
        return false;
    }

    @Override
    public boolean changeSpecificCurrencyAllowanceByBank(String bankName, String shortName, boolean allow) {
        return false;
    }

    @Override
    public List<CurrencyValue> getBestPrices() {
        return null;
    }
}
