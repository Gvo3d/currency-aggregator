package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class CurrencyValue extends IdentifiedEntity<Long>{
    private NationalCurrency type;
    private Bank bank;
    @Digits(integer=7, fraction=2)
    private BigDecimal value;
    @Enumerated(EnumType.STRING)
    private CurrencyActionType sellingValue;
    private boolean operationAllowed;
}
