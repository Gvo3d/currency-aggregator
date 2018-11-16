package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class HistoryAction extends IdentifiedEntity<Long>{
    private CurrencyValue valueInstance;
    private User author;
    private Date date;
    @Enumerated(EnumType.STRING)
    private HistoryActionType actionType;
    private String previousValue;
    private String newValue;
}
