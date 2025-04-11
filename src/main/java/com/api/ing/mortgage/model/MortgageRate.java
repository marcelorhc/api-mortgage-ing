package com.api.ing.mortgage.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "mortgage_rates")
@Data
@NoArgsConstructor
public class MortgageRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int maturityPeriod;
    private BigDecimal interestRate;
    private Timestamp lastUpdate;

    public MortgageRate(int maturityPeriod, BigDecimal interestRate, Timestamp lastUpdate) {
        this.maturityPeriod = maturityPeriod;
        this.interestRate = interestRate;
        this.lastUpdate = lastUpdate;
    }
}
