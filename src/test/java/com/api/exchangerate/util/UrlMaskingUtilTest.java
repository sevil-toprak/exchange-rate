package com.api.exchangerate.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlMaskingUtilTest {

    @Test
    void testMaskAccessKey_shouldMaskKeyInUrl() {
        String originalUrl = "https://testprojecturl/api/exchange-rate/convert?from=USD&to=TRY&amount=1&access_key=secret123";
        String masked = UrlMaskingUtil.maskAccessKey(originalUrl);

        assertTrue(masked.contains("access_key=****"));
        assertFalse(masked.contains("secret123"));
    }

    @Test
    void testMaskAccessKey_shouldReturnSameUrlWhenNoKeyPresent() {
        String urlWithoutKey = "https://testprojecturl/api/exchange-rate/convert?from=USD&to=TRY";
        String result = UrlMaskingUtil.maskAccessKey(urlWithoutKey);

        assertEquals(urlWithoutKey, result);
    }

    @Test
    void testMaskAccessKey_shouldHandleNullInput() {
        assertNull(null);
    }

    @Test
    void testMaskAccessKey_shouldMaskKeyInMiddleOfUrl() {
        String url = "https://example.com/api?access_key=abcd1234&other=value";
        String result = UrlMaskingUtil.maskAccessKey(url);

        assertEquals("https://example.com/api?access_key=****&other=value", result);
    }
}
