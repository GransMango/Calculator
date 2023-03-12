package calculator;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Character.isDigit;

public class Calculator extends JFrame {
    private final List<JButton> numberButtons;
    private final List<JButton> operandButtons;
    private final List<JButton> functionalButtons;
    private final JLabel resultLabel;
    private final JLabel equationLabel;
    private int openParantheses = 0;
    private boolean isNegated = false;
    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        setSize(300, 500);
        setLocationRelativeTo(null);
        numberButtons = createNumbers();
        operandButtons = createOperands();
        resultLabel = createResultField();
        equationLabel = createEquationField();
        functionalButtons = createFunctionalButtons();
        numberButtons.forEach(this::add);
        operandButtons.forEach(this::add);
        functionalButtons.forEach(this::add);
        add(resultLabel);
        add(equationLabel);
        createAdvancedOperators();
        createAction();
    }

    private List<JButton> createNumbers() {
        List<JButton> buttonList = new ArrayList<>();
        int x = 150;
        int y = 180;
        int width = 50;
        int height = 50;
        String[] numberNames = {"Nine", "Eight", "Seven", "Six", "Five", "Four", "Three", "Two", "One", "Zero"};
        for (int i = 9; i >= 0; i--) {
            JButton button = new JButton();
            button.setName(numberNames[9-i]);
            button.setText(String.valueOf(i));
            button.setBounds(x, y, width, height);
            stylizeButton(button);
            buttonList.add(button);
            x -= 60;
            if (x < 30) {
                x = 150;
                y += 60;
            }
            if (y > 310) {
                x = 90;
            }
        }
        return buttonList;
    }

    private List<JButton> createOperands() {
        List<JButton> buttonList = new ArrayList<>();
        int x = 210;
        int y = 120;
        int width = 50;
        int height = 50;
        char[] operands = {'÷', '×', '+', '-'};
        String[] operandNames = {"Divide", "Multiply", "Add", "Subtract"};
        for (int i = 0; i < 4; i++) {
            JButton button = new JButton();
            button.setName(operandNames[i]);
            button.setText(String.valueOf(operands[i]));
            button.setBounds(x, y, width, height);
            stylizeButton(button);
            buttonList.add(button);
            y += 60;
        }

        JButton button = new JButton();
        button.setName("Equals");
        button.setText("=");
        stylizeButton(button);
        button.setBounds(x, y, width, height);
        buttonList.add(button);

        return buttonList;
    }

    private List<JButton> createFunctionalButtons() {
        List<JButton> functionalButtonList = new ArrayList<>();

        int x = 210;
        int y = 60;
        int width = 50;
        int height = 50;

        JButton deleteButton = new JButton();
        deleteButton.setName("Delete");
        deleteButton.setText("Del");
        deleteButton.setBounds(x, y, width, height);
        stylizeButton(deleteButton);
        functionalButtonList.add(deleteButton);

        JButton clearButton = new JButton();
        clearButton.setName("Clear");
        clearButton.setText("C");
        clearButton.setBounds(x-60, y, width, height);
        stylizeButton(clearButton);
        functionalButtonList.add(clearButton);

        JButton dotButton = new JButton();
        dotButton.setName("Dot");
        dotButton.setText("");
        dotButton.setBounds(x-60, 360, width, height);
        stylizeButton(dotButton);
        functionalButtonList.add(dotButton);

        dotButton.addActionListener(e -> {
            String content = equationLabel.getText();
            if (!content.isEmpty()) {
                if (!isDigit(content.charAt(content.length() - 1))) {
                    content += "0" + dotButton.getText();
                } else {
                    content += dotButton.getText();
                }
            } else {
                content += dotButton.getText();
            }
            equationLabel.setText(content);
        });

        deleteButton.addActionListener(e -> {
            if (equationLabel.getText().length() > 0) {
                equationLabel.setText(equationLabel.getText().substring(0, equationLabel.getText().length() - 1));
            }
        });

        clearButton.addActionListener(e -> {
            resultLabel.setText("");
            equationLabel.setText("");
            equationLabel.setForeground(Color.GRAY);
            openParantheses = 0;
        });
        return functionalButtonList;
    }

    private void createAdvancedOperators() {
        int x = 30;
        int y = 60;
        int width = 50;
        int height = 50;

        JButton parentheses = new JButton();
        parentheses.setText("()");
        parentheses.setName("Parentheses");
        parentheses.setBounds(x, y, width, height);
        stylizeButton(parentheses);
        parentheses.addActionListener(e -> {
            String content = equationLabel.getText();
            if (openParantheses == 0 || isOperator(content.charAt(content.length() - 1))
                || content.charAt(content.length() - 1) == '(') {
                equationLabel.setText(equationLabel.getText() + "(");
                openParantheses++;
            } else {
                equationLabel.setText(equationLabel.getText() + ")");
                openParantheses--;
            }
        });
        add(parentheses);


        JButton powerTwo = new JButton();
        powerTwo.setText("x²");
        powerTwo.setName("PowerTwo");
        powerTwo.setBounds(x, y+60, width, height);
        stylizeButton(powerTwo);
        powerTwo.addActionListener(e -> {
            String content = fixInvalidDot();
            if (!content.isEmpty()) {
                if (isClearToInsertOperator(content)) {
                    content += "^" + "(2)";
                    equationLabel.setText(content);
                } else if (isOperator(content.charAt(content.length()-1))) {
                    equationLabel.setText(content.substring(0, content.length() - 1) + "^" + "(2)");
                }
            }
        });
        add(powerTwo);

        JButton power = new JButton();
        power.setText("xⁿ");
        power.setName("PowerY");
        power.setBounds(x+60, y+60, width, height);
        stylizeButton(power);
        power.addActionListener(e -> {
            String content = fixInvalidDot();
            if (!content.isEmpty()) {
                if (isClearToInsertOperator(content)) {
                    content += "^" + "(";
                    equationLabel.setText(content);
                    openParantheses++;
                } else if (isOperator(content.charAt(content.length()-1))) {
                    equationLabel.setText(content.substring(0, content.length() - 1) + "^" + "(");
                    openParantheses++;
                }
            }
        });
        add(power);

        JButton squared = new JButton();
        squared.setText("√");
        squared.setName("SquareRoot");
        squared.setBounds(x+120, y+60, width, height);
        stylizeButton(squared);
        squared.addActionListener(e -> {
            String content = fixInvalidDot();
            if (!content.isEmpty()) {
                if (isOperator(content.charAt(content.length()-1))) {
                    content += "√" + "(";
                    equationLabel.setText(content);
                    openParantheses++;
                } else if (!isClearToInsertOperator(content)) {
                    equationLabel.setText(content.substring(0, content.length() - 1) + "√" + "(");
                    openParantheses++;
                }
            } else {
                content += "√" + "(";
                equationLabel.setText(content);
                openParantheses++;
            }
        });
        add(squared);

        JButton plusminus = new JButton();
        plusminus.setText("±");
        plusminus.setName("PlusMinus");
        plusminus.setBounds(x, y+300, width, height);
        stylizeButton(plusminus);
        plusminus.addActionListener(e -> {
            String content = fixInvalidDot();
            if (content.length() >= 2) {
                if (content.substring(0, 2).equals("(-")) {
                    equationLabel.setText(content.replace("(-", ""));
                    openParantheses--;
                    isNegated = false;
                } else {
                    equationLabel.setText("(-" + content);
                    openParantheses++;
                    isNegated = true;
                }
            } else {
                equationLabel.setText("(-" + content);
                openParantheses++;
                isNegated = true;
            }
        });
        add(plusminus);

        JButton funButton = new JButton();
        funButton.setText("\uD83C\uDF89");
        funButton.setName("FunButton");
        funButton.setBounds(x+60, y, width, height);
        stylizeButton(funButton);
        funButton.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 24));
        funButton.addActionListener(e -> {
            equationLabel.setText("🙏💻🔢😊");
            resultLabel.setText("Enjoy the calculator");
        });
        add(funButton);
    }

    private JLabel createResultField() {
        JLabel textField = new JLabel();
        textField.setName("ResultLabel");
        textField.setFont(new Font("Segoe UI Symbol", Font.BOLD, 20));
        textField.setBounds(25, 10, 240, 30);
        textField.setText("0");
        return textField;
    }

    private JLabel createEquationField() {
        JLabel textField = new JLabel();
        textField.setName("EquationLabel");
        textField.setFont(new Font("Segoe UI Symbol", Font.BOLD, 12));
        textField.setForeground(Color.GRAY);
        textField.setBounds(25, 30, 240, 30);
        return textField;
    }

    private void createAction() {
        for (JButton button : numberButtons) {
            button.addActionListener(e -> {
                String content = equationLabel.getText();
                content += button.getText();
                equationLabel.setText(content);
            });
        }
        for (JButton button : operandButtons) {
            if (!button.getActionCommand().equals("=")) {
                button.addActionListener(e -> {
                    String content = fixInvalidDot();
                    if (!content.isEmpty()) {
                        if (isClearToInsertOperator(content)) {
                            content += button.getText();
                            equationLabel.setText(content);
                        } else if (isOperator(content.charAt(content.length()-1))) {
                            equationLabel.setText(content.substring(0, content.length() - 1) + button.getText());
                        }
                    }
                });
            } else {
                button.addActionListener(e -> {
                    String content = equationLabel.getText();
                    if (isValidExpression()) {
                        if (isNegated) {
                            content = Calculation.formatNegatedExpression(content);
                        }
                        content = String.valueOf(Calculation.evaluate(content));
                        resultLabel.setText(content);

                    } else {
                        equationLabel.setForeground(Color.RED.darker());
                    }
                });
            }
        }
    }

    private boolean isOperator(char character) {
        char[] operands = {'÷', '×', '+', '-'};
        for (char operand : operands) {
            if (character == operand) {
                return true;
            }
        }
        return false;
    }

    private boolean divisionByZero() {
        String equation = equationLabel.getText();
        char previousChar = 'a';
        for (char character : equation.toCharArray()) {
            if(character == '0' && previousChar == '÷') {
                return true;
            }
            previousChar = character;
        }
        return false;
    }

    private String fixInvalidDot() {
        String content = equationLabel.getText();
        content = fixDotMissingNumber(content);
        content = fixDotMissingNumberBehind(content);
        return content;
    }

    private String fixDotMissingNumber(String content) {
        String fixedContent = "";
        char previousCharacter = 'a';
        for (char character : content.toCharArray()) {
            if ((!isDigit(previousCharacter) && character == '.')) {
                fixedContent += "0" + String.valueOf(character);
            } else {
                fixedContent += String.valueOf(character);
            }
            previousCharacter = character;
        }
        return fixedContent;
    }

    private String fixDotMissingNumberBehind(String content) {
        if (!content.isEmpty()) {
            if (content.charAt(content.length() - 1) == '.') {
                content += "0";
            }
        }
        return content;
    }

    private void stylizeButton(JButton button) {
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setBackground(Color.white.darker());
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
    }

    private boolean isClearToInsertOperator(String content) {
        int index = content.length() - 1;
        if (isDigit(content.charAt(index))) {
            return true;
        }
        if ((content.charAt(index) == ')')) {
            return true;
        }
        return false;
    }

    private boolean isValidExpression() {
        String content = equationLabel.getText();
        if (!isClearToInsertOperator(content)) {
            return false;
        }
        if (divisionByZero()) {
            return false;
        }
        if (openParantheses > 0) {
            return false;
        }
        return true;
    }


}
