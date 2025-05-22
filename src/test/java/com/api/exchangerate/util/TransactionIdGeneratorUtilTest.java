package com.api.exchangerate.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionIdGeneratorUtilTest {

    @Test
    void testGenerateTransactionId_shouldReturnValidUUID() {
        String transactionId = TransactionIdGeneratorUtil.generateTransactionId();

        assertNotNull(transactionId);
        assertFalse(transactionId.isBlank());

        assertDoesNotThrow(() -> UUID.fromString(transactionId));
    }

    @Test
    void testGenerateTransactionId_shouldReturnUniqueValues() {
        String id1 = TransactionIdGeneratorUtil.generateTransactionId();
        String id2 = TransactionIdGeneratorUtil.generateTransactionId();

        assertNotEquals(id1, id2);
    }
}
