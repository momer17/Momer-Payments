package com.MPS.momer_payments_platform.api.dto.Payment;

import com.MPS.momer_payments_platform.Domain.Enums.Currency;
import com.MPS.momer_payments_platform.Domain.Enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentResponse(
        UUID transactionId,
        UUID senderAccountId,
        String senderAccountNumber,
        String senderSortCode,
        UUID receiverAccountId,
        String receiverAccountNumber,
        String receiverSortCode,
        BigDecimal amount,
        Currency currency,
        TransactionStatus status,
        String paymentDescription,
        Instant createdAt,
        Instant updatedAt
) {}