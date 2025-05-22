package com.api.exchangerate.util.fileparser;

import com.api.exchangerate.exception.BaseException;
import com.api.exchangerate.exception.constants.ErrorCode;
import com.api.exchangerate.model.request.BulkConvertRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BulkCsvConvertParserTest {

    private BulkCsvConvertParser parser;

    @BeforeEach
    void setUp() {
        parser = new BulkCsvConvertParser();
    }

    @Test
    void testParse_validCsv_shouldReturnRows() {
        String csv = "source,target,amount\nUSD,TRY,100.00\nEUR,USD,200.50";
        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());

        List<BulkConvertRow> result = parser.parse(inputStream);

        assertEquals(2, result.size());

        assertEquals("USD", result.get(0).source());
        assertEquals("TRY", result.get(0).target());
        assertEquals(new BigDecimal("100.00"), result.get(0).amount());
    }

    @Test
    void testParse_invalidRowFormat_shouldThrowException() {
        String csv = "source,target,amount\nUSD,TRY"; // eksik sütun
        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());

        BaseException e = assertThrows(BaseException.class, () -> parser.parse(inputStream));
        assertEquals(ErrorCode.INVALID_CSV_ROW_FORMAT, e.getCode());
    }

    @Test
    void testParse_invalidAmount_shouldThrowException() {
        String csv = "source,target,amount\nUSD,TRY,abc"; // amount geçersiz
        InputStream inputStream = new ByteArrayInputStream(csv.getBytes());

        BaseException e = assertThrows(BaseException.class, () -> parser.parse(inputStream));
        assertEquals(ErrorCode.INVALID_CSV_ROW_FORMAT, e.getCode());
        assertTrue(e.getMessage().contains("USD,TRY,abc"));
    }

    @Test
    void testGetParserType_shouldReturnCorrectType() {
        assertEquals("bulkCsvConvert", parser.getParserType());
    }
}

