package com.lendico.kata.repayment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Builder
@ToString
public class LoanDetails {

    private BigDecimal loanAmount;
    private BigDecimal nominalRate;
    private Integer duration;
    private ZonedDateTime startDate;
}
