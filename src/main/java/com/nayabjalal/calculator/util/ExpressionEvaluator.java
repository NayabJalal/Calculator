package com.nayabjalal.calculator.util;

import com.nayabjalal.calculator.exception.CalculatorException;
import java.util.Stack;
import java.util.StringTokenizer;

public class ExpressionEvaluator {

    public static double evaluateExpression(String expression) throws CalculatorException {
        if (expression == null || expression.trim().isEmpty()) {
            throw new CalculatorException(
                    CalculatorException.ErrorType.INVALID_INPUT,
                    "Expression cannot be empty"
            );
        }

        try {
            return evaluate(expression.replaceAll("\\s+", ""));
        } catch (Exception e) {
            throw new CalculatorException(
                    CalculatorException.ErrorType.EXPRESSION_PARSE_ERROR,
                    "Failed to parse expression: " + expression,
                    e
            );
        }
    }

    private static double evaluate(String expression) throws CalculatorException {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                StringBuilder number = new StringBuilder();
                while (i < expression.length() &&
                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i++));
                }
                i--; // Back up one
                numbers.push(Double.parseDouble(number.toString()));
            }
            else if (c == '(') {
                operators.push(c);
            }
            else if (c == ')') {
                while (operators.peek() != '(') {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.pop(); // Remove '('
            }
            else if (isOperator(c)) {
                while (!operators.empty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        while (!operators.empty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^';
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/' || op1 == '%') && (op2 == '+' || op2 == '-')) return false;
        if (op1 == '^' && (op2 == '+' || op2 == '-' || op2 == '*' || op2 == '/' || op2 == '%')) return false;
        return true;
    }

    private static double applyOperation(char op, double b, double a) throws CalculatorException {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) {
                    throw new CalculatorException(
                            CalculatorException.ErrorType.DIVISION_BY_ZERO,
                            "Cannot divide by zero"
                    );
                }
                return a / b;
            case '%': return a % b;
            case '^': return Math.pow(a, b);
            default:
                throw new CalculatorException(
                        CalculatorException.ErrorType.INVALID_OPERATION,
                        "Invalid operator: " + op
                );
        }
    }
}