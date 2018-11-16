package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class Bank extends IdentifiedEntity<Integer> {
    private String displayName;
}
