package com.api.exchangerate.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlMaskingUtil {

    private static final String MASK_ACCESS_KEY_REGEX = "access_key=([^&]+)";
    private static final String MASK_ACCESS_KEY_TEXT = "access_key=****";

    public static String maskAccessKey(String url) {
        if (url == null) return null;
        return url.replaceAll(MASK_ACCESS_KEY_REGEX, MASK_ACCESS_KEY_TEXT);
    }
}
