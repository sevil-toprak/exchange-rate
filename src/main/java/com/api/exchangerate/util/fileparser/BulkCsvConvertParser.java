package com.api.exchangerate.util.fileparser;

import com.api.exchangerate.exception.BaseException;
import com.api.exchangerate.exception.constants.ErrorCode;
import com.api.exchangerate.exception.constants.ErrorMessages;
import com.api.exchangerate.model.request.BulkConvertRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class BulkCsvConvertParser implements FileParser<BulkConvertRow> {

    private static final String SPLIT_COMMA = ",";

    @Override
    public List<BulkConvertRow> parse(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .skip(1) // skip header
                    .map(this::parseLine)
                    .toList();
        } catch (IOException e) {
            log.error("CSV parsing failed.", e);
            throw new BaseException(ErrorCode.INVALID_CSV_FILE, ErrorMessages.INVALID_CSV_FILE, HttpStatus.BAD_REQUEST);
        }
    }

    private BulkConvertRow parseLine(String line) {
        try {
            String[] parts = line.split(SPLIT_COMMA);
            if (parts.length != 3) {
                log.error("CSV file contains an incomplete or missing row.");
                throw new BaseException(ErrorCode.INVALID_CSV_ROW_FORMAT, ErrorMessages.INVALID_CSV_ROW_FORMAT, HttpStatus.BAD_REQUEST);
            }

            return new BulkConvertRow(
                    parts[0].trim().toUpperCase(),
                    parts[1].trim().toUpperCase(),
                    new BigDecimal(parts[2].trim())
            );
        } catch (Exception e) {
            log.error("An error occurred while parsing a row in the CSV file.", e);
            throw new BaseException(ErrorCode.INVALID_CSV_ROW_FORMAT, ErrorMessages.INVALID_CSV_ROW_FORMAT + line, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public String getParserType() {
        return "bulkCsvConvert";
    }
}
