package org.yakimov.denis.currencyagregator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yakimov.denis.currencyagregator.models.Bank;
import org.yakimov.denis.currencyagregator.models.CurrencyActionType;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;
import org.yakimov.denis.currencyagregator.models.NationalCurrency;

import java.util.List;

public interface ICurrencyValueRepository extends JpaRepository<CurrencyValue, Long> {
    List<CurrencyValue> getByTypeAndBankAndSellingValue(NationalCurrency type, Bank bank, CurrencyActionType sellingValue);
    List<CurrencyValue> getByTypeAndSellingValueAndDisabledOrderByValueAsc(NationalCurrency type, CurrencyActionType sellingValue, boolean disabled);
    List<CurrencyValue> getByTypeAndSellingValueAndDisabledOrderByValueDesc(NationalCurrency type, CurrencyActionType sellingValue, boolean disabled);
    List<CurrencyValue> getByTypeAndBank(NationalCurrency type, Bank bank);
    List<CurrencyValue> getByBank(Bank bank);
}
