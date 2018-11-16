package org.yakimov.denis.currencyagregator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.yakimov.denis.currencyagregator.support.JacksonMappingMarker;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @JsonView(JacksonMappingMarker.Private.class)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "author")
    List<HistoryAction> historyList = new ArrayList<>();
}
