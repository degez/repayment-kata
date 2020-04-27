package com.lendico.kata.repayment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lendico.kata.repayment.mapper.RepaymentPlanRequestMapper;
import com.lendico.kata.repayment.model.Installment;
import com.lendico.kata.repayment.model.request.RepaymentPlanRequest;
import com.lendico.kata.repayment.service.RepaymentPlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class RepaymentPlanControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RepaymentPlanController repaymentPlanController;

    @MockBean
    RepaymentPlanService repaymentPlanService;

    @Test
    public void whenRepaymentPlanController_thenNotNull() {
        assertThat(repaymentPlanController).isNotNull();
    }

    @Test
    public void givenRepaymentPlanRequest_whenGenerate_thenValidResponse() throws Exception {

        // given
        RepaymentPlanRequest repaymentPlanRequest = RepaymentPlanRequest.builder()
                .duration(2)
                .loanAmount(BigDecimal.valueOf(5000l))
                .nominalRate(BigDecimal.valueOf(2l))
                .startDate(ZonedDateTime.parse("2018-01-01T00:00:01Z"))
                .build();

        List<Installment> installments = populateInstallmentList();

        when(repaymentPlanService.generateRepaymentPlan(RepaymentPlanRequestMapper
                .mapRepaymentPlanRequest(repaymentPlanRequest)))
                .thenReturn(installments);


        // when perform generate then
        mockMvc.perform(MockMvcRequestBuilders.post("/generate-plan")
                .content(objectMapper.writeValueAsString(repaymentPlanRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void givenInvalidNominalRateRepaymentPlanRequest_whenGenerate_thenBadRequestResponse() throws Exception {

        // given
        RepaymentPlanRequest repaymentPlanRequest = RepaymentPlanRequest.builder()
                .duration(2)
                .loanAmount(BigDecimal.valueOf(5000l))
                .nominalRate(BigDecimal.valueOf(0l))
                .startDate(ZonedDateTime.parse("2018-01-01T00:00:01Z"))
                .build();

        // when perform generate then
        mockMvc.perform(MockMvcRequestBuilders.post("/generate-plan")
                .content(objectMapper.writeValueAsString(repaymentPlanRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void givenInvalidDurationRepaymentPlanRequest_whenGenerate_thenBadRequestResponse() throws Exception {

        // given
        RepaymentPlanRequest repaymentPlanRequest = RepaymentPlanRequest.builder()
                .duration(0)
                .loanAmount(BigDecimal.valueOf(5000l))
                .nominalRate(BigDecimal.valueOf(2l))
                .startDate(ZonedDateTime.parse("2018-01-01T00:00:01Z"))
                .build();

        // when perform generate then
        mockMvc.perform(MockMvcRequestBuilders.post("/generate-plan")
                .content(objectMapper.writeValueAsString(repaymentPlanRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void givenInvalidLoanAmountRepaymentPlanRequest_whenGenerate_thenBadRequestResponse() throws Exception {

        // given
        RepaymentPlanRequest repaymentPlanRequest = RepaymentPlanRequest.builder()
                .duration(2)
                .loanAmount(BigDecimal.valueOf(0l))
                .nominalRate(BigDecimal.valueOf(2l))
                .startDate(ZonedDateTime.parse("2018-01-01T00:00:01Z"))
                .build();

        // when perform generate then
        mockMvc.perform(MockMvcRequestBuilders.post("/generate-plan")
                .content(objectMapper.writeValueAsString(repaymentPlanRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void givenInvalidStartDateRepaymentPlanRequest_whenGenerate_thenBadRequestResponse() throws Exception {

        // given
        RepaymentPlanRequest repaymentPlanRequest = RepaymentPlanRequest.builder()
                .duration(2)
                .loanAmount(BigDecimal.valueOf(0l))
                .nominalRate(BigDecimal.valueOf(2l))
                .startDate(null)
                .build();

        // when perform generate then
        mockMvc.perform(MockMvcRequestBuilders.post("/generate-plan")
                .content(objectMapper.writeValueAsString(repaymentPlanRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void givenExceedMaxDurationRepaymentPlanRequest_whenGenerate_thenBadRequestResponse() throws Exception {

        // given
        RepaymentPlanRequest repaymentPlanRequest = RepaymentPlanRequest.builder()
                .duration(61)
                .loanAmount(BigDecimal.valueOf(20l))
                .nominalRate(BigDecimal.valueOf(2l))
                .startDate(ZonedDateTime.parse("2018-01-01T00:00:01Z"))
                .build();

        // when perform generate then
        mockMvc.perform(MockMvcRequestBuilders.post("/generate-plan")
                .content(objectMapper.writeValueAsString(repaymentPlanRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

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
