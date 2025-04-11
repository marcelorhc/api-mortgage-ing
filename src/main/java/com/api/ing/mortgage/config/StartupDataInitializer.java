package com.api.ing.mortgage.config;

import com.api.ing.mortgage.model.MortgageRate;
import com.api.ing.mortgage.repository.MortgageRateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class StartupDataInitializer {

    private final MortgageRateRepository repository;

    @PostConstruct
    public void loadInitialData() {
        repository.save(new MortgageRate(10, new BigDecimal("2.50"), Timestamp.from(Instant.now())));
        repository.save(new MortgageRate(15, new BigDecimal("3.00"), Timestamp.from(Instant.now())));
        repository.save(new MortgageRate(20, new BigDecimal("3.50"), Timestamp.from(Instant.now())));
        repository.save(new MortgageRate(25, new BigDecimal("4.00"), Timestamp.from(Instant.now())));
        repository.save(new MortgageRate(30, new BigDecimal("4.50"), Timestamp.from(Instant.now())));
    }
}
