package calculator;

import java.util.*;
import java.util.concurrent.DelayQueue;

import static java.lang.Character.isDigit;

public class Calculation {
    private static String infixToPostFix(String equation) throws IllegalArgumentException {
        String postFix = "";
        Deque<Character> operatorStack = new ArrayDeque<>();
        for (int i = 0; i < equation.length(); i++) {
            char currentChar = equation.charAt(i);
            if (isDigit(currentChar)) {
                postFix += currentChar;
                // If the next character is also a digit, keep appending to the current number
                while (i + 1 < equation.length() && isDigit(equation.charAt(i + 1))) {
                    i++;
                    postFix += equation.charAt(i);
                }
                postFix += " ";
            } else if (currentChar == '(') {
                operatorStack.push(currentChar);
            } else if (currentChar == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    postFix += operatorStack.pop() + " ";
                }
                if (operatorStack.isEmpty()) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
                operatorStack.pop();
            } else {
                while (!operatorStack.isEmpty() && precedence(currentChar) <= precedence(operatorStack.peek())) {
                    postFix += operatorStack.pop() + " ";
                }
                operatorStack.push(currentChar);
            }
        }
        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() == '(') {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            postFix += operatorStack.pop() + " ";
        }
        return postFix.trim();
    }

    public static Double evaluate(String equation) {
        List<String> postFix = Arrays.asList(infixToPostFix(equation).split(" "));
        Deque<Double> numberStack = new ArrayDeque<>();
        for (int i = 0; i < postFix.size(); i++) {
            String currentToken = postFix.get(i);
            if (postFix.get(i).isEmpty()) {
                continue;
            }
            if (isNumeric(currentToken)) {
                numberStack.push(Double.parseDouble(currentToken));
            } else {
                Double number2 = numberStack.pop();
                Double number1 = numberStack.pop();
                switch (currentToken) {
                    case "+" -> numberStack.push(number1 + number2);
                    case "-" -> numberStack.push(number1 - number2);
                    case "x" -> numberStack.push(number1 * number2);
                    case "/" -> numberStack.push(number1 / number2);
                    default -> throw new IllegalArgumentException("Invalid operator: " + currentToken);
                }
            }
        }
        Double result = numberStack.peek();
        return result;
    }

    private static boolean isNumeric(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private static int precedence(char character) {
        return switch (character) {
            case '+', '-' -> 1;
            case 'x', '/' -> 2;
            case '^' -> 3;
            default -> -1;
        };
    }
}
