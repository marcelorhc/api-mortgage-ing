package com.api.ing.mortgage.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record MortgageCheckResponse(
        @Schema(description = "Whether the mortgage is feasible or not", example = "true")
        boolean feasible,

        @Schema(description = "The calculated monthly mortgage payment", example = "1500.50")
        BigDecimal monthlyCosts
) {
}
