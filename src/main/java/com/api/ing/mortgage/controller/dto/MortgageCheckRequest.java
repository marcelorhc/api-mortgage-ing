package com.api.ing.mortgage.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MortgageCheckRequest(
        @NotNull(message = "Income cannot be null")
        @Schema(description = "User's yearly income", example = "50000.00")
        BigDecimal income,

        @NotNull(message = "Maturity period cannot be null")
        @Schema(description = "Maturity period in years", example = "20")
        Integer maturityPeriod,

        @NotNull(message = "Loan value cannot be null")
        @Schema(description = "Loan amount requested", example = "300000.00")
        BigDecimal loanValue,

        @NotNull(message = "Home value cannot be null")
        @Schema(description = "Home value", example = "400000.00")
        BigDecimal homeValue
) {}
