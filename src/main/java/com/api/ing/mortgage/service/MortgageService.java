package com.api.ing.mortgage.service;

import com.api.ing.mortgage.controller.dto.MortgageCheckRequest;
import com.api.ing.mortgage.controller.dto.MortgageCheckResponse;
import com.api.ing.mortgage.exception.MortgageException;
import com.api.ing.mortgage.exception.TechnicalException;
import com.api.ing.mortgage.model.MortgageRate;
import com.api.ing.mortgage.repository.MortgageRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MortgageService {

    private final MortgageRateRepository mortgageRateRepository;

    public MortgageCheckResponse checkMortgage(MortgageCheckRequest mortgageCheckRequest) {
        log.info("Start mortgage check");

        MortgageRate mortgageRateResponse = mortgageRateRepository.findByMaturityPeriod(mortgageCheckRequest.maturityPeriod());

        if (mortgageRateResponse == null) {
            throw new MortgageException("Mortgage rate not found");
        }

        try {
            BigDecimal loanAmount = mortgageCheckRequest.loanValue();
            BigDecimal interestRate = mortgageRateResponse.getInterestRate().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal maturityPeriod = BigDecimal.valueOf(mortgageCheckRequest.maturityPeriod());
            BigDecimal months = BigDecimal.valueOf(mortgageCheckRequest.maturityPeriod() * 12L);

            BigDecimal yearlyInterestValue = loanAmount.multiply(interestRate);
            BigDecimal mortgageValue = yearlyInterestValue.multiply(maturityPeriod).add(loanAmount);
            BigDecimal monthlyValue = mortgageValue.divide(months, 2, RoundingMode.HALF_UP);

            boolean feasible = checkIncome(mortgageCheckRequest.income(), mortgageValue) && mortgageValue.compareTo(mortgageCheckRequest.homeValue()) <= 0;

            log.info("Mortgage check finished");

            return new MortgageCheckResponse(feasible, monthlyValue);
        } catch (Exception e) {
            log.error("Error while checking mortgage", e);
            throw new TechnicalException("Technical error on checking mortgage " + e.getMessage());
        }
    }

    private boolean checkIncome(BigDecimal income, BigDecimal loanValue) {
        BigDecimal incomeX4 = income.multiply(BigDecimal.valueOf(4));
        return loanValue.compareTo(incomeX4) <= 0;
    }

    public List<MortgageRate> findAllMortgageRates() {
        return mortgageRateRepository.findAll();
    }
}
