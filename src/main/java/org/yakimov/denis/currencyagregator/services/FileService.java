package org.yakimov.denis.currencyagregator.services;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yakimov.denis.currencyagregator.dto.CurrencyDto;
import org.yakimov.denis.currencyagregator.support.CurrencyListDeserializer;
import org.yakimov.denis.currencyagregator.support.WrongIncomingDataException;

import javax.annotation.PostConstruct;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);
    private final SimpleModule module = new SimpleModule();
    private final CsvSchema schema = CsvSchema.emptySchema().withHeader();

    @Autowired
    private ICurrencyService currencyService;


    @PostConstruct
    private void init() {
        module.addDeserializer(List.class, new CurrencyListDeserializer());
    }


    public List<CurrencyDto> processData(MultipartFile file) throws WrongIncomingDataException {
        String[] fileNameSections;
        String bankName;
        String format;
        Extension extension;

        try {
            fileNameSections = file.getOriginalFilename().split("\\.");
            bankName = fileNameSections[0];
            format = fileNameSections[fileNameSections.length - 1].toUpperCase();
            extension = Extension.valueOf(format);
        } catch (RuntimeException e) {
            throw new WrongIncomingDataException("Unknown error: " + e.getLocalizedMessage());
        }

        List<CurrencyDto> valueList;
        try {
            switch (extension) {
                case CSV:
                    ObjectMapper mapper = new CsvMapper();
                    MappingIterator<CurrencyDto> it = mapper.readerFor(CurrencyDto.class).with(schema)
                            .readValues(file.getBytes());
                    valueList = it.readAll();
                    break;
                case JSON:
                    valueList = read(new ObjectMapper(), new String(file.getBytes()));
                    break;
                case XML:
                    String xml = new String(file.getBytes());
                    JSONObject jObject = XML.toJSONObject(xml);
                    valueList = read(new ObjectMapper(), jObject.toString());
                    break;
                default:
                    throw new WrongIncomingDataException("Unknown format: " + format);
            }
        } catch (IOException e) {
            throw new WrongIncomingDataException("Unknown error: " + e.getLocalizedMessage());
        }

        List<CurrencyDto> result = new ArrayList<>();
        for (CurrencyDto current : valueList) {
            current.setBank(bankName);
            result.add(currencyService.persistCurrency(current));
        }
        return result;
    }


    private List<CurrencyDto> read(ObjectMapper mapper, String data) throws IOException {
        List<CurrencyDto> result;
        mapper.registerModule(module);
        result = mapper.readValue(data, List.class);
        return result;
    }


    private enum Extension {CSV, JSON, XML}
}
