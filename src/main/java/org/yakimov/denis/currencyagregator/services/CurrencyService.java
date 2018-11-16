package org.yakimov.denis.currencyagregator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yakimov.denis.currencyagregator.dao.ICurrencyValueRepository;
import org.yakimov.denis.currencyagregator.dao.IHistoryActionRepository;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService implements ICurrencyService {
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyService.class);
    private static final Comparator<CurrencyValue> dateComparator = Comparator.comparing(x -> x.getChanged());

    @Autowired
    private ICurrencyValueRepository currencyValueRepository;

    @Autowired
    private HistoryService historyService;


    @Override
    public List<CurrencyValue> getLiveCurrency(boolean ascendByPrice) {
        return null;
    }

    @Override
    public List<CurrencyValue> getSpecificCurrency(String currencyShortName, boolean ascendByPrice) {
        return null;
    }


    @Transactional
    @Override
    public boolean persistCurrency(CurrencyValue newValue) {
        List<CurrencyValue> previousList = currencyValueRepository.getByTypeAndBankAndSellingValue(newValue.getType(), newValue.getBank(), newValue.getSellingValue());

        Optional<CurrencyValue> previousOptional = previousList.stream().sorted(dateComparator).findFirst();
        if (previousOptional.isPresent()){
            for (CurrencyValue currentPrevious: previousList){
                if (currentPrevious!=previousOptional.get()){
                    historyService.persistHistory(null, currentPrevious);
                    currentPrevious.setDisabled(true);
                }
            }

            CurrencyValue previous = previousOptional.get();
            previous.setBank(newValue.getBank());
            previous.setOperationAllowed(newValue.getOperationAllowed());
            previous.setSellingValue(newValue.getSellingValue());
            previous.setType(newValue.getType());
            previous.setValue(newValue.getValue());
            previous.setChanged(new Date());

            historyService.persistHistory(previous, newValue);
            currencyValueRepository.save(previous);
            return true;

        } else {
            newValue.setChanged(new Date());
            historyService.persistHistory(null, newValue);
            currencyValueRepository.save(newValue);
            return true;
        }
    }


    @Override
    public boolean persistCurrencyList(List<CurrencyValue> valueList) {
        LOG.info("Saving list to the dataqbase");
        boolean result = true;

        for (CurrencyValue value: valueList){
            if (!persistCurrency(value)){
                result = false;
            }
        }

        return result;
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
