package com.api.exchangerate.service.business;

import com.api.exchangerate.exception.BaseException;
import com.api.exchangerate.exception.constants.ErrorCode;
import com.api.exchangerate.exception.constants.ErrorMessages;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateBusinessServiceTest {

    @InjectMocks
    private ExchangeRateBusinessService businessService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRateHistoryService exchangeRateHistoryService;

    @Mock
    private FileParserContext<BulkConvertRow> fileParserContext;

    @Mock
    private FileParser<BulkConvertRow> fileParser;

    @Test
    void testConvertCurrency_shouldCallServiceAndGenerateHistory() {
        // Given
        ConvertRateRequest request = ConvertRateRequest.builder()
                .source("USD").target("EUR").amount(BigDecimal.valueOf(0.8825)).build();

        String transactionId = UUID.randomUUID().toString();
        ConvertRateResponse response = new ConvertRateResponse(
                transactionId,
                Currency.getInstance("USD"),
                Currency.getInstance("EUR"),
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(88.25));

        when(exchangeRateService.convertCurrency(request)).thenReturn(response);

        // When
        ConvertRateResponse result = businessService.convertCurrency(request);

        // Then
        assertEquals(transactionId, result.transactionId());
        verify(exchangeRateHistoryService).createExchangeHistory(any(CreateExchangeRateHistoryRequest.class));
    }

    @Test
    void testBulkConvertCurrency_shouldReturnResponse() {
        // Given
        String csv = "USD,EUR,100";
        MockMultipartFile file = new MockMultipartFile("file", "bulk.csv", "text/csv", csv.getBytes());

        BulkConvertRow row = new BulkConvertRow("USD", "EUR", BigDecimal.valueOf(0.8825));

        BulkConvertRateResponse mockResponse = new BulkConvertRateResponse(List.of(), 10, 1, 1);

        when(fileParserContext.getParserByType("bulkCsvConvert")).thenReturn(fileParser);
        when(fileParser.parse(any())).thenReturn(List.of(row));
        when(exchangeRateService.bulkConvertCurrency(anyList())).thenReturn(mockResponse);

        // When
        BulkConvertRateResponse result = businessService.bulkConvertCurrency(file);

        // Then
        assertNotNull(result);
        verify(fileParser).parse(any());
        verify(exchangeRateService).bulkConvertCurrency(anyList());
    }

    @Test
    void testBulkConvertCurrency_withEmptyFile_shouldThrowException() {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.csv", "text/csv", new byte[0]);

        // When
        BaseException exception = assertThrows(BaseException.class,
                () -> businessService.bulkConvertCurrency(emptyFile));

        // Then
        assertEquals(ErrorCode.FILE_IS_EMPTY, exception.getCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void testBulkConvertCurrency_whenIOExceptionThrown_shouldThrowBaseException() throws Exception {
        // Given
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getInputStream()).thenThrow(new IOException("Simulated I/O error"));

        when(fileParserContext.getParserByType("bulkCsvConvert")).thenReturn(fileParser);

        // When
        BaseException exception = assertThrows(BaseException.class, () -> {
            businessService.bulkConvertCurrency(mockFile);
        });

        // Then
        assertEquals(ErrorCode.INVALID_CSV_FILE, exception.getCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorMessages.INVALID_CSV_FILE, exception.getMessage());
    }

    @Test
    void testBulkConvertCurrency_whenRuntimeExceptionThrown_shouldThrowBaseException() {
        // Given
        byte[] dummyCsv = "USD,TRY,100".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("file", "data.csv", "text/csv", dummyCsv);

        when(fileParserContext.getParserByType("bulkCsvConvert")).thenReturn(fileParser);
        when(fileParser.parse(any())).thenThrow(new RuntimeException("Parsing failed"));

        // When
        BaseException exception = assertThrows(BaseException.class, () -> {
            businessService.bulkConvertCurrency(mockFile);
        });

        // Then
        assertEquals(ErrorCode.INVALID_CSV_FILE, exception.getCode());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void testExchangeCurrency_shouldDelegateToService() {
        // Given
        ExchangeRateRequest request = new ExchangeRateRequest("USD", "TRY");
        ExchangeRateResponse expected = new ExchangeRateResponse("USD", "TRY", BigDecimal.valueOf(4.0));

        when(exchangeRateService.exchangeCurrency(request)).thenReturn(expected);

        // When
        ExchangeRateResponse response = businessService.exchangeCurrency(request);

        // Then
        assertEquals(expected, response);
        verify(exchangeRateService).exchangeCurrency(request);
    }
}
