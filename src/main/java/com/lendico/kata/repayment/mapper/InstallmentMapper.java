package com.lendico.kata.repayment.mapper;

import com.lendico.kata.repayment.model.Installment;
import com.lendico.kata.repayment.model.dto.InstallmentDto;

public class InstallmentMapper {

    public static InstallmentDto mapInstallment(Installment installment) {
        return InstallmentDto.builder()
                .borrowerPaymentAmount(installment.getBorrowerPaymentAmount())
                .date(installment.getDate())
                .initialOutstandingPrincipal(installment.getInitialOutstandingPrincipal())
                .interest(installment.getInterest())
                .principal(installment.getPrincipal())
                .remainingOutstandingPrincipal(installment.getRemainingOutstandingPrincipal())
                .build();
    }
}
