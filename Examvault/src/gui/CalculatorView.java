package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CalculatorView {
    private static Stage calculatorStage;
    private TextArea displayArea;

    private CalculatorView() {
    }

    public static void showCalculator() {
        if (calculatorStage == null || !calculatorStage.isShowing()) {
            calculatorStage = new Stage();
            calculatorStage.setTitle("Calculator");
            calculatorStage.setWidth(400);
            calculatorStage.setHeight(500);
            calculatorStage.setScene(createCalculatorScene());
        }
        calculatorStage.show();
        calculatorStage.toFront();
    }

    private static Scene createCalculatorScene() {
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #2c3e50;");

        // Display
        TextArea displayArea = new TextArea();
        displayArea.setPrefHeight(80);
        displayArea.setWrapText(true);
        displayArea.setEditable(true);
        displayArea.setStyle("-fx-font-size: 16px; -fx-padding: 10px; -fx-control-inner-background: #34495e; " +
                "-fx-text-fill: #ecf0f1; -fx-font-weight: bold; -fx-border-radius: 5;");

        // Button grid
        GridPane buttonGrid = new GridPane();
        buttonGrid.setPadding(new Insets(15));
        buttonGrid.setHgap(8);
        buttonGrid.setVgap(8);
        buttonGrid.setStyle("-fx-background-color: #2c3e50;");

        String[][] buttons = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"0", ".", "=", "+"},
                {"C", "Del", "", ""}
        };

        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                String buttonText = buttons[row][col];
                if (!buttonText.isEmpty()) {
                    Button btn = createCalculatorButton(buttonText, displayArea);
                    GridPane.setConstraints(btn, col, row);
                    buttonGrid.getChildren().add(btn);
                }
            }
        }

        VBox.setVgrow(buttonGrid, Priority.ALWAYS);
        root.getChildren().addAll(displayArea, buttonGrid);

        return new Scene(root);
    }

    private static Button createCalculatorButton(String text, TextArea display) {
        Button btn = new Button(text);
        btn.setPrefSize(80, 60);
        btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        final String buttonColor;
        if (text.matches("[0-9.]")) {
            buttonColor = "#95a5a6";
        } else if (text.equals("=")) {
            buttonColor = "#27ae60";
        } else if (text.equals("C")) {
            buttonColor = "#e74c3c";
        } else if (text.equals("Del")) {
            buttonColor = "#e67e22";
        } else {
            buttonColor = "#3498db";
        }

        btn.setStyle("-fx-font-size: 16px; -fx-background-color: " + buttonColor + "; " +
                "-fx-text-fill: white; -fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-font-size: 16px; -fx-background-color: " + buttonColor +
                "; -fx-text-fill: white; -fx-border-radius: 5; -fx-opacity: 0.8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-font-size: 16px; -fx-background-color: " + buttonColor +
                "; -fx-text-fill: white; -fx-border-radius: 5;"));

        btn.setOnAction(e -> handleCalculatorButton(text, display));
        return btn;
    }

    private static void handleCalculatorButton(String text, TextArea display) {
        String currentText = display.getText();

        if (text.equals("C")) {
            display.clear();
        } else if (text.equals("Del")) {
            if (!currentText.isEmpty()) {
                display.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (text.equals("=")) {
            try {
                String result = String.valueOf(evaluateExpression(currentText));
                display.setText(result);
            } catch (Exception ex) {
                display.setText("Error");
            }
        } else {
            display.appendText(text);
        }
    }

    private static double evaluateExpression(String expression) {
        // Simple expression evaluator without external dependencies
        expression = expression.replaceAll("\\s+", "");
        try {
            return eval(expression);
        } catch (Exception e) {
            throw new RuntimeException("Invalid expression");
        }
    }

    private static double eval(String expression) {
        return new Calculator().evaluate(expression);
    }

    static class Calculator {
        private String expr;
        private int pos = 0;

        public double evaluate(String expression) {
            this.expr = expression;
            this.pos = 0;
            double result = parseExpression();
            if (pos < expr.length()) {
                throw new RuntimeException("Unexpected character: " + expr.charAt(pos));
            }
            return result;
        }

        private double parseExpression() {
            double result = parseTerm();
            while (pos < expr.length()) {
                char op = expr.charAt(pos);
                if (op == '+' || op == '-') {
                    pos++;
                    double right = parseTerm();
                    if (op == '+') result += right;
                    else result -= right;
                } else {
                    break;
                }
            }
            return result;
        }

        private double parseTerm() {
            double result = parseFactor();
            while (pos < expr.length()) {
                char op = expr.charAt(pos);
                if (op == '*' || op == '/') {
                    pos++;
                    double right = parseFactor();
                    if (op == '*') result *= right;
                    else if (right != 0) result /= right;
                    else throw new RuntimeException("Division by zero");
                } else {
                    break;
                }
            }
            return result;
        }

        private double parseFactor() {
            if (pos < expr.length() && expr.charAt(pos) == '(') {
                pos++;
                double result = parseExpression();
                if (pos >= expr.length() || expr.charAt(pos) != ')') {
                    throw new RuntimeException("Missing closing parenthesis");
                }
                pos++;
                return result;
            }
            return parseNumber();
        }

        private double parseNumber() {
            StringBuilder num = new StringBuilder();
            if (pos < expr.length() && expr.charAt(pos) == '-') {
                num.append('-');
                pos++;
            }
            while (pos < expr.length() && (Character.isDigit(expr.charAt(pos)) || expr.charAt(pos) == '.')) {
                num.append(expr.charAt(pos));
                pos++;
            }
            if (num.length() == 0 || (num.length() == 1 && num.charAt(0) == '-')) {
                throw new RuntimeException("Invalid number");
            }
            return Double.parseDouble(num.toString());
        }
    }
}
