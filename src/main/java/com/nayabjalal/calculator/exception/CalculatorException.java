package com.nayabjalal.calculator.exception;

import com.nayabjalal.calculator.config.AppConfig;

public class CalculatorException extends Exception {
    private final ErrorType errorType;

    public enum ErrorType {
        DIVISION_BY_ZERO,
        INVALID_INPUT,
        OVERFLOW,
        UNDERFLOW,
        INVALID_OPERATION,
        EXPRESSION_PARSE_ERROR
    }

    public CalculatorException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public CalculatorException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getLocalizedMessage() {
        return AppConfig.getInstance().get("errors." + errorType.name().toLowerCase().replace("_", ""));
    }
}