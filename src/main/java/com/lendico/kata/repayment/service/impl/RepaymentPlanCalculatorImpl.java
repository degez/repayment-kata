package com.lendico.kata.repayment.service.impl;

import com.lendico.kata.repayment.service.RepaymentPlanCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.ZonedDateTime;

import static java.math.RoundingMode.HALF_UP;

@Service
public class RepaymentPlanCalculatorImpl implements RepaymentPlanCalculator {

    @Value("${repayment.plan.months.to.add:1}")
    private long monthsToAdd;
    @Value("${repayment.plan.days.in.month:30}")
    private BigDecimal daysInMonth;
    @Value("${repayment.plan.days.in.year:360}")
    private BigDecimal daysInYear;
    @Value("${repayment.plan.months.in.year:12}")
    private BigDecimal monthsInYear;
    @Value("${repayment.plan.decimal.scale:2}")
    private Integer scale;
    @Value("${repayment.plan.precision.for.calculation:100}")
    private Integer precisionForCalculation;

    @Override
    public BigDecimal calculateInterest(BigDecimal rate, BigDecimal initialOutstandingPrincipal) {

        return rate.multiply(daysInMonth).multiply(initialOutstandingPrincipal).divide(daysInYear, scale, HALF_UP);
    }

    @Override
    public BigDecimal calculatePrincipal(BigDecimal borrowerPaymentAmount, BigDecimal interest) {

        return borrowerPaymentAmount.subtract(interest);
    }

    /**
     * Calculates the payment amount for the borrower.
     * Uses calculation method explained here:
     * <a href="https://financeformulas.net/Annuity_Payment_Formula.html">https://financeformulas.net/Annuity_Payment_Formula.html</a>
     *
     * @param presentValue is the total loan amount
     * @param ratePerPeriod in our case it is a value for monthly interest rate
     * @param numberOfPeriods total count of the periods
     * @return Borrower payment amount
     */
    @Override
    public BigDecimal calculateBorrowerPaymentAmount(BigDecimal presentValue, BigDecimal ratePerPeriod, Integer numberOfPeriods) {

        // we use precision 100, a high value to calculate more precise
        MathContext mathContext = new MathContext(100);

        return presentValue
                .multiply(ratePerPeriod)
                .divide(BigDecimal.ONE.subtract(BigDecimal.ONE.add(ratePerPeriod)
                        .pow(numberOfPeriods * -1, mathContext)), scale, HALF_UP);
    }

    @Override
    public ZonedDateTime calculateNextPaymentDay(ZonedDateTime paymentDate) {

        return paymentDate.plusMonths(monthsToAdd);
    }

    @Override
    public BigDecimal calculateRatePerPeriod(BigDecimal nominalRate) {
        return nominalRate
                .divide(monthsInYear, 100, RoundingMode.HALF_UP);
    }

}
