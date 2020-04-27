package com.lendico.kata.repayment.mapper;

import com.lendico.kata.repayment.model.Installment;
import com.lendico.kata.repayment.model.dto.InstallmentDto;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InstallmentMapperTest {

    @Test
    public void givenInstallment_whenMap_thenGetDto() {
        // given
        Installment installment = Installment.builder()
                .remainingOutstandingPrincipal(new BigDecimal("0"))
                .principal(new BigDecimal("2505.19"))
                .interest(new BigDecimal("10.44"))
                .initialOutstandingPrincipal(new BigDecimal("2505.19"))
                .borrowerPaymentAmount(new BigDecimal("2515.64"))
                .date(ZonedDateTime.parse("2018-02-01T00:00:01Z"))
                .build();

        InstallmentDto expectedInstallmentDto = InstallmentDto.builder()
                .remainingOutstandingPrincipal(new BigDecimal("0"))
                .principal(new BigDecimal("2505.19"))
                .interest(new BigDecimal("10.44"))
                .initialOutstandingPrincipal(new BigDecimal("2505.19"))
                .borrowerPaymentAmount(new BigDecimal("2515.64"))
                .date(ZonedDateTime.parse("2018-02-01T00:00:01Z"))
                .build();

        // when

        InstallmentDto installmentDto = InstallmentMapper.mapInstallment(installment);

        assertEquals(expectedInstallmentDto, installmentDto);
    }
}
