package com.lendico.kata.repayment.exception;

public class InvalidDurationException extends RuntimeException{

    public static final String MAXIMUM_DURATION_MESSAGE = "Maximum allowed loan duration is 60";

    public InvalidDurationException() {
        super(MAXIMUM_DURATION_MESSAGE);
    }
}
