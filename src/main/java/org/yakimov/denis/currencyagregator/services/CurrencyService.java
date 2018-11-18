package org.yakimov.denis.currencyagregator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yakimov.denis.currencyagregator.dao.IBankRepository;
import org.yakimov.denis.currencyagregator.dao.ICurrencyValueRepository;
import org.yakimov.denis.currencyagregator.dao.IHistoryActionRepository;
import org.yakimov.denis.currencyagregator.dao.INationalCurrencyRepository;
import org.yakimov.denis.currencyagregator.dto.CurrencyDto;
import org.yakimov.denis.currencyagregator.models.Bank;
import org.yakimov.denis.currencyagregator.models.CurrencyActionType;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;
import org.yakimov.denis.currencyagregator.models.NationalCurrency;
import org.yakimov.denis.currencyagregator.support.StaticMessages;
import org.yakimov.denis.currencyagregator.support.WrongIncomingDataException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurrencyService implements ICurrencyService {
    private static final Logger LOG = LoggerFactory.getLogger(CurrencyService.class);
    private static final Comparator<CurrencyValue> dateComparator = Comparator.comparing(x -> x.getChanged());

    @Autowired
    private ICurrencyValueRepository currencyValueRepository;

    @Autowired
    private INationalCurrencyRepository nationalCurrencyRepository;

    @Autowired
    private IBankRepository bankRepository;

    @Autowired
    private HistoryService historyService;


    @Override
    public List<CurrencyValue> getLiveCurrency(boolean ascendByPrice) {
        return null;
    }

    @Override
    public List<CurrencyDto> getSpecificCurrency(String currencyShortName, boolean isBuying, boolean ascendByPrice) throws WrongIncomingDataException {

        List<CurrencyValue> result;
        NationalCurrency nationalCurrency = nationalCurrencyRepository.getByShortName(currencyShortName);
        if (nationalCurrency==null) {
            String message = String.format(StaticMessages.MESSAGE_ILLEGAL_CURRENCY_NAME, currencyShortName);
            LOG.info(message);
            throw new WrongIncomingDataException(message);
        }
        CurrencyActionType actionType = isBuying ? CurrencyActionType.BUYING : CurrencyActionType.SELLING;

        if (ascendByPrice) {
                result = currencyValueRepository.getByTypeAndSellingValueAndDisabledOrderByValueAsc(nationalCurrency, actionType, false);
        } else {
                result = currencyValueRepository.getByTypeAndSellingValueAndDisabledOrderByValueDesc(nationalCurrency, actionType, false);
        }

        return result.stream().map(this::convert).collect(Collectors.toList());
    }


    @Transactional
    @Override
    public CurrencyDto persistCurrency(CurrencyDto newValue) throws WrongIncomingDataException {
        NationalCurrency currency = nationalCurrencyRepository.getByShortName(newValue.getName());
        Bank bank = bankRepository.getByDisplayName(newValue.getBank());
        CurrencyActionType actionType = CurrencyActionType.valueOf(newValue.getAction());
        Boolean allowed = newValue.getAllowed();
        BigDecimal value = new BigDecimal(newValue.getValue());

        try {
            List<CurrencyValue> previousList = currencyValueRepository.getByTypeAndBankAndSellingValue(currency, bank, actionType);

            CurrencyValue toPersist;

            Optional<CurrencyValue> previousOptional = previousList.stream().sorted(dateComparator).findFirst();
            if (previousOptional.isPresent()) {
                for (CurrencyValue currentPrevious : previousList) {
                    if (currentPrevious != previousOptional.get()) {
                        historyService.persistHistory(null, currentPrevious);
                        currentPrevious.setDisabled(true);
                    }
                }
                toPersist = previousOptional.get();
            } else {
                toPersist = new CurrencyValue();
            }

            toPersist.setBank(bank);
            toPersist.setOperationAllowed(allowed);
            toPersist.setSellingValue(actionType);
            toPersist.setType(currency);
            toPersist.setValue(value);
            toPersist.setChanged(new Date());

            historyService.persistHistory(null, toPersist);
            CurrencyValue result = currencyValueRepository.save(toPersist);
            return convert(result);

        } catch (RuntimeException e) {
            String message = String.format(StaticMessages.MESSAGE_ILLEGAL_CURRENCY_CREATION, newValue);
            LOG.warn(message, e);
            throw new WrongIncomingDataException(message);
        }
    }


    @Transactional
    @Override
    public void persistCurrencyList(List<CurrencyValue> valueList) {
        LOG.info("Saving list to the database");

        currencyValueRepository.saveAll(valueList);
//        Set<Bank> banks = valueList.stream().map(x->x.getBank()).collect(Collectors.toSet());
//        List<Bank> updatedBanks = bankRepository.saveAll(banks);
//
//        Set<NationalCurrency> currencyTypes = valueList.stream().map(x->x.getType()).collect(Collectors.toSet());
//        List<NationalCurrency> updatedTypes = nationalCurrencyRepository.saveAll(currencyTypes);
//
//        int i=0;
//        int j=0;
//
//        for (CurrencyValue value: valueList){
//            Bank currentBank = updatedBanks.get(j);
//            NationalCurrency nationalCurrency = updatedTypes.get(i);
//
//            value.setBank(null);
//            value.setType(null);
//            value.setChanged(new Date());
//
//            currencyValueRepository.save(value);
//            //currencyValueRepository.flush();
//            value.setBank(currentBank);
//            currentBank.getCurrencyValueList().add(value);
//            value.setType(nationalCurrency);
//            nationalCurrency.getCurrencyValueList().add(value);
//            //currencyValueRepository.save(value);
//
//            i++;
//            if (i==3){
//                j++;
//                i=0;
//            }
//        }
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

    private CurrencyDto convert(CurrencyValue value) {
        return new CurrencyDto(value);
    }
}
