package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@MappedSuperclass
abstract class IdentifiedEntity<T> {
    T id;
    Date changed;
    boolean disabled;
}
