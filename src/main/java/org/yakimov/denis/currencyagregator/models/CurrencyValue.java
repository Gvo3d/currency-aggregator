package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"type", "bank", "historyList"})
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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "valueInstance")
    List<HistoryAction> historyList = new ArrayList<>();
}
