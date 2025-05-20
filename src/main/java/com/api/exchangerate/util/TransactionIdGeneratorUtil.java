package com.api.exchangerate.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TransactionIdGeneratorUtil {

    public static String generateTransactionId() {
        return UUID.randomUUID().toString(); //version v4
    }
}
