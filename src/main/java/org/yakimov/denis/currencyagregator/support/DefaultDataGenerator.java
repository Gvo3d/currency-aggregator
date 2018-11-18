package org.yakimov.denis.currencyagregator.support;

import org.yakimov.denis.currencyagregator.models.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultDataGenerator {
    private static final String[] banks = {"Bank 1", "Bank 2", "Bank 3"};
    private static final String[] nationals = {"USD", "GBP", "EUR"};
    private static final String[] logins = {"admin", "client"};
    private static final String[] names = {"admin_name", "client_name"};
    private static final Group[] groups = {Group.ADMIN, Group.CLIENT};
    private static final String password = "pass";
    private Date now = new Date();


    public List<User> generateUsers(){
        List<User> userList = new ArrayList<>(2);

        for (int i=0; i<2; i++){
            User user = new User();
            user.setLogin(logins[i]);
            user.setName(names[i]);
            user.setPassword(password);
            user.setGroup(groups[i]);
            user.setChanged(now);
            user.setDisabled(false);
            userList.add(user);
        }

        return userList;
    }


    public List<CurrencyValue> generateCurrencyData(){
        List<Bank> banksList = generateBanks();
        List<NationalCurrency> nationalsList = generateNationals();

        List<CurrencyValue> values = new ArrayList<>(9);
        BigDecimal random;

        for(Bank bank: banksList){
            for (NationalCurrency national: nationalsList){
                random = generateRandomBigDecimalFromRange(
                        new BigDecimal(0.21).setScale(2, BigDecimal.ROUND_HALF_UP),
                        new BigDecimal(35.28).setScale(2, BigDecimal.ROUND_HALF_UP)
                );
                CurrencyValue value = new CurrencyValue();
                value.setDisabled(false);
                value.setChanged(now);
                value.setValue(random);
                value.setBank(bank);
                bank.getCurrencyValueList().add(value);
                value.setType(national);
               //national.getCurrencyValueList().add(value);
                value.setOperationAllowed(true);
                value.setSellingValue(CurrencyActionType.BUYING);
                values.add(value);

                random = generateRandomBigDecimalFromRange(
                        new BigDecimal(0.21).setScale(2, BigDecimal.ROUND_HALF_UP),
                        new BigDecimal(30.28).setScale(2, BigDecimal.ROUND_HALF_UP)
                );
                value = new CurrencyValue();
                value.setDisabled(false);
                value.setChanged(now);
                value.setValue(random);
                value.setBank(bank);
               // bank.getCurrencyValueList().add(value);
                value.setType(national);
                //national.getCurrencyValueList().add(value);
                value.setOperationAllowed(true);
                value.setSellingValue(CurrencyActionType.SELLING);
                values.add(value);
            }
        }

        return values;
    }


    private List<Bank> generateBanks(){
        List<Bank> banksList = new ArrayList<>(3);
        for(String bankName: banks) {
            Bank bank = new Bank();
            bank.setDisplayName(bankName);
            bank.setChanged(now);
            bank.setDisabled(false);
            banksList.add(bank);
        }

        return banksList;
    }


    private List<NationalCurrency> generateNationals(){
        int order = 0;
        List<NationalCurrency> nationalsList = new ArrayList<>(3);
        for(String nationalName: nationals) {
            NationalCurrency currency = new NationalCurrency();
            currency.setShortName(nationalName);
            currency.setChanged(now);
            currency.setDisabled(false);
            currency.setOrder(order++);
            nationalsList.add(currency);
        }

        return nationalsList;
    }


    private BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max) {
        BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
    }
}
