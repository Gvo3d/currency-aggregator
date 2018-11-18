package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"valueInstance", "author"})
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class HistoryAction extends IdentifiedEntity<Long>{
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "currency_id")
    private CurrencyValue valueInstance;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private User author;
    private Date date;
    @Enumerated(EnumType.STRING)
    private HistoryActionType actionType;
    private String previousValue;
    private String newValue;
}
