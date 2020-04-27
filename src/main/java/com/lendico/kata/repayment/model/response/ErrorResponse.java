package com.lendico.kata.repayment.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private HttpStatus httpStatus;
    private List<String> details;
}
