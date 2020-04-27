package com.lendico.kata.repayment.mapper;

import com.lendico.kata.repayment.model.LoanDetails;
import com.lendico.kata.repayment.model.request.RepaymentPlanRequest;

import java.math.BigDecimal;

public class RepaymentPlanRequestMapper {

    public static LoanDetails mapRepaymentPlanRequest(RepaymentPlanRequest repaymentPlanRequest) {
        return LoanDetails.builder()
                .duration(repaymentPlanRequest.getDuration())
                .loanAmount(repaymentPlanRequest.getLoanAmount())
                .nominalRate(repaymentPlanRequest.getNominalRate()
                        .divide(BigDecimal.valueOf(100l))
                )
                .startDate(repaymentPlanRequest.getStartDate())
                .build();
    }

}
