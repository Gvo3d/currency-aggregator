package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class CurrencyValue extends IdentifiedEntity<Long>{
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private NationalCurrency type;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Bank bank;
    @Digits(integer=7, fraction=2)
    private BigDecimal value;
    @Enumerated(EnumType.STRING)
    private CurrencyActionType sellingValue;
    private Boolean operationAllowed;
}
