package com.lendico.kata.repayment.service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface RepaymentPlanCalculator {

    BigDecimal calculateInterest(BigDecimal rate, BigDecimal initialOutstandingPrincipal);
    BigDecimal calculatePrincipal(BigDecimal borrowerPaymentAmount, BigDecimal interest);
    BigDecimal calculateBorrowerPaymentAmount(BigDecimal presentValue, BigDecimal ratePerPeriod, Integer numberOfPeriods);
    ZonedDateTime calculateNextPaymentDay(ZonedDateTime paymentDate);
    BigDecimal calculateRatePerPeriod(BigDecimal nominalRate);
}
