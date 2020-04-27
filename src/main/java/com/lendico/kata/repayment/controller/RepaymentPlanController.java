package com.lendico.kata.repayment.controller;

import com.lendico.kata.repayment.exception.InvalidDurationException;
import com.lendico.kata.repayment.mapper.InstallmentMapper;
import com.lendico.kata.repayment.mapper.RepaymentPlanRequestMapper;
import com.lendico.kata.repayment.model.Installment;
import com.lendico.kata.repayment.model.dto.InstallmentDto;
import com.lendico.kata.repayment.model.request.RepaymentPlanRequest;
import com.lendico.kata.repayment.service.RepaymentPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller object responsible for handling requests for repayment plans.
 */
@Slf4j
@RestController
public class RepaymentPlanController {

    @Autowired
    RepaymentPlanService repaymentPlanService;

    @Value("${repayment.plan.max.duration:60}")
    private Integer maxLoanDuration;

    /**
     * Handles POST requests for generating repayment plans.
     * RepaymentPlanRequest needs to be valid
     * @see RepaymentPlanRequest
     *
     * Throws runtime exception when max loan duration is more than 60.
     *
     * @param repaymentPlanRequest
     * @return List of installments for the repayment plan
     */
    @PostMapping("/generate-plan")
    public ResponseEntity<List<InstallmentDto>> generatePlan(@Valid @RequestBody RepaymentPlanRequest repaymentPlanRequest) {

        log.debug("new generate plan request: {}", repaymentPlanRequest);

        if(repaymentPlanRequest.getDuration() > maxLoanDuration){
            log.error("duration is greater than max allowed for RepaymentPlanRequest: {}", repaymentPlanRequest);
            throw new InvalidDurationException();
        }

        List<Installment> installments = repaymentPlanService
                .generateRepaymentPlan(RepaymentPlanRequestMapper.mapRepaymentPlanRequest(repaymentPlanRequest));

        List<InstallmentDto> installmentDtoList = installments
                .stream()
                .map(InstallmentMapper::mapInstallment)
                .collect(Collectors.toList());

        log.trace("installment DTO list to be send: {}", installmentDtoList);
        return ResponseEntity.ok(installmentDtoList);
    }
}
