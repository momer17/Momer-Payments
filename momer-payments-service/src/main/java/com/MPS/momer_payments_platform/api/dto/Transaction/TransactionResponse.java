package com.MPS.momer_payments_platform.api.dto.Transaction;

import com.MPS.momer_payments_platform.domain.Enums.Currency;
import com.MPS.momer_payments_platform.domain.Enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID transactionId,
        String paymentReferenceNumber,
        UUID senderAccountId,
        String senderAccountNumber,
        String senderSortCode,
        UUID receiverAccountId,
        String receiverAccountNumber,
        String receiverSortCode,
        BigDecimal amount,
        Currency currency,
        String paymentDescription,
        TransactionStatus transactionStatus,
        Instant createdAt,
        Instant updatedAt
) {
}