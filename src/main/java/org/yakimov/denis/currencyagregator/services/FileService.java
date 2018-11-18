package org.yakimov.denis.currencyagregator.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yakimov.denis.currencyagregator.dto.CurrencyDto;
import org.yakimov.denis.currencyagregator.support.CurrencyListDeserializer;
import org.yakimov.denis.currencyagregator.support.WrongIncomingDataException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    private SimpleModule module = new SimpleModule();

    @Autowired
    private ICurrencyService currencyService;


    @PostConstruct
    private void init() {
        module.addDeserializer(List.class, new CurrencyListDeserializer());
    }


    public List<CurrencyDto> processData(MultipartFile file) throws WrongIncomingDataException {
        String[] fileNameSections = file.getOriginalFilename().split(".");
        String bankName = fileNameSections[fileNameSections.length - 2];
        String format = fileNameSections[fileNameSections.length - 1].toUpperCase();

        Extension extension = Extension.valueOf(format);
        List<CurrencyDto> valueList;

        ObjectMapper mapper;
        switch (extension) {
            case CSV:
                mapper = new CsvMapper();
                break;
            case JSON:
                mapper = new ObjectMapper();
                break;
            case XML:
                mapper = new XmlMapper();
                break;
            default:
                throw new WrongIncomingDataException("Unknown format: " + format);
        }

        try {
            List<CurrencyDto> result = new ArrayList<>();
            mapper.registerModule(module);
            valueList = mapper.readValue(file.getBytes(), List.class);
            for (CurrencyDto current: valueList){
                current.setBank(bankName);
                result.add(currencyService.persistCurrency(current));
            }
            return result;
        } catch (IOException | WrongIncomingDataException e) {
            throw new WrongIncomingDataException(e.getLocalizedMessage());
        }
    }


    private enum Extension {CSV, JSON, XML}
}
