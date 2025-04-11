package com.api.ing.mortgage.controller;

import com.api.ing.mortgage.controller.dto.MortgageCheckRequest;
import com.api.ing.mortgage.model.MortgageRate;
import com.api.ing.mortgage.repository.MortgageRateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MortgageControllerTest {

    @Autowired
    private MortgageRateRepository mortgageRateRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080/api";

        mortgageRateRepository.deleteAll();
        mortgageRateRepository.save(new MortgageRate(10, new BigDecimal("2.50"), Timestamp.from(Instant.now())));
        mortgageRateRepository.save(new MortgageRate(15, new BigDecimal("3.00"), Timestamp.from(Instant.now())));
        mortgageRateRepository.save(new MortgageRate(20, new BigDecimal("3.50"), Timestamp.from(Instant.now())));
        mortgageRateRepository.save(new MortgageRate(25, new BigDecimal("4.00"), Timestamp.from(Instant.now())));
        mortgageRateRepository.save(new MortgageRate(30, new BigDecimal("4.50"), Timestamp.from(Instant.now())));
    }

    @Test
    public void testGetInterestRates() {
        get("/interest-rates")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].maturityPeriod", notNullValue())
                .body("[0].interestRate", notNullValue());
    }

    @Test
    public void testMortgageCheckFeasibility() throws JsonProcessingException {
        MortgageCheckRequest requestPayload = new MortgageCheckRequest(
                new BigDecimal("100000.00"),
                20,
                new BigDecimal("200000.00"),
                new BigDecimal("400000.00"));

        String jsonPayload = objectMapper.writeValueAsString(requestPayload);

        given()
                .contentType("application/json")
                .body(jsonPayload)
                .when()
                .post("/mortgage-check")
                .then()
                .statusCode(200)
                .body("feasible", equalTo(true))
                .body("monthlyCosts", greaterThan(0f));
    }

    @Test
    public void testMortgageCheckFeasibilityNegative() throws JsonProcessingException {
        MortgageCheckRequest requestPayload = new MortgageCheckRequest(
                new BigDecimal("50000.00"),
                20,
                new BigDecimal("200000.00"),
                new BigDecimal("400000.00"));

        String jsonPayload = objectMapper.writeValueAsString(requestPayload);

        given()
                .contentType("application/json")
                .body(jsonPayload)
                .when()
                .post("/mortgage-check")
                .then()
                .statusCode(200)
                .body("feasible", equalTo(false))
                .body("monthlyCosts", greaterThan(0f));
    }

    @Test
    public void testMortgageCheckFeasibilityWithoutMortgageRate() throws JsonProcessingException {
        mortgageRateRepository.deleteAll();

        MortgageCheckRequest requestPayload = new MortgageCheckRequest(
                new BigDecimal("50000.00"),
                20,
                new BigDecimal("200000.00"),
                new BigDecimal("400000.00"));

        String jsonPayload = objectMapper.writeValueAsString(requestPayload);

        given()
                .contentType("application/json")
                .body(jsonPayload)
                .when()
                .post("/mortgage-check")
                .then()
                .statusCode(400)
                .body("message", equalTo("Mortgage rate not found"));
    }

    @Test
    public void testIncomeFieldNull() {
        MortgageCheckRequest mortgageCheckRequest = new MortgageCheckRequest(
                null,
                20,
                new BigDecimal("200000.00"),
                new BigDecimal("250000.00")
        );

        given()
                .contentType("application/json")
                .body(mortgageCheckRequest)
                .when()
                .post("/mortgage-check")
                .then()
                .statusCode(400)
                .body("message", equalTo("Income cannot be null"));
    }

    @Test
    public void testMaturityPeriodFieldNull() {
        MortgageCheckRequest mortgageCheckRequest = new MortgageCheckRequest(
                new BigDecimal("5000.00"),
                null,
                new BigDecimal("200000.00"),
                new BigDecimal("250000.00")
        );

        given()
                .contentType("application/json")
                .body(mortgageCheckRequest)
                .when()
                .post("/mortgage-check")
                .then()
                .statusCode(400)
                .body("message", equalTo("Maturity period cannot be null"));
    }

    @Test
    public void testLoanValueFieldNull() {
        MortgageCheckRequest mortgageCheckRequest = new MortgageCheckRequest(
                new BigDecimal("5000.00"),
                20,
                null,
                new BigDecimal("250000.00")
        );

        given()
                .contentType("application/json")
                .body(mortgageCheckRequest)
                .when()
                .post("/mortgage-check")
                .then()
                .statusCode(400)
                .body("message", equalTo("Loan value cannot be null"));
    }

    @Test
    public void testHomeValueFieldNull() {
        MortgageCheckRequest mortgageCheckRequest = new MortgageCheckRequest(
                new BigDecimal("5000.00"),
                20,
                new BigDecimal("200000.00"),
                null
        );

        given()
                .contentType("application/json")
                .body(mortgageCheckRequest)
                .when()
                .post("/mortgage-check")
                .then()
                .statusCode(400)
                .body("message", equalTo("Home value cannot be null"));
    }

}
