package org.yakimov.denis.currencyagregator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yakimov.denis.currencyagregator.dto.CurrencyDto;
import org.yakimov.denis.currencyagregator.services.FileService;
import org.yakimov.denis.currencyagregator.support.WrongIncomingDataException;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;


    @RequestMapping(method= RequestMethod.POST,
            produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
        try {
            List<CurrencyDto> result = fileService.processData(file);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (WrongIncomingDataException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.OK);
        }
    }
}
