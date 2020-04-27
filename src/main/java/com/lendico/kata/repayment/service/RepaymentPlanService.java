package com.lendico.kata.repayment.service;

import com.lendico.kata.repayment.model.Installment;
import com.lendico.kata.repayment.model.LoanDetails;

import java.util.List;

public interface RepaymentPlanService {
    List<Installment> generateRepaymentPlan(LoanDetails loanDetails);
}
