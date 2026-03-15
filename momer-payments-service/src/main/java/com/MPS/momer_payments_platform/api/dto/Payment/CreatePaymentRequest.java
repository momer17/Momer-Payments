package com.MPS.momer_payments_platform.api.dto.Payment;

import com.MPS.momer_payments_platform.Domain.Enums.Currency;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentRequest(
        @NotNull UUID senderAccountId,
        @NotBlank @Size(min = 8, max = 8) String receiverAccountNumber,
        @NotBlank @Pattern(regexp = "\\d{2}-\\d{2}-\\d{2}") String receiverSortCode,
        @NotBlank String receiverName,
        @NotBlank Currency currency,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotBlank UUID idempotencyKey,
        @NotBlank String paymentDescription
){}
