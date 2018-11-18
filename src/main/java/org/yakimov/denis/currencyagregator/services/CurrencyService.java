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
                result = currencyValueRepository.getByTypeAndSellingValueAndDisabledAndOperationAllowedOrderByValueAsc(nationalCurrency, actionType, false, true);
        } else {
                result = currencyValueRepository.getByTypeAndSellingValueAndDisabledAndOperationAllowedOrderByValueDesc(nationalCurrency, actionType, false, true);
        }

        return result.stream().map(this::convert).collect(Collectors.toList());
    }


    @Transactional
    @Override
    public CurrencyDto persistCurrency(CurrencyDto newValue) throws WrongIncomingDataException {

        NationalCurrency currency = nationalCurrencyRepository.getByShortName(newValue.getName());
        if (currency==null){
            NationalCurrency newOne = new NationalCurrency();
            newOne.setShortName(newValue.getName());
            newOne.setChanged(new Date());
            newOne.setDisabled(false);
            newOne.setOrder(nationalCurrencyRepository.getLastOrder());
            currency = nationalCurrencyRepository.save(newOne);
        }

        Bank bank = bankRepository.getByDisplayName(newValue.getBank());
        if (bank==null){
            Bank newBank = new Bank();
            newBank.setDisplayName(newValue.getBank());
            newBank.setChanged(new Date());
            newBank.setDisabled(false);
            bank = bankRepository.save(newBank);
        }

        CurrencyActionType actionType;
        try {
            actionType = CurrencyActionType.valueOf(newValue.getAction());
        } catch (IllegalArgumentException e) {
            throw new WrongIncomingDataException("Unknown action: "+newValue.getAction());
        }
        Boolean allowed = newValue.getAllowed();

        BigDecimal value;
        if (newValue.getValue()==null || newValue.getValue().isEmpty()) {
            Optional<CurrencyValue> valueOptional = currencyValueRepository.getByTypeAndBankAndSellingValueAndDisabledAndOperationAllowed(currency, bank, actionType, false, true).stream().min(dateComparator);
            if (valueOptional.isPresent()){
                value = valueOptional.get().getValue();
            } else {
                value = new BigDecimal(0);
            }
        } else if (!newValue.getValue().equals(StaticMessages.EMPTY_VALUE)){
                value = new BigDecimal(newValue.getValue());
            } else {
                value = new BigDecimal(0);
            }

        try {
            List<CurrencyValue> previousList = currencyValueRepository.getByTypeAndBankAndSellingValueAndDisabled(currency, bank, actionType, false);

            for (CurrencyValue currentPrevious : previousList) {
                currentPrevious.setDisabled(true);
            }

            CurrencyValue toPersist = new CurrencyValue();
            toPersist.setBank(bank);
            if (value.intValue()==0){
                toPersist.setOperationAllowed(false);
            } else {
                toPersist.setOperationAllowed(allowed);
            }
            toPersist.setDisabled(false);
            toPersist.setSellingValue(actionType);
            toPersist.setType(currency);
            toPersist.setValue(value);
            toPersist.setChanged(new Date());

            //historyService.persistHistory(null, toPersist);
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
    }


    @Override
    public List<CurrencyDto> changeSpecificCurrencyAllowanceByBank(String bankName, String shortName, String action, Boolean allow, boolean delete) throws WrongIncomingDataException {
        Bank bank = bankRepository.getByDisplayName(bankName);
        if (bank==null){
            throw new WrongIncomingDataException("Can't find bank with name: "+bankName);
        }

        NationalCurrency currency = nationalCurrencyRepository.getByShortName(shortName);

        CurrencyActionType actionType = null;
        if (action!=null) {
            try {
                actionType = CurrencyActionType.valueOf(action);
            } catch (IllegalArgumentException e) {
                throw new WrongIncomingDataException("Unknown action: "+action);
            }
        }

        List<CurrencyValue> toProcessList;
        if (currency!=null){
            if (actionType!=null){
                toProcessList = currencyValueRepository.getByTypeAndBankAndSellingValueAndDisabled(currency, bank, actionType, false);
            } else {
                toProcessList = currencyValueRepository.getByTypeAndBankAndDisabled(currency, bank, false);
            }
        } else {
            if (actionType!=null){
                toProcessList = currencyValueRepository.getByBankAndSellingValueAndDisabled(bank, actionType, false);
            } else {
                toProcessList = currencyValueRepository.getByBankAndDisabled(bank, false);
            }
        }

        if (delete) {
            toProcessList.stream().filter(x-> !x.getDisabled()).forEach(x->x.setDisabled(true));
        } else if (allow!=null && allow) {
            toProcessList.stream().filter(x-> !x.getOperationAllowed()).forEach(x->x.setOperationAllowed(true));
        } else if (allow!=null && !allow) {
            toProcessList.stream().filter(CurrencyValue::getOperationAllowed).forEach(x->x.setOperationAllowed(false));
        }

        currencyValueRepository.saveAll(toProcessList);

        return toProcessList.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<CurrencyValue> getBestPrices() {
        return null;
    }

    private CurrencyDto convert(CurrencyValue value) {
        return new CurrencyDto(value);
    }
}
