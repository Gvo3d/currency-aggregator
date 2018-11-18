package org.yakimov.denis.currencyagregator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yakimov.denis.currencyagregator.dto.CurrencyDto;
import org.yakimov.denis.currencyagregator.services.ICurrencyService;
import org.yakimov.denis.currencyagregator.support.StaticMessages;
import org.yakimov.denis.currencyagregator.support.WrongIncomingDataException;

import java.util.List;

@RestController
@RequestMapping("/currency/")
public class CurrencyController {

    @Autowired
    private ICurrencyService currencyService;


    @GetMapping(value="specific", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity getSpecificCurrency(@RequestParam("type") String currencyName,
                                                               @RequestParam("buying") boolean isBuying,
                                                               @RequestParam("ascend") boolean ascendByPrice){
        try {
            return new ResponseEntity<List<CurrencyDto>>(currencyService.getSpecificCurrency(currencyName, isBuying, ascendByPrice), HttpStatus.OK);
        } catch (WrongIncomingDataException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity changeCurrencyAvailability(@RequestParam("type") String bankName,
                                              @RequestParam("type") String currencyName,
                                              @RequestParam("action") String action,
                                              @RequestParam(value = "allow", required = false) Boolean allow,
                                              @RequestParam(value="delete", required = false) Boolean delete){
        if (allow == null && (delete == null || !delete)){
            return new ResponseEntity<>(StaticMessages.NO_FLAGS, HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<List<CurrencyDto>>(currencyService.changeSpecificCurrencyAllowanceByBank(bankName, currencyName, action, allow, delete), HttpStatus.OK);
        } catch (WrongIncomingDataException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity addSpecificCurrency(@RequestBody CurrencyDto incoming){
        try {
            return new ResponseEntity<>(currencyService.persistCurrency(incoming), HttpStatus.CREATED);
        } catch (WrongIncomingDataException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
