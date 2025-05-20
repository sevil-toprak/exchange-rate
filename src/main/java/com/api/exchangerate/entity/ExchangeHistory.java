package com.api.exchangerate.entity;

import com.api.exchangerate.model.enums.HistoryType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Table(name = "exchange_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeHistory extends BaseEntity {

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "source_currency_code", nullable = false)
    private Currency sourceCurrencyCode;

    @Column(name = "target_currency_code", nullable = false)
    private Currency targetCurrencyCode;

    // Precision value can be changed --> in db: DECIMAL(11, 2) --> 123.456.789,01
    @Column(name = "source_amount", nullable = false, precision = 11, scale = 2)
    private BigDecimal sourceAmount;

    @Column(name = "calculated_amount", nullable = false, precision = 11, scale = 2)
    private BigDecimal calculatedAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private HistoryType type;

}
