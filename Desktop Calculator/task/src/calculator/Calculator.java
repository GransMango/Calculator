package calculator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Calculator extends JFrame {
    private List<JButton> numberButtons;
    private List<JButton> operandButtons;
    private JTextField equationTextField;
    private StringBuilder currentInput;
    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setSize(300, 400);
        setLocationRelativeTo(null);
        numberButtons = createNumbers();
        operandButtons = createOperands();
        equationTextField = createTextField();
        numberButtons.forEach(this::add);
        operandButtons.forEach(this::add);
        add(equationTextField);
        createAction();
    }

    private List<JButton> createNumbers() {
        List<JButton> buttonList = new ArrayList<>();
        int x = 150;
        int y = 80;
        int width = 50;
        int height = 50;
        String[] numberNames = {"Nine", "Eight", "Seven", "Six", "Five", "Four", "Three", "Two", "One", "Zero"};
        for (int i = 9; i >= 0; i--) {
            JButton button = new JButton();
            button.setName(numberNames[i]);
            button.setText(String.valueOf(i));
            button.setBounds(x, y, width, height);
            buttonList.add(button);
            x -= 60;
            if (x < 30) {
                x = 150;
                y += 60;
            }
            if (y > 210) {
                x = 90;
            }
        }
        return buttonList;
    }

    private List<JButton> createOperands() {
        List<JButton> buttonList = new ArrayList<>();
        int x = 210;
        int y = 80;
        int width = 50;
        int height = 50;
        String[] operands = {"/", "x", "+", "-"};
        String[] operandNames = {"Divide", "Multiply", "Add", "Subtract"};
        for (int i = 0; i < 4; i++) {
            JButton button = new JButton();
            button.setName(operandNames[i]);
            button.setText(operands[i]);
            button.setBounds(x, y, width, height);
            buttonList.add(button);
            y += 60;
        }
        JButton button = new JButton();
        button.setName("Equals");
        button.setText("=");
        button.setBounds(x-60, y-60, width, height);
        buttonList.add(button);
        return buttonList;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setName("EquationTextField");
        textField.setBounds(25, 10, 240, 30);
        return textField;
    }

    private void createAction() {
        currentInput = new StringBuilder();
        for (JButton button : numberButtons) {
            button.addActionListener(e -> {
                currentInput.append(e.getActionCommand());
                equationTextField.setText(currentInput.toString());
                System.out.println(equationTextField.getText());
            });
        }
        for (JButton button : operandButtons) {
            if (!button.getActionCommand().equals("=")) {
                button.addActionListener(e -> {
                    currentInput.append(e.getActionCommand());
                    equationTextField.setText(currentInput.toString());
                    System.out.println(e.getActionCommand());
                });
            } else {
                button.addActionListener(e -> solve(button));
            }
        }
    }

    private void solve(JButton button) {
        List<String> operands = new ArrayList<>();
        operandButtons.forEach(e -> operands.add(e.getActionCommand()));
        int operand = -1;
        if (!equationTextField.getText().isEmpty()) {
            for (int i = 0; i < operands.size(); i++) {
                if (equationTextField.getText().contains(operands.get(i))) {
                    operand = i;
                }
            }
        }
        if (operand == -1) {
            equationTextField.setText("1");
            currentInput.setLength(0);
            return;
        }
        String[] equation;
        if (operand == 2) {
            equation = equationTextField.getText().split("\\+");
        } else if (operand == 3) {
            equation = equationTextField.getText().split("\\-");
        } else {
            equation = equationTextField.getText().split(operands.get(operand));
        }
        switch (operand) {
            case 0 ->
                    equationTextField.setText(equation[0] + operands.get(operand) + equation[1] + "=" + (Double.parseDouble(equation[0]) / Double.parseDouble(equation[1])));
            case 1 ->
                    equationTextField.setText(equation[0] + operands.get(operand) + equation[1] + "=" + (Integer.parseInt(equation[0]) * Integer.parseInt(equation[1])));
            case 2 ->
                    equationTextField.setText(equation[0] + operands.get(operand) + equation[1] + "=" + (Integer.parseInt(equation[0]) + Integer.parseInt(equation[1])));
            case 3 ->
                    equationTextField.setText(equation[0] + operands.get(operand) + equation[1] + "=" + (Integer.parseInt(equation[0]) - Integer.parseInt(equation[1])));
        }
        currentInput.setLength(0);
    }
}
