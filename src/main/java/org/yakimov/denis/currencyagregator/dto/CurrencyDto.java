package org.yakimov.denis.currencyagregator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class CurrencyDto {
    private String name;
    private String bank;
    private String action;
    private String value;
    private Boolean allowed;

    public CurrencyDto(CurrencyValue value) {
        this.name = value.getType().getShortName();
        this.bank = value.getBank().getDisplayName();
        this.action = value.getSellingValue().name();
        this.value = value.getValue().toPlainString();
        this.allowed = value.getOperationAllowed();
    }

    public CurrencyDto(String name, String bank, String action, String value, Boolean allowed) {
        this.name = name;
        this.bank = bank;
        this.action = action;
        this.value = value;
        this.allowed = allowed;
    }

    public CurrencyDto() {
    }
}
