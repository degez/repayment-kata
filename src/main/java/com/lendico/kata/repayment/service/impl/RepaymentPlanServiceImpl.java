package com.lendico.kata.repayment.service.impl;

import com.lendico.kata.repayment.model.Installment;
import com.lendico.kata.repayment.model.LoanDetails;
import com.lendico.kata.repayment.service.RepaymentPlanCalculator;
import com.lendico.kata.repayment.service.RepaymentPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class RepaymentPlanServiceImpl implements RepaymentPlanService {

    @Autowired
    private RepaymentPlanCalculator repaymentPlanCalculator;

    /**
     * Generates the repayment plan using the information provided.
     * Uses RepaymentPlanCalculator for building the repayment plan.
     *
     * @see RepaymentPlanCalculator
     *
     * @see LoanDetails
     * @see Installment
     * @param loanDetails includes the information for generating repayment plan
     * @return list of installments of the repayment plan
     *
     */
    @Override
    public List<Installment> generateRepaymentPlan(LoanDetails loanDetails) {

        log.info("loan details for repayment plan: {}", loanDetails);

        List<Installment> installments = new ArrayList<>();

        BigDecimal presentValue = loanDetails.getLoanAmount();
        BigDecimal ratePerPeriod = repaymentPlanCalculator.calculateRatePerPeriod(loanDetails.getNominalRate());
        ZonedDateTime tempPaymentDate = loanDetails.getStartDate();
        Integer duration = loanDetails.getDuration();
        BigDecimal borrowerPaymentAmount = repaymentPlanCalculator
                .calculateBorrowerPaymentAmount(presentValue, ratePerPeriod, duration);

        for (int i = 0; i < duration; i++) {

            BigDecimal interest = repaymentPlanCalculator.calculateInterest(loanDetails.getNominalRate(), presentValue);
            BigDecimal principal = repaymentPlanCalculator.calculatePrincipal(borrowerPaymentAmount, interest);

            if (principal.compareTo(presentValue) > 0) {
                principal = presentValue;
            }

            BigDecimal remainingOutstandingPrincipal = presentValue.subtract(principal);
            ZonedDateTime nextPaymentDay = tempPaymentDate;

            Installment installment = Installment.builder()
                    .borrowerPaymentAmount(borrowerPaymentAmount)
                    .date(nextPaymentDay)
                    .initialOutstandingPrincipal(presentValue)
                    .interest(interest)
                    .remainingOutstandingPrincipal(remainingOutstandingPrincipal)
                    .principal(principal)
                    .build();

            installments.add(installment);

            presentValue = remainingOutstandingPrincipal;
            tempPaymentDate = repaymentPlanCalculator.calculateNextPaymentDay(tempPaymentDate);
        }

        log.debug("prepared installment list for the payment plan: {}", installments);
        return installments;
    }
}
