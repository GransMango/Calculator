package calculator;

import java.util.*;

import static java.lang.Character.isDigit;

public class Calculation {
    private static String infixToPostFix(String equation) throws IllegalArgumentException {
        String postFix = "";
        Deque<Character> operatorStack = new ArrayDeque<>();
        for (int i = 0; i < equation.length(); i++) {
            char currentChar = equation.charAt(i);
            if (isDigit(currentChar) || currentChar == '.') {
                postFix += currentChar;
                // If the next character is also a digit, keep appending to the current number
                while (i + 1 < equation.length() && (isDigit(equation.charAt(i + 1)) || equation.charAt(i + 1) == '.')) {
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

    public static Number evaluate(String equation) {
        String[] postFix = infixToPostFix(equation).split(" ");
        Deque<Double> numberStack = new ArrayDeque<>();
        for (String currentToken : postFix) {
            if (currentToken.isEmpty()) {
                continue;
            }
            if (isNumeric(currentToken)) {
                numberStack.push(Double.parseDouble(currentToken));
            } else {
                if (currentToken.equals("√")) {
                    numberStack.push(Math.sqrt(numberStack.pop()));
                } else {
                    Double number2 = numberStack.pop();
                    Double number1 = numberStack.pop();
                    switch (currentToken) {
                        case "+" -> numberStack.push(number1 + number2);
                        case "-" -> numberStack.push(number1 - number2);
                        case "×" -> numberStack.push(number1 * number2);
                        case "÷" -> numberStack.push(number1 / number2);
                        case "^" -> numberStack.push(Math.pow(number1, number2));
                        default -> throw new IllegalArgumentException("Invalid operator: " + currentToken);
                    }
                }
            }
        }
        Double result = numberStack.peek();
        if (isRoundable(result)) {
            return result.intValue();
        }
        return result;
    }

    public static String formatNegatedExpression(String content) {
        StringBuilder fixedcontent = new StringBuilder();
        char[] expressionCharacters = content.toCharArray();
        char previousCharacter = 'a';
        for (char character : expressionCharacters) {
            if (previousCharacter == '(' && character == '-') {
                fixedcontent.append(0);
            }
            fixedcontent.append(character);
            previousCharacter = character;
        }
        System.out.println(fixedcontent);
        return fixedcontent.toString();
    }

    private static boolean isNumeric(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private static boolean isRoundable(Double number) {
        return number % 1 == 0;
    }

    private static int precedence(char character) {
        return switch (character) {
            case '+', '-' -> 1;
            case '×', '÷' -> 2;
            case '^', '√' -> 3;
            default -> -1;
        };
    }
}
