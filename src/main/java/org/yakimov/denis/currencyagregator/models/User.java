package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.yakimov.denis.currencyagregator.support.JacksonMappingMarker;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class User extends IdentifiedEntity<Long>{
    @JsonView(JacksonMappingMarker.Public.class)
    private String name;
    @JsonView(JacksonMappingMarker.Private.class)
    @Column(unique =  true, updatable = false, nullable = false)
    private String login;
    @JsonView(JacksonMappingMarker.Private.class)
    private String password;
    @JsonView(JacksonMappingMarker.Public.class)
    @Enumerated(EnumType.STRING)
    private Group group;
}
