package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import services.CGPAService;

import java.util.ArrayList;
import java.util.List;

public class CGPACalculatorView {

    private BorderPane root;
    private String studentName;
    private StackPane contentArea;

    public CGPACalculatorView(String studentName) {
        this.studentName = studentName;
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0f1117;");
        root.setCenter(buildMain());
    }

    private ScrollPane buildMain() {
        VBox outer = new VBox(24);
        outer.setPadding(new Insets(28, 32, 28, 32));
        outer.setStyle("-fx-background-color: #0f1117;");

        Label title = new Label("CGPA Calculator");
        title.getStyleClass().add("heading-lg");
        Label sub = new Label("Calculate your GPA by marks or by subject grades");
        sub.getStyleClass().add("text-secondary");
        VBox header = new VBox(4, title, sub);

        // Mode toggle
        HBox modeBar = new HBox(0);
        modeBar.setStyle("-fx-background-color: #161b22; -fx-background-radius: 6px; -fx-padding: 4px;");
        modeBar.setAlignment(Pos.CENTER_LEFT);
        modeBar.setMaxWidth(360);

        ToggleButton marksMode = new ToggleButton("Marks-Based GPA");
        ToggleButton subjectMode = new ToggleButton("Subject-Based CGPA");

        String activeStyle = "-fx-background-color: #1c2128; -fx-text-fill: #3b82f6; -fx-font-weight: bold; " +
                "-fx-font-size: 13px; -fx-padding: 7px 18px; -fx-background-radius: 4px; -fx-cursor: hand; -fx-border-color: transparent;";
        String inactiveStyle = "-fx-background-color: transparent; -fx-text-fill: #8b949e; -fx-font-size: 13px; " +
                "-fx-padding: 7px 18px; -fx-background-radius: 4px; -fx-cursor: hand; -fx-border-color: transparent;";

        marksMode.setStyle(activeStyle);
        subjectMode.setStyle(inactiveStyle);
        ToggleGroup modeGroup = new ToggleGroup();
        marksMode.setToggleGroup(modeGroup); subjectMode.setToggleGroup(modeGroup);
        marksMode.setSelected(true);
        modeBar.getChildren().addAll(marksMode, subjectMode);

        contentArea = new StackPane();
        VBox marksContent = buildMarksContent();
        VBox subjectContent = buildSubjectContent();
        subjectContent.setVisible(false); subjectContent.setManaged(false);
        contentArea.getChildren().addAll(marksContent, subjectContent);

        marksMode.setOnAction(e -> {
            marksMode.setStyle(activeStyle); subjectMode.setStyle(inactiveStyle);
            marksContent.setVisible(true); marksContent.setManaged(true);
            subjectContent.setVisible(false); subjectContent.setManaged(false);
        });
        subjectMode.setOnAction(e -> {
            subjectMode.setStyle(activeStyle); marksMode.setStyle(inactiveStyle);
            subjectContent.setVisible(true); subjectContent.setManaged(true);
            marksContent.setVisible(false); marksContent.setManaged(false);
        });

        outer.getChildren().addAll(header, modeBar, contentArea);

        ScrollPane scroll = new ScrollPane(outer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #0f1117; -fx-background: #0f1117;");
        return scroll;
    }

    private VBox buildMarksContent() {
        VBox box = new VBox(20);
        box.setMaxWidth(560);

        Label heading = new Label("Enter Marks for Each Component");
        heading.getStyleClass().add("heading-md");

        VBox card = new VBox(14);
        card.getStyleClass().add("card");

        String[][] criteria = {
            {"Class Attendance", "0", "7"},
            {"Assignment", "0", "5"},
            {"Presentation", "0", "8"},
            {"3 Quizzes Total", "0", "15"},
            {"Midterm Exam", "0", "25"},
            {"Final Exam", "0", "40"}
        };

        List<TextField> fields = new ArrayList<>();

        for (String[] c : criteria) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            Label lbl = new Label(c[0]);
            lbl.setStyle("-fx-text-fill: #e6edf3; -fx-font-size: 13px; -fx-min-width: 200px;");
            Label range = new Label("(0–" + c[2] + ")");
            range.getStyleClass().add("text-muted");
            TextField field = new TextField();
            field.setPromptText("0–" + c[2]);
            field.setMaxWidth(90);
            HBox spacer = new HBox(); HBox.setHgrow(spacer, Priority.ALWAYS);
            row.getChildren().addAll(lbl, spacer, range, field);
            fields.add(field);
            card.getChildren().add(row);
        }

        // Result area
        VBox resultBox = new VBox(10);
        resultBox.setVisible(false);
        resultBox.setManaged(false);

        Button calcBtn = new Button("Calculate GPA");
        calcBtn.getStyleClass().add("btn-primary");
        calcBtn.setPadding(new Insets(10, 24, 10, 24));

        Label errorLbl = new Label("");
        errorLbl.getStyleClass().add("text-danger");
        errorLbl.setVisible(false);

        calcBtn.setOnAction(e -> {
            errorLbl.setVisible(false);
            double total = 0;
            try {
                double[] maxMarks = {7, 5, 8, 15, 25, 40};
                for (int i = 0; i < fields.size(); i++) {
                    String val = fields.get(i).getText().trim();
                    if (val.isEmpty()) { errorLbl.setText("Please fill all fields."); errorLbl.setVisible(true); return; }
                    double v = Double.parseDouble(val);
                    if (v < 0 || v > maxMarks[i]) {
                        errorLbl.setText("Value out of range for field " + (i+1) + ".");
                        errorLbl.setVisible(true); return;
                    }
                    total += v;
                }
                if (total < 0 || total > 100) { errorLbl.setText("Total must be between 0 and 100."); errorLbl.setVisible(true); return; }
                String[] info = CGPAService.getGradeInfoByMarks(total);
                showGPAResult(resultBox, total, info);
                resultBox.setVisible(true); resultBox.setManaged(true);
            } catch (NumberFormatException ex) {
                errorLbl.setText("Please enter valid numbers only."); errorLbl.setVisible(true);
            }
        });

        card.getChildren().addAll(new Separator(), errorLbl, calcBtn);
        box.getChildren().addAll(heading, card, resultBox);
        return box;
    }

    private void showGPAResult(VBox resultBox, double total, String[] info) {
        resultBox.getChildren().clear();
        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER);

        Label emoji = new Label(Double.parseDouble(info[1]) >= 3.5 ? "🎉" : Double.parseDouble(info[1]) >= 2.5 ? "👍" : "📚");
        emoji.setStyle("-fx-font-size: 36px;");

        Label congrats = new Label("Congratulations, " + studentName + "!");
        congrats.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #e6edf3;");

        Label remarkLbl = new Label(info[2] + " Result");
        remarkLbl.getStyleClass().add("text-secondary");

        HBox statsRow = new HBox(24);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
            resultStat("Total Marks", String.format("%.1f", total), "#e6edf3"),
            resultStat("Grade", info[0], "#3b82f6"),
            resultStat("Grade Point", info[1], "#22c55e")
        );

        // Grade table reference
        Label tableTitle = new Label("Grade Scale Reference");
        tableTitle.getStyleClass().add("text-secondary");
        tableTitle.setPadding(new Insets(8, 0, 4, 0));

        card.getChildren().addAll(emoji, congrats, remarkLbl, new Separator(), statsRow);
        resultBox.getChildren().add(card);
    }

    private VBox resultStat(String label, String value, String color) {
        VBox v = new VBox(4);
        v.setAlignment(Pos.CENTER);
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label lbl = new Label(label);
        lbl.getStyleClass().add("text-muted");
        v.getChildren().addAll(val, lbl);
        return v;
    }

    private VBox buildSubjectContent() {
        VBox box = new VBox(20);
        box.setMaxWidth(640);

        Label heading = new Label("Enter Subject Grades");
        heading.getStyleClass().add("heading-md");

        // Subject entry area
        VBox subjectListCard = new VBox(10);
        subjectListCard.getStyleClass().add("card");
        Label listTitle = new Label("Subjects");
        listTitle.getStyleClass().add("heading-md");

        VBox subjectRows = new VBox(8);

        // Result area
        VBox resultBox = new VBox(10);
        resultBox.setVisible(false);
        resultBox.setManaged(false);

        Label errorLbl = new Label("");
        errorLbl.getStyleClass().add("text-danger");
        errorLbl.setVisible(false);

        // Add first row by default
        addSubjectRow(subjectRows);

        Button addRowBtn = new Button("+ Add Subject");
        addRowBtn.getStyleClass().add("btn-secondary");
        addRowBtn.setOnAction(e -> addSubjectRow(subjectRows));

        Button calcBtn = new Button("Calculate CGPA");
        calcBtn.getStyleClass().add("btn-primary");
        calcBtn.setPadding(new Insets(10, 24, 10, 24));

        HBox btnRow = new HBox(12);
        btnRow.setAlignment(Pos.CENTER_LEFT);
        btnRow.getChildren().addAll(addRowBtn, calcBtn);

        calcBtn.setOnAction(e -> {
            errorLbl.setVisible(false);
            List<double[]> entries = new ArrayList<>();
            for (int i = 0; i < subjectRows.getChildren().size(); i++) {
                HBox row = (HBox) subjectRows.getChildren().get(i);
                TextField gradeField = (TextField) row.getChildren().get(2);
                TextField creditField = (TextField) row.getChildren().get(4);
                String gradeInput = gradeField.getText().trim();
                String creditInput = creditField.getText().trim();
                if (gradeInput.isEmpty() || creditInput.isEmpty()) continue;
                double gp;
                try {
                    if (gradeInput.matches("\\d+(\\.\\d+)?")) {
                        gp = Double.parseDouble(gradeInput);
                        if (gp < 0 || gp > 4.0) { errorLbl.setText("Grade point must be 0.00–4.00."); errorLbl.setVisible(true); return; }
                    } else {
                        String[] info = CGPAService.getGradeInfoByGrade(gradeInput.toUpperCase());
                        if (info == null) { errorLbl.setText("Invalid grade: " + gradeInput + ". Use A+, A, A-, B+, etc."); errorLbl.setVisible(true); return; }
                        gp = Double.parseDouble(info[1]);
                    }
                    double credit = Double.parseDouble(creditInput);
                    if (credit <= 0) { errorLbl.setText("Credit hours must be positive."); errorLbl.setVisible(true); return; }
                    entries.add(new double[]{gp, credit});
                } catch (NumberFormatException ex) {
                    errorLbl.setText("Invalid number in row " + (i + 1)); errorLbl.setVisible(true); return;
                }
            }
            if (entries.isEmpty()) { errorLbl.setText("Please add at least one subject."); errorLbl.setVisible(true); return; }
            double totalCredits = 0, weighted = 0;
            for (double[] en : entries) { weighted += en[0] * en[1]; totalCredits += en[1]; }
            double cgpa = totalCredits > 0 ? weighted / totalCredits : 0;
            String[] info = CGPAService.getGradeInfoByMarks(CGPAService.cgpaToMarks(cgpa));
            showCGPAResult(resultBox, cgpa, info, totalCredits);
            resultBox.setVisible(true); resultBox.setManaged(true);
        });

        // Grade reference
        VBox gradeRef = buildGradeReference();

        subjectListCard.getChildren().addAll(listTitle, subjectRows, btnRow, errorLbl);
        box.getChildren().addAll(heading, subjectListCard, resultBox, gradeRef);
        return box;
    }

    private void addSubjectRow(VBox container) {
        int rowNum = container.getChildren().size() + 1;
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label num = new Label(rowNum + ".");
        num.setStyle("-fx-text-fill: #8b949e; -fx-min-width: 24px;");

        TextField nameField = new TextField();
        nameField.setPromptText("Subject name");
        nameField.setPrefWidth(160);

        TextField gradeField = new TextField();
        gradeField.setPromptText("Grade (A+, B, 3.75...)");
        gradeField.setPrefWidth(160);

        Label creditLbl = new Label("Credits:");
        creditLbl.getStyleClass().add("text-secondary");

        TextField creditField = new TextField();
        creditField.setPromptText("e.g. 3");
        creditField.setPrefWidth(70);

        Button removeBtn = new Button("✕");
        removeBtn.getStyleClass().add("btn-ghost");
        removeBtn.setStyle(removeBtn.getStyle() + "-fx-text-fill: #ef4444;");
        removeBtn.setOnAction(e -> {
            if (container.getChildren().size() > 1) container.getChildren().remove(row);
        });

        row.getChildren().addAll(num, nameField, gradeField, creditLbl, creditField, removeBtn);
        container.getChildren().add(row);
    }

    private void showCGPAResult(VBox resultBox, double cgpa, String[] info, double totalCredits) {
        resultBox.getChildren().clear();
        VBox card = new VBox(16);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER);

        Label emoji = new Label(cgpa >= 3.5 ? "🎉" : cgpa >= 2.5 ? "👍" : "📚");
        emoji.setStyle("-fx-font-size: 36px;");
        Label congrats = new Label("Congratulations, " + studentName + "!");
        congrats.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #e6edf3;");
        Label remarkLbl = new Label(info[2] + " Result");
        remarkLbl.getStyleClass().add("text-secondary");

        HBox statsRow = new HBox(24);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
            resultStat("CGPA", String.format("%.2f", cgpa), "#3b82f6"),
            resultStat("Grade", info[0], "#22c55e"),
            resultStat("Total Credits", String.format("%.1f", totalCredits), "#f59e0b")
        );
        card.getChildren().addAll(emoji, congrats, remarkLbl, new Separator(), statsRow);
        resultBox.getChildren().add(card);
    }

    private VBox buildGradeReference() {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setMaxWidth(640);

        Label title = new Label("Grade Scale");
        title.getStyleClass().add("heading-md");

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(6);
        grid.setPadding(new Insets(6, 0, 0, 0));

        String[][] table = CGPAService.getGradeTable();
        String[] headers = {"Marks", "Grade", "Points", "Remarks"};

        for (int col = 0; col < headers.length; col++) {
            Label h = new Label(headers[col]);
            h.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 11px; -fx-font-weight: bold;");
            grid.add(h, col, 0);
        }

        for (int row = 0; row < table.length; row++) {
            String[] r = table[row];
            Label marks = new Label(r[0] + "–" + r[1]);
            marks.setStyle("-fx-text-fill: #e6edf3; -fx-font-size: 12px;");
            Label grade = new Label(r[2]);
            grade.setStyle("-fx-text-fill: #3b82f6; -fx-font-weight: bold; -fx-font-size: 12px;");
            Label points = new Label(r[3]);
            points.setStyle("-fx-text-fill: #22c55e; -fx-font-size: 12px;");
            Label remark = new Label(r[4]);
            remark.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 12px;");
            grid.addRow(row + 1, marks, grade, points, remark);
        }

        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setMinWidth(80); cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }

        card.getChildren().addAll(title, grid);
        return card;
    }

    public BorderPane getRoot() { return root; }
}
