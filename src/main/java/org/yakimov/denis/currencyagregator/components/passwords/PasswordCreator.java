package org.yakimov.denis.currencyagregator.components.passwords;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class PasswordCreator implements IPassowordGenerator{
    public String generateRandomPassword(){
        return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    }
}
