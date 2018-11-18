package org.yakimov.denis.currencyagregator.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.yakimov.denis.currencyagregator.dto.CurrencyDto;

import javax.xml.bind.SchemaOutputResolver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        if (node.isArray()) {
            Iterator<JsonNode> iterator = node.elements();
            while (iterator.hasNext()) {
                JsonNode current = iterator.next();
                if (current.isObject()) {
                    resultList.add(getDtoFromNode(current));
                }
            }
        } else if (node.isObject()){
            Iterator<Map.Entry<String, JsonNode>> iterator = root.fields();
            while (iterator.hasNext()) {
                JsonNode xmlObject = iterator.next().getValue();
                if (xmlObject.isArray()){
                    Iterator<JsonNode> xmlIterator = xmlObject.elements();
                    while (xmlIterator.hasNext()) {
                        JsonNode current = xmlIterator.next();
                        if (current.isObject()) {
                            resultList.add(getDtoFromNode(current));
                        }
                    }
                } else {
                    resultList.add(getDtoFromNode(xmlObject));
                }
            }
        }
        return resultList;
    }


    private CurrencyDto getDtoFromNode(JsonNode node){
        String value = node.get("value").asText();
        String name = node.get("name").asText();
        String action = node.get("action").asText();
        Boolean allowed = node.get("allowed").asBoolean();
        return new CurrencyDto(name, null, action, value, allowed);
    }


    private void recursiveWrite(JsonNode node){
        System.out.println("Type: "+node.getNodeType() +" name: "+node.toString());
        System.out.println("Fields: ");
        node.fieldNames().forEachRemaining(System.out::println);
        Iterator<JsonNode> nodes = node.elements();
        System.out.println("Values: ");
        Iterator<Map.Entry<String, JsonNode>> currentValues = node.fields();
        while (currentValues.hasNext()){
            Map.Entry<String, JsonNode> currentNode = currentValues.next();
            System.out.println("key: "+currentNode.getKey()+" value: "+currentNode.getValue());
            recursiveWrite(currentNode.getValue());
        }
        while (nodes.hasNext()){
            JsonNode current = nodes.next();
            recursiveWrite(current);
        }
    }
}