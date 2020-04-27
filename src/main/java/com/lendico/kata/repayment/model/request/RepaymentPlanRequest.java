package com.lendico.kata.repayment.model.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter(AccessLevel.PRIVATE)
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentPlanRequest {

    @Positive
    @NotNull
    private BigDecimal loanAmount;
    @Positive
    @NotNull
    private BigDecimal nominalRate;
    @Positive
    @NotNull
    private Integer duration;
//    @Future
    @NotNull
    private ZonedDateTime startDate;
}
