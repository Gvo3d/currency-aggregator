package org.yakimov.denis.currencyagregator.controllers;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yakimov.denis.currencyagregator.dto.CurrencyDto;
import org.yakimov.denis.currencyagregator.services.FileService;
import org.yakimov.denis.currencyagregator.support.WrongIncomingDataException;
import org.apache.commons.compress.utils.IOUtils;

import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;


    @CacheRemoveAll(cacheName = "values")
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


    @CacheResult(cacheName = "values")
    @GetMapping(produces={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<byte[]> getBetPrices() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            fileService.generateReportPdf(outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            byte[] result = IOUtils.toByteArray(inputStream);

            return ResponseEntity
                    .ok()
                    .contentLength(result.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header("Content-Disposition", "inline; filename=\"best-prices.pdf\"")
                    .body(result);

        } catch (DocumentException | IOException e) {
            return new ResponseEntity(e.getLocalizedMessage(), HttpStatus.OK);
        }
    }


}
