package com.MPS.momer_payments_platform.api.dto.Account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
        @NotBlank @Size(min = 8, max = 8) String accountNumber,
        @NotBlank @Pattern(regexp = "\\d{2}-\\d{2}-\\d{2}") String sortCode,
        @NotBlank String accountName
        ) {}


