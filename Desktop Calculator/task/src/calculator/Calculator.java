package calculator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Calculator extends JFrame {
    private List<JButton> numberButtons;
    private List<JButton> operandButtons;
    private JTextField equationTextField;
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
            button.setName(numberNames[9-i]);
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
        for (JButton button : numberButtons) {
            button.addActionListener(e -> {
                String content = equationTextField.getText();
                content += button.getText();
                equationTextField.setText(content);
            });
        }
        for (JButton button : operandButtons) {
            if (!button.getActionCommand().equals("=")) {
                button.addActionListener(e -> {
                    String content = equationTextField.getText();
                    content += button.getText();
                    equationTextField.setText(content);
                });
            } else {
                button.addActionListener(e -> {
                    String content = equationTextField.getText();
                    content += button.getText();
                    content += solve();
                    equationTextField.setText(content);
                });
            }
        }
    }

    private String solve() {
        List<String> operands = new ArrayList<>();
        operandButtons.forEach(e -> operands.add(e.getActionCommand()));
        int operand = -1;
        String equation_raw = equationTextField.getText();
        if (!equation_raw.isEmpty()) {
            for (int i = 0; i < operands.size()-1; i++) {
                if (equation_raw.contains(operands.get(i))) {
                    operand = i;
                    System.out.println(operands.get(i));
                }
            }
        }
        if (operand == -1) {
            System.out.println("test");
            return "-1";
        }
        String[] equation_split;
        if (operand == 2) {
            equation_split = equation_raw.split("\\+");
        } else if (operand == 3) {
            equation_split = equation_raw.split("\\-");
        } else {
            equation_split = equation_raw.split(operands.get(operand));
        }
        if (operand == 0) {
            return String.valueOf(Integer.parseInt(equation_split[0]) / Integer.parseInt(equation_split[1]));
        } else if (operand == 1) {
            return String.valueOf(Integer.parseInt(equation_split[0]) * Integer.parseInt(equation_split[1]));
        } else if (operand == 2) {
            return String.valueOf(Integer.parseInt(equation_split[0]) + Integer.parseInt(equation_split[1]));
        } else {
            return String.valueOf(Integer.parseInt(equation_split[0]) - Integer.parseInt(equation_split[1]));
        }
    }
}
