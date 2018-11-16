package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class User extends IdentifiedEntity<Long>{
    private String name;
    private String login;
    private String password;
    private Group group;
}
