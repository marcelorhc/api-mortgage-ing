package com.api.ing.mortgage.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponse(
        @Schema(description = "Error message", example = "Error message")
        String message
) {
}
