package com.api.ing.mortgage.mapper;

import com.api.ing.mortgage.controller.dto.MortgageRateResponse;
import com.api.ing.mortgage.model.MortgageRate;

public class MortgageRateMapper {

    public static MortgageRateResponse toResponse(MortgageRate rate) {
        return new MortgageRateResponse(
                rate.getMaturityPeriod(),
                rate.getInterestRate(),
                rate.getLastUpdate()
        );
    }

}
