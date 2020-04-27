package com.lendico.kata.repayment.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class InstallmentDto {

    private BigDecimal borrowerPaymentAmount;
    private ZonedDateTime date;
    private BigDecimal initialOutstandingPrincipal;
    private BigDecimal interest;
    private BigDecimal principal;
    private BigDecimal remainingOutstandingPrincipal;
}
