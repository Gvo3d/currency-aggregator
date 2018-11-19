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

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private ICurrencyService currencyService;


    @CacheResult(cacheName = "values")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity getSpecificCurrency(@RequestParam("type") String currencyName,
                                                               @RequestParam("buying") boolean isBuying,
                                                               @RequestParam("ascend") boolean ascendByPrice){
        try {
            return new ResponseEntity<>(currencyService.getSpecificCurrency(currencyName, isBuying, ascendByPrice), HttpStatus.OK);
        } catch (WrongIncomingDataException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @CacheRemoveAll(cacheName = "values")
    @PutMapping(consumes={MediaType.APPLICATION_JSON_UTF8_VALUE},  produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity changeCurrencyAvailability(@RequestBody CurrencyDto incoming,
                                              @RequestParam(value="delete", required = false) Boolean delete){
        String bankName = incoming.getBank();
        String currencyName = incoming.getName();
        String action = incoming.getAction();
        Boolean allow = incoming.getAllowed();
        if (allow == null && (delete == null || !delete)){
            return new ResponseEntity<>(StaticMessages.NO_FLAGS, HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(currencyService.changeSpecificCurrencyAllowanceByBank(bankName, currencyName, action, allow, delete), HttpStatus.OK);
        } catch (WrongIncomingDataException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @CacheRemoveAll(cacheName = "values")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity addSpecificCurrency(@RequestBody CurrencyDto incoming){
        try {
            return new ResponseEntity<>(currencyService.persistCurrency(incoming), HttpStatus.CREATED);
        } catch (WrongIncomingDataException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
