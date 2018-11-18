package org.yakimov.denis.currencyagregator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yakimov.denis.currencyagregator.models.Bank;
import org.yakimov.denis.currencyagregator.models.CurrencyActionType;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;
import org.yakimov.denis.currencyagregator.models.NationalCurrency;

import java.util.List;

public interface ICurrencyValueRepository extends JpaRepository<CurrencyValue, Long> {
    List<CurrencyValue> getByTypeAndBankAndSellingValueAndDisabled(NationalCurrency type, Bank bank, CurrencyActionType sellingValue, boolean disabled);
    List<CurrencyValue> getByTypeAndBankAndSellingValueAndDisabledAndOperationAllowed(NationalCurrency type, Bank bank, CurrencyActionType sellingValue, boolean disabled, boolean operationAllowed);
    List<CurrencyValue> getByTypeAndBankAndDisabled(NationalCurrency type, Bank bank, boolean disabled);
    List<CurrencyValue> getByBankAndSellingValueAndDisabled(Bank bank, CurrencyActionType sellingValue, boolean disabled);
    List<CurrencyValue> getByBankAndDisabled(Bank bank, boolean disabled);

    List<CurrencyValue> getByTypeAndSellingValueAndDisabledAndOperationAllowedOrderByValueAsc(NationalCurrency type, CurrencyActionType sellingValue, boolean disabled, boolean operationAllowed);
    List<CurrencyValue> getByTypeAndSellingValueAndDisabledAndOperationAllowedOrderByValueDesc(NationalCurrency type, CurrencyActionType sellingValue, boolean disabled, boolean operationAllowed);
    List<CurrencyValue> getByTypeAndBank(NationalCurrency type, Bank bank);
    List<CurrencyValue> getByBank(Bank bank);
}
