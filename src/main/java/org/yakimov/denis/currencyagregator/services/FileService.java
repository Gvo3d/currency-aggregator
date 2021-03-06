package org.yakimov.denis.currencyagregator.services;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yakimov.denis.currencyagregator.dto.CurrencyDto;
import org.yakimov.denis.currencyagregator.models.CurrencyActionType;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;
import org.yakimov.denis.currencyagregator.models.NationalCurrency;
import org.yakimov.denis.currencyagregator.support.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
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
            fileNameSections = file.getOriginalFilename().split(StaticMessages.SPLITTER_REGEX);
            bankName = fileNameSections[0];
            format = fileNameSections[fileNameSections.length - 1].toUpperCase();
            extension = Extension.valueOf(format);
        } catch (RuntimeException e) {
            throw new WrongIncomingDataException(StaticMessages.UNKNOWN_ERROR + e.getLocalizedMessage());
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
                    throw new WrongIncomingDataException(StaticMessages.UNKNOWN_FORMAT + format);
            }
        } catch (IOException e) {
            throw new WrongIncomingDataException(StaticMessages.UNKNOWN_ERROR + e.getLocalizedMessage());
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


    public void generateReportPdf(OutputStream outputStream) throws DocumentException {
        List<CurrencyValue> values = currencyService.getAllData();

        Document document = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        document.open();
        Set<NationalCurrency> currencies = values.stream().map(CurrencyValue::getType).collect(Collectors.toSet());

        CurrencyComparator comparator = new CurrencyComparator(true);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(0f);

        PdfPCell cell = getCell(getPhraze(StaticMessages.CODES, StaticMessages.BLACK_TITLE_FONT), 2);
        cell.setRowspan(2);
        table.addCell(cell);
        table.addCell(getCell(getPhraze(StaticMessages.BUYING, StaticMessages.BLACK_TITLE_FONT), 2));
        table.addCell(getCell(getPhraze(StaticMessages.SELLING, StaticMessages.BLACK_TITLE_FONT), 2));
        table.addCell(getCell(getPhraze(StaticMessages.RATE, StaticMessages.BLACK_TITLE_FONT), 1));
        table.addCell(getCell(getPhraze(StaticMessages.BANK, StaticMessages.BLACK_TITLE_FONT), 1));
        table.addCell(getCell(getPhraze(StaticMessages.RATE, StaticMessages.BLACK_TITLE_FONT), 1));
        table.addCell(getCell(getPhraze(StaticMessages.BANK, StaticMessages.BLACK_TITLE_FONT), 1));

        for (NationalCurrency currentNationalCurrency: currencies){
            table.addCell(getCell(getPhraze(currentNationalCurrency.getShortName(), StaticMessages.STANDARD_BOLD_FONT), 2));
            List<CurrencyValue> currentValues = values.stream()
                    .filter(new DataContainerCheck(currentNationalCurrency.getShortName()))
                    .collect(Collectors.toList());

            Optional<CurrencyValue> opt = currentValues.stream()
                    .filter(x->x.getSellingValue().equals(CurrencyActionType.BUYING))
                    .min(comparator);
            addOptionalCurrentValueToTable(opt.orElse(null), table);

            opt = currentValues.stream()
                    .filter(x->x.getSellingValue().equals(CurrencyActionType.SELLING))
                    .max(comparator);
            addOptionalCurrentValueToTable(opt.orElse(null), table);
        }

        document.add(table);
        document.close();
    }


    private void addOptionalCurrentValueToTable(CurrencyValue value, PdfPTable table) {
        if (value!=null) {
            table.addCell(getCell(getPhraze(value.getValue().toString(), StaticMessages.STANDARD_FONT), 1));
            table.addCell(getCell(getPhraze(value.getBank().getDisplayName(), StaticMessages.STANDARD_BOLD_FONT), 1));
        } else {
            table.addCell(getCell(getPhraze(StaticMessages.NO_VALUE_FOR_PDF, StaticMessages.STANDARD_FONT), 2));
        }
    }


    private PdfPCell getCell(Phrase phrase, int colspan) {
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(Rectangle.BOX);
        cell.setColspan(colspan);
        cell.setPadding(4);
        return cell;
    }


    private Phrase getPhraze(String data, Font font) {
        return new Phrase(data, font);
    }


    private enum Extension {CSV, JSON, XML}
}
