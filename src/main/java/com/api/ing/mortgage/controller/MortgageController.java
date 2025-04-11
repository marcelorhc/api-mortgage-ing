package com.api.ing.mortgage.controller;

import com.api.ing.mortgage.controller.dto.ErrorResponse;
import com.api.ing.mortgage.controller.dto.MortgageCheckRequest;
import com.api.ing.mortgage.controller.dto.MortgageCheckResponse;
import com.api.ing.mortgage.controller.dto.MortgageRateResponse;
import com.api.ing.mortgage.mapper.MortgageRateMapper;
import com.api.ing.mortgage.service.MortgageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MortgageController {

    private final MortgageService mortgageService;

    @GetMapping("/interest-rates")
    @Operation(summary = "Get all current mortgage interest rates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of interest rates")
    })
    public List<MortgageRateResponse> getInterestRate() {
        return mortgageService.findAllMortgageRates()
                .stream()
                .map(MortgageRateMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/mortgage-check")
    @Operation(summary = "Check if a mortgage is feasible and calculate monthly cost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated mortgage feasibility and monthly payment"),
            @ApiResponse(responseCode = "400", description = "Missing fields on request or invalid maturity level",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Unknown server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public MortgageCheckResponse checkMortgage(@RequestBody @Valid MortgageCheckRequest mortgageCheckRequest) {
        return mortgageService.checkMortgage(mortgageCheckRequest);
    }
}
