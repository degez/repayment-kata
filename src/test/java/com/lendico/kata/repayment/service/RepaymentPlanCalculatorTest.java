package com.lendico.kata.repayment.service;

import com.lendico.kata.repayment.service.impl.RepaymentPlanCalculatorImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = RepaymentPlanCalculatorImpl.class)
public class RepaymentPlanCalculatorTest {

    private static final Integer MONTHS_IN_YEAR = 12;

    @Autowired
    RepaymentPlanCalculator repaymentPlanCalculator;

    @Test
    public void givenNominalRate_whenCalculateRatePeriod_thenGetExpectedRatePerPeriod() {
        // given
        BigDecimal nominalRate = BigDecimal.valueOf(5l).divide(BigDecimal.valueOf(100l));
        BigDecimal expectedRatePerPeriod = nominalRate.divide(BigDecimal.valueOf(MONTHS_IN_YEAR), 100, RoundingMode.HALF_UP);

        // when
        BigDecimal ratePerPeriod = repaymentPlanCalculator.calculateRatePerPeriod(nominalRate);

        // then
        assertEquals(expectedRatePerPeriod, ratePerPeriod);
    }

    @Test
    public void givenValidInput_whenCalculateBorrowerPaymentAmount_thenGetExpectedBorrowerPaymentAmount() {
        // given
        BigDecimal nominalRate = BigDecimal.valueOf(5l).divide(BigDecimal.valueOf(100l));
        BigDecimal ratePerPeriod = nominalRate.divide(BigDecimal.valueOf(MONTHS_IN_YEAR), 100, RoundingMode.HALF_UP);
        BigDecimal loanAmount = BigDecimal.valueOf(5000);
        BigDecimal expectedBorrowerPaymentAmount = new BigDecimal("2515.64");

        Integer duration = 2;

        // when
        BigDecimal borrowerPaymentAmount = repaymentPlanCalculator.calculateBorrowerPaymentAmount(loanAmount, ratePerPeriod, duration);

        // then
        assertEquals(expectedBorrowerPaymentAmount, borrowerPaymentAmount);
    }

    @Test
    public void givenValidInput_whenCalculateInterest_thenGetExpectedInterest() {
        // given
        BigDecimal nominalRate = BigDecimal.valueOf(5l).divide(BigDecimal.valueOf(100l));
        BigDecimal firstInitialOutstandingPrincipal = new BigDecimal("5000");
        BigDecimal expectedInterest = new BigDecimal("20.83");

        // when
        BigDecimal interest = repaymentPlanCalculator.calculateInterest(nominalRate, firstInitialOutstandingPrincipal);

        // then
        assertEquals(expectedInterest, interest);
    }


    @Test
    public void givenValidInput_whenCalculatePrincipal_thenGetExpectedPrincipal() {
        // given
        BigDecimal borrowerPaymentAmount = new BigDecimal("2515.64");
        BigDecimal expectedPrincipal = new BigDecimal("2494.81");
        BigDecimal interest = new BigDecimal("20.83");

        // when
        BigDecimal principal = repaymentPlanCalculator.calculatePrincipal(borrowerPaymentAmount, interest);
        // then
        assertEquals(expectedPrincipal, principal);
    }

    @Test
    public void givenValidDate_whenCalculateNextPaymentDay_thenGetExpectedPaymentDay() {
        // given
        ZonedDateTime paymentDay = ZonedDateTime.parse("2018-01-01T00:00:01Z");
        ZonedDateTime expectedPaymentDay = ZonedDateTime.parse("2018-02-01T00:00:01Z");

        // when
        ZonedDateTime nextPaymentDay = repaymentPlanCalculator.calculateNextPaymentDay(paymentDay);
        // then
        assertEquals(expectedPaymentDay, nextPaymentDay);
    }
}
