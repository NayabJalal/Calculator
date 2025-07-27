package com.nayabjalal.calculator.util;

import java.util.regex.Pattern;

public class InputValidator {

    private static final String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
    private static final String ZERO_PATTERN = "0+(\\.0+)?";
    private static final String INTEGER_REGEX = "-?\\d+";

    private InputValidator() {
        throw new AssertionError("Utility class - constructor not allowed");
    }

    public static boolean isValidNumber(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(NUMBER_REGEX, input.trim());
    }

    public static boolean isZeroOnly(String input) {
        return input != null && Pattern.matches(ZERO_PATTERN, input.trim());
    }

    public static boolean isInteger(String input) {
        return input != null && Pattern.matches(INTEGER_REGEX, input.trim());
    }

    public static String formatResult(double result) {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return "Error";
        }

        // Check if it's a whole number
        if (result == (long) result) {
            return String.valueOf((long) result);
        }
        return String.valueOf(result);
    }
}