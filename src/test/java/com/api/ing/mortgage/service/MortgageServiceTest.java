package com.api.ing.mortgage.service;

import com.api.ing.mortgage.controller.dto.MortgageCheckRequest;
import com.api.ing.mortgage.controller.dto.MortgageCheckResponse;
import com.api.ing.mortgage.exception.MortgageException;
import com.api.ing.mortgage.exception.TechnicalException;
import com.api.ing.mortgage.model.MortgageRate;
import com.api.ing.mortgage.repository.MortgageRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MortgageServiceTest {

    @Mock
    private MortgageRateRepository mortgageRateRepository;

    @InjectMocks
    private MortgageService mortgageService;

    private MortgageCheckRequest validMortgageCheckRequest;
    private MortgageRate validMortgageRate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validMortgageCheckRequest = new MortgageCheckRequest(
                new BigDecimal("500000.00"),
                20,
                new BigDecimal("150000.00"),
                new BigDecimal("400000.00")
        );

        validMortgageRate = new MortgageRate(20, new BigDecimal("3.5"), new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testCheckMortgageSuccessful() {
        when(mortgageRateRepository.findByMaturityPeriod(validMortgageCheckRequest.maturityPeriod()))
                .thenReturn(validMortgageRate);

        MortgageCheckResponse response = mortgageService.checkMortgage(validMortgageCheckRequest);

        assertNotNull(response);
        assertTrue(response.feasible());
        assertNotNull(response.monthlyCosts());
    }

    @Test
    void testCheckMortgageMortgageRateNotFound() {
        when(mortgageRateRepository.findByMaturityPeriod(validMortgageCheckRequest.maturityPeriod()))
                .thenReturn(null);

        MortgageException thrown = assertThrows(MortgageException.class,
                () -> mortgageService.checkMortgage(validMortgageCheckRequest));

        assertEquals("Mortgage rate not found", thrown.getMessage());
    }

    @Test
    void testCheckMortgageTechnicalException() {
        when(mortgageRateRepository.findByMaturityPeriod(validMortgageCheckRequest.maturityPeriod()))
                .thenReturn(validMortgageRate);

        MortgageCheckRequest invalidMortgageCheckRequest = new MortgageCheckRequest(
                null,
                20,
                new BigDecimal("150000.00"),
                new BigDecimal("180000.00")
        );
        TechnicalException thrown = assertThrows(TechnicalException.class,
                () -> mortgageService.checkMortgage(invalidMortgageCheckRequest));

        assertTrue(thrown.getMessage().contains("Technical error on checking mortgage"));
    }

    @Test
    void testFindAllMortgageRates() {
        when(mortgageRateRepository.findAll()).thenReturn(List.of(validMortgageRate));

        var rates = mortgageService.findAllMortgageRates();

        assertNotNull(rates);
        assertFalse(rates.isEmpty());
        assertEquals(1, rates.size());
        assertEquals(validMortgageRate, rates.getFirst());
    }
}
