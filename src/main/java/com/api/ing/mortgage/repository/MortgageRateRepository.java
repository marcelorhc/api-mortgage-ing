package com.api.ing.mortgage.repository;

import com.api.ing.mortgage.model.MortgageRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface MortgageRateRepository extends JpaRepository<MortgageRate, Long> {

    MortgageRate findByMaturityPeriod(int maturityPeriod);

}
