package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"currencyValueList"})
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class NationalCurrency extends IdentifiedEntity<Integer>{
    @Column(unique = true)
    private String shortName;
    @Column(name="order_sequence")
    private Integer order;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "type")
    List<CurrencyValue> currencyValueList = new ArrayList<>();
}
