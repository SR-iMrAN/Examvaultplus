package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class CalculatorView {

    private BorderPane root;
    private Label display;
    private Label expressionDisplay;
    private StringBuilder currentInput = new StringBuilder();
    private double storedValue = 0;
    private String pendingOp = null;
    private boolean freshInput = true;
    private String fullExpression = "";

    public CalculatorView() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0f1117;");
        root.setCenter(buildCalculator());
    }

    private VBox buildCalculator() {
        VBox outer = new VBox(0);
        outer.setAlignment(Pos.CENTER);
        outer.setPadding(new Insets(28));

        // Title
        HBox titleRow = new HBox();
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setPadding(new Insets(0, 0, 20, 0));
        Label title = new Label("Calculator");
        title.getStyleClass().add("heading-lg");
        titleRow.getChildren().add(title);

        // Calculator card
        VBox calcCard = new VBox(16);
        calcCard.getStyleClass().add("card");
        calcCard.setMaxWidth(340);
        calcCard.setMinWidth(320);

        // Expression display (secondary)
        expressionDisplay = new Label(" ");
        expressionDisplay.setStyle("-fx-font-size: 13px; -fx-text-fill: #8b949e; -fx-alignment: CENTER_RIGHT; -fx-min-width: 280px;");
        expressionDisplay.setMaxWidth(Double.MAX_VALUE);
        expressionDisplay.setAlignment(Pos.CENTER_RIGHT);

        // Main display
        display = new Label("0");
        display.getStyleClass().add("calc-display");
        display.setMaxWidth(Double.MAX_VALUE);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setMinHeight(60);

        VBox displayBox = new VBox(4, expressionDisplay, display);
        displayBox.setStyle("-fx-background-color: #0f1117; -fx-background-radius: 8px; -fx-padding: 10px 14px;");

        // Button grid
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);

        // Row 0: DEL, AC, +/-, ÷
        grid.add(calcBtn("DEL", "delete"), 0, 0);
        grid.add(calcBtn("AC", "clear"), 1, 0);
        grid.add(calcBtn("±", "negate"), 2, 0);
        grid.add(opBtn("÷", "/"), 3, 0);

        // Row 1: 7 8 9 ×
        grid.add(numBtn("7"), 0, 1);
        grid.add(numBtn("8"), 1, 1);
        grid.add(numBtn("9"), 2, 1);
        grid.add(opBtn("×", "*"), 3, 1);

        // Row 2: 4 5 6 −
        grid.add(numBtn("4"), 0, 2);
        grid.add(numBtn("5"), 1, 2);
        grid.add(numBtn("6"), 2, 2);
        grid.add(opBtn("−", "-"), 3, 2);

        // Row 3: 1 2 3 +
        grid.add(numBtn("1"), 0, 3);
        grid.add(numBtn("2"), 1, 3);
        grid.add(numBtn("3"), 2, 3);
        grid.add(opBtn("+", "+"), 3, 3);

        // Row 4: 0 (wide), . =
        Button zeroBtn = numBtn("0");
        zeroBtn.setMinWidth(128);
        zeroBtn.setMaxWidth(Double.MAX_VALUE);
        GridPane.setColumnSpan(zeroBtn, 2);
        grid.add(zeroBtn, 0, 4);
        grid.add(numBtn("."), 2, 4);
        grid.add(equalsBtn(), 3, 4);

        // Make all columns equal width
        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setMinWidth(60);
            grid.getColumnConstraints().add(cc);
        }

        calcCard.getChildren().addAll(displayBox, grid);
        outer.getChildren().addAll(titleRow, calcCard);
        return outer;
    }

    private Button numBtn(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("calc-btn");
        btn.setMinWidth(60); btn.setMinHeight(50);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> handleNumber(text));
        return btn;
    }

    private Button opBtn(String display, String op) {
        Button btn = new Button(display);
        btn.getStyleClass().add("calc-btn-op");
        btn.setMinWidth(60); btn.setMinHeight(50);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> handleOperator(op, display));
        return btn;
    }

    private Button calcBtn(String text, String action) {
        Button btn = new Button(text);
        btn.getStyleClass().add("calc-btn-clear");
        btn.setMinWidth(60); btn.setMinHeight(50);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            switch (action) {
                case "clear" -> handleClear();
                case "negate" -> handleNegate();
                case "percent" -> handlePercent();
                case "delete" -> handleDelete();
            }
        });
        return btn;
    }

    private Button equalsBtn() {
        Button btn = new Button("=");
        btn.getStyleClass().add("calc-btn-equals");
        btn.setMinWidth(60); btn.setMinHeight(50);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> handleEquals());
        return btn;
    }

    private void handleNumber(String digit) {
        if (freshInput) {
            currentInput.setLength(0);
            freshInput = false;
        }
        if (digit.equals(".") && currentInput.toString().contains(".")) return;
        if (digit.equals(".") && currentInput.length() == 0) currentInput.append("0");
        currentInput.append(digit);
        display.setText(currentInput.toString());
    }

    private void handleOperator(String op, String displayOp) {
        if (currentInput.length() > 0) {
            if (pendingOp != null && !freshInput) {
                computePending();
            } else {
                storedValue = Double.parseDouble(currentInput.toString());
            }
        }
        pendingOp = op;
        fullExpression = formatNumber(storedValue) + " " + displayOp;
        expressionDisplay.setText(fullExpression);
        freshInput = true;
    }

    private void handleEquals() {
        if (pendingOp == null || currentInput.length() == 0) return;
        double inputVal = Double.parseDouble(currentInput.toString());
        fullExpression = formatNumber(storedValue) + " " + opSymbol(pendingOp) + " " + formatNumber(inputVal) + " =";
        expressionDisplay.setText(fullExpression);
        computePending();
        pendingOp = null;
        freshInput = true;
    }

    private void computePending() {
        if (pendingOp == null || currentInput.length() == 0) return;
        double inputVal = Double.parseDouble(currentInput.toString());
        double result = switch (pendingOp) {
            case "+" -> storedValue + inputVal;
            case "-" -> storedValue - inputVal;
            case "*" -> storedValue * inputVal;
            case "/" -> inputVal == 0 ? Double.NaN : storedValue / inputVal;
            default -> inputVal;
        };
        if (Double.isNaN(result)) {
            display.setText("Error");
            currentInput.setLength(0);
            storedValue = 0;
        } else {
            storedValue = result;
            String formatted = formatNumber(result);
            display.setText(formatted);
            currentInput.setLength(0);
            currentInput.append(formatted);
        }
    }

    private void handleClear() {
        currentInput.setLength(0);
        storedValue = 0;
        pendingOp = null;
        freshInput = true;
        display.setText("0");
        expressionDisplay.setText(" ");
        fullExpression = "";
    }

    private void handleNegate() {
        if (currentInput.length() > 0) {
            double val = Double.parseDouble(currentInput.toString());
            val = -val;
            currentInput.setLength(0);
            currentInput.append(formatNumber(val));
            display.setText(currentInput.toString());
        }
    }

    private void handlePercent() {
        if (currentInput.length() > 0) {
            double val = Double.parseDouble(currentInput.toString()) / 100.0;
            currentInput.setLength(0);
            currentInput.append(formatNumber(val));
            display.setText(currentInput.toString());
        }
    }

    private void handleDelete() {
        if (currentInput.length() > 0) {
            currentInput.setLength(currentInput.length() - 1);
            if (currentInput.length() == 0) {
                display.setText("0");
                freshInput = true;
            } else {
                display.setText(currentInput.toString());
            }
        } else if (display.getText().equals("Error")) {
            handleClear();
        }
    }

    private String formatNumber(double val) {
        if (val == Math.floor(val) && !Double.isInfinite(val) && Math.abs(val) < 1e12) {
            return String.valueOf((long) val);
        }
        return String.format("%.6f", val).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    private String opSymbol(String op) {
        return switch (op) {
            case "+" -> "+";
            case "-" -> "−";
            case "*" -> "×";
            case "/" -> "÷";
            default -> op;
        };
    }

    public BorderPane getRoot() { return root; }
}
