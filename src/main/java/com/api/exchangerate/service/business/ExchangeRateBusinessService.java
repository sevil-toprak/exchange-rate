package com.api.exchangerate.service.business;

import com.api.exchangerate.exception.BaseException;
import com.api.exchangerate.exception.constants.ErrorCode;
import com.api.exchangerate.exception.constants.ErrorMessages;
import com.api.exchangerate.model.enums.HistoryType;
import com.api.exchangerate.model.request.BulkConvertRow;
import com.api.exchangerate.model.request.ConvertRateRequest;
import com.api.exchangerate.model.request.CreateExchangeRateHistoryRequest;
import com.api.exchangerate.model.request.ExchangeRateRequest;
import com.api.exchangerate.model.response.BulkConvertRateResponse;
import com.api.exchangerate.model.response.ConvertRateResponse;
import com.api.exchangerate.model.response.ExchangeRateResponse;
import com.api.exchangerate.service.spec.ExchangeRateHistoryService;
import com.api.exchangerate.service.spec.ExchangeRateService;
import com.api.exchangerate.util.fileparser.FileParser;
import com.api.exchangerate.util.fileparser.FileParserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateBusinessService {
    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateHistoryService historyService;
    private final FileParserContext<BulkConvertRow> fileParserContext;

    private static final String PARSER_TYPE = "bulkCsvConvert";

    public ExchangeRateResponse exchangeCurrency(ExchangeRateRequest request) {
        return exchangeRateService.exchangeCurrency(request);
    }

    public ConvertRateResponse convertCurrency(ConvertRateRequest request) {
        ConvertRateResponse response = exchangeRateService.convertCurrency(request);
        generateExchangeRateHistory(response);
        return response;
    }

    public BulkConvertRateResponse bulkConvertCurrency(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BaseException(ErrorCode.FILE_IS_EMPTY, ErrorMessages.FILE_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }

        // It should be called Async method, and it should be used chunk/buffer input for processing.
        // It can be added like a createFileParserContext method, or it can be added ParserEnum file
        FileParser<BulkConvertRow> parser = fileParserContext.getParserByType(PARSER_TYPE);

        try (var input = file.getInputStream()) {
            List<BulkConvertRow> rows = parser.parse(input);

            List<ConvertRateRequest> convertRateRequestList = buildBuildRequest(rows);
            return exchangeRateService.bulkConvertCurrency(convertRateRequestList);
        } catch (IOException | RuntimeException e) {
            log.error("An error occurred while getting the CSV file.", e);
            throw new BaseException(ErrorCode.INVALID_CSV_FILE, ErrorMessages.INVALID_CSV_FILE, HttpStatus.BAD_REQUEST);
        }
    }

    private List<ConvertRateRequest> buildBuildRequest(List<BulkConvertRow> rows) {
        List<ConvertRateRequest> convertRateRequestList = new ArrayList<>();
        for (BulkConvertRow row : rows) {
            ConvertRateRequest request = ConvertRateRequest.builder()
                    .source(row.source())
                    .target(row.target())
                    .amount(row.amount())
                    .build();

            convertRateRequestList.add(request);
        }
        return convertRateRequestList;
    }

    private void generateExchangeRateHistory(ConvertRateResponse response) {
        CreateExchangeRateHistoryRequest historyRequest = new CreateExchangeRateHistoryRequest(
                response.transactionId(),
                response.source(),
                response.target(),
                response.amount(),
                response.calculatedAmount(),
                HistoryType.CONVERT);
        historyService.createExchangeHistory(historyRequest);
        log.info("Exchange History has been created successfully.");
    }
}
