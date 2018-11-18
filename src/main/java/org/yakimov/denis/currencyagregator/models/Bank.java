package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = "currencyValueList")
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class Bank extends IdentifiedEntity<Integer> {
    private String displayName;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "bank")
    List<CurrencyValue> currencyValueList = new ArrayList<>();
}
