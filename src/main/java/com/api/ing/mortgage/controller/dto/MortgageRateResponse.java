package com.api.ing.mortgage.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.sql.Timestamp;


public record MortgageRateResponse(
        @Schema(description = "The maturity period of the mortgage in years", example = "30")
        Integer maturityPeriod,

        @Schema(description = "The interest rate for this mortgage in percentage", example = "3.5")
        BigDecimal interestRate,

        @Schema(description = "The timestamp when this rate was last updated", example = "2025-04-11T14:30:00Z")
        Timestamp lastUpdate
) {}

