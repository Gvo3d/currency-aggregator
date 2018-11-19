package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"type", "bank"})
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class CurrencyValue extends IdentifiedEntity<Long>{
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id")
    private NationalCurrency type;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id")
    private Bank bank;
    @Digits(integer=7, fraction=2)
    private BigDecimal value;
    @Enumerated(EnumType.STRING)
    private CurrencyActionType sellingValue;
    private Boolean operationAllowed;

    @Override
    public String toString() {
        return "CurrencyValue{" +
                "type=" + (type!=null?type.getShortName():"null") +
                ", bank=" + (bank!=null?bank.getDisplayName():"null") +
                ", value=" + value +
                ", sellingValue=" + (sellingValue!=null?sellingValue.toString():"null") +
                ", operationAllowed=" + operationAllowed +
                ", id=" + id +
                ", changed=" + changed +
                ", disabled=" + disabled +
                '}';
    }
}
