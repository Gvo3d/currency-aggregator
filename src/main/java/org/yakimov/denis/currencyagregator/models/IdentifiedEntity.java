package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@MappedSuperclass
abstract class IdentifiedEntity<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    T id;
    @Temporal(TemporalType.TIMESTAMP)
    Date changed;
    Boolean disabled;
}
