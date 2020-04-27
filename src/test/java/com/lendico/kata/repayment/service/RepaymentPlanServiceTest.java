package com.lendico.kata.repayment.service;

import com.lendico.kata.repayment.model.Installment;
import com.lendico.kata.repayment.model.LoanDetails;
import com.lendico.kata.repayment.service.impl.RepaymentPlanServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepaymentPlanServiceTest {

    private static final String NOMINAL_RATE_PER_PERIOD = "0.0041667";

    @Mock
    RepaymentPlanCalculator repaymentPlanCalculator;

    @InjectMocks
    RepaymentPlanServiceImpl repaymentPlanService;

    @Test
    public void givenValidLoanDetails_whenGenerate_thenReturnListOfInstallments() {

        // given
        BigDecimal ratePerPeriod = new BigDecimal(NOMINAL_RATE_PER_PERIOD);
        BigDecimal borrowerPaymentAmount = new BigDecimal("2515.64");
        BigDecimal firstInitialOutstandingPrincipal = new BigDecimal("5000");
        BigDecimal secondInitialOutstandingPrincipal = new BigDecimal("2505.19");
        BigDecimal firstInstallmentInterest = new BigDecimal("20.83");
        BigDecimal secondInstallmentInterest = new BigDecimal("10.44");
        BigDecimal firstInstallmentPrincipal = new BigDecimal("2494.81");
        BigDecimal secondInstallmentPrincipal = new BigDecimal("2505.19");


        LoanDetails loanDetails = LoanDetails.builder()
                .duration(2)
                .loanAmount(BigDecimal.valueOf(5000l))
                .nominalRate(BigDecimal.valueOf(5l).divide(BigDecimal.valueOf(100l)))
                .startDate(ZonedDateTime.parse("2018-01-01T00:00:01Z"))
                .build();

        when(repaymentPlanCalculator.calculateRatePerPeriod(loanDetails.getNominalRate()))
                .thenReturn(ratePerPeriod);

        when(repaymentPlanCalculator
                .calculateBorrowerPaymentAmount(loanDetails.getLoanAmount(), ratePerPeriod, loanDetails.getDuration()))
                .thenReturn(borrowerPaymentAmount);

        when(repaymentPlanCalculator.calculateInterest(loanDetails.getNominalRate(), firstInitialOutstandingPrincipal))
                .thenReturn(firstInstallmentInterest);
        when(repaymentPlanCalculator.calculateInterest(loanDetails.getNominalRate(), secondInitialOutstandingPrincipal))
                .thenReturn(secondInstallmentInterest);

        when(repaymentPlanCalculator.calculatePrincipal(borrowerPaymentAmount, firstInstallmentInterest))
                .thenReturn(firstInstallmentPrincipal);
        when(repaymentPlanCalculator.calculatePrincipal(borrowerPaymentAmount, secondInstallmentInterest))
                .thenReturn(secondInstallmentPrincipal);

        //  when generate repayment
        List<Installment> installments = repaymentPlanService.generateRepaymentPlan(loanDetails);
        List<Installment> expectedInstallments = populateInstallmentList();

        // then
        assertEquals(expectedInstallments.size(), installments.size());
        assertTrue(installments.stream()
                .anyMatch(installment -> installment.getBorrowerPaymentAmount()
                        .compareTo(expectedInstallments.get(0)
                                .getBorrowerPaymentAmount()) == 0));

        assertTrue(installments.stream()
                .anyMatch(installment -> installment.getInitialOutstandingPrincipal()
                        .compareTo(expectedInstallments.get(0)
                                .getInitialOutstandingPrincipal()) == 0));
        assertTrue(installments.stream()
                .anyMatch(installment -> installment.getInitialOutstandingPrincipal()
                        .compareTo(expectedInstallments.get(1)
                                .getInitialOutstandingPrincipal()) == 0));

    }

    public List<Installment> populateInstallmentList() {
        List<Installment> installments = new ArrayList<>();

        installments.add(Installment.builder()
                .remainingOutstandingPrincipal(new BigDecimal("2505.19"))
                .principal(new BigDecimal("2494.81"))
                .interest(new BigDecimal("20.83"))
                .initialOutstandingPrincipal(new BigDecimal("5000"))
                .borrowerPaymentAmount(new BigDecimal("2515.64"))
                .date(ZonedDateTime.parse("2018-01-01T00:00:01Z"))
                .build()
        );

        installments.add(Installment.builder()
                .remainingOutstandingPrincipal(new BigDecimal("0"))
                .principal(new BigDecimal("2505.19"))
                .interest(new BigDecimal("10.44"))
                .initialOutstandingPrincipal(new BigDecimal("2505.19"))
                .borrowerPaymentAmount(new BigDecimal("2515.64"))
                .date(ZonedDateTime.parse("2018-02-01T00:00:01Z"))
                .build()
        );

        return installments;
    }
}
