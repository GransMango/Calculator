package calculator;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Calculator extends JFrame {
    private final List<JButton> numberButtons;
    private final List<JButton> operandButtons;
    private final List<JButton> functionalButtons;
    private final JLabel resultLabel;
    private final JLabel equationLabel;
    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setSize(300, 400);
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
        createAction();
    }

    private List<JButton> createNumbers() {
        List<JButton> buttonList = new ArrayList<>();
        int x = 150;
        int y = 120;
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
            if (y > 250) {
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

        JButton dotButton = new JButton();
        dotButton.setName("Dot");
        dotButton.setText(".");
        dotButton.setBounds(x-180, y-60, width, height);
        buttonList.add(dotButton);
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
        deleteButton.setBounds(x-5, y, width + 5, height);
        functionalButtonList.add(deleteButton);

        JButton clearButton = new JButton();
        clearButton.setName("Clear");
        clearButton.setText("C");
        clearButton.setBounds(x-65, y, width + 5, height);
        functionalButtonList.add(clearButton);

        deleteButton.addActionListener(e -> {
            if (resultLabel.getText().length() > 0) {
                resultLabel.setText(resultLabel.getText().substring(0, resultLabel.getText().length() - 1));
            }
        });

        clearButton.addActionListener(e -> {
            equationLabel.setText("");
            resultLabel.setText("");
        });
        return functionalButtonList;
    }

    private JLabel createResultField() {
        JLabel textField = new JLabel();
        textField.setName("ResultLabel");
        textField.setFont(new Font("DecoType Naskh", Font.BOLD, 20));
        textField.setBounds(25, 10, 240, 30);
        return textField;
    }

    private JLabel createEquationField() {
        JLabel textField = new JLabel();
        textField.setName("EquationLabel");
        textField.setFont(new Font("Serif Italic", Font.BOLD, 12));
        textField.setForeground(Color.GRAY);
        textField.setBounds(25, 30, 240, 30);
        return textField;
    }

    private void createAction() {
        for (JButton button : numberButtons) {
            button.addActionListener(e -> {
                String content = resultLabel.getText();
                content += button.getText();
                resultLabel.setText(content);
            });
        }
        for (JButton button : operandButtons) {
            if (!button.getActionCommand().equals("=")) {
                button.addActionListener(e -> {
                    String content = resultLabel.getText();
                    content += button.getText();
                    resultLabel.setText(content);
                });
            } else {
                button.addActionListener(e -> {
                    String content = resultLabel.getText();
                    equationLabel.setText(content);
                    content = String.valueOf(Calculation.evaluate(content));
                    resultLabel.setText(content);
                });
            }
        }
    }

}
