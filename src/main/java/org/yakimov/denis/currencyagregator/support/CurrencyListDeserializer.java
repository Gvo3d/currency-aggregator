package org.yakimov.denis.currencyagregator.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.yakimov.denis.currencyagregator.dto.CurrencyDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CurrencyListDeserializer extends StdDeserializer<List<CurrencyDto>> {

    public CurrencyListDeserializer() {
        this(null);
    }

    public CurrencyListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<CurrencyDto> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        List<CurrencyDto> resultList = new ArrayList<>();

        JsonNode node = jp.getCodec().readTree(jp);
        Iterator<JsonNode> iterator = node.elements();
        while (iterator.hasNext()){
            JsonNode current = iterator.next();
            String value =current.get("value").asText();
            String name = current.get("name").asText();
            String action = current.get("action").asText();
            Boolean allowed = current.get("allowed").asBoolean();
            resultList.add(new CurrencyDto(name, null, action, value, allowed));
        }
        return resultList;
    }
}