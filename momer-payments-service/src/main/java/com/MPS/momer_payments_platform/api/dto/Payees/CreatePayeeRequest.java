package com.MPS.momer_payments_platform.api.dto.Payees;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreatePayeeRequest(
        @NotNull UUID ownerAccountId,
        @NotBlank String displayName,
        @NotBlank @Size(min = 8, max = 8) String receiverAccountNumber,
        @NotBlank @Pattern(regexp = "\\d{2}-\\d{2}-\\d{2}") String receiverSortCode
) {}
