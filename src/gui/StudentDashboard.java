package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.ResultModel;
import models.StudentModel;
import repositories.ResultRepository;
import repositories.SubjectRepository;

import java.util.List;

public class StudentDashboard {

    private BorderPane root;
    private StudentModel student;
    private String activeTab = "quiz";
    private BorderPane contentArea;

    public StudentDashboard(StudentModel student) {
        this.student = student;
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0f1117;");
        root.setTop(buildNavBar());
        contentArea = new BorderPane();
        root.setCenter(contentArea);
        showQuizTab();
    }

    private HBox buildNavBar() {
        HBox nav = new HBox(0);
        nav.getStyleClass().add("nav-bar");
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setPrefHeight(50);

        // Brand
        HBox brand = new HBox(4);
        brand.setAlignment(Pos.CENTER);
        brand.setPadding(new Insets(0, 24, 0, 4));
        Label vault = new Label("ExamVault");
        vault.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #e6edf3;");
        Label plus = new Label("Plus");
        plus.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #3b82f6;");
        brand.getChildren().addAll(vault, plus);

        // Nav tabs
        Button quizBtn = navButton("Quiz", "quiz");
        Button resultsBtn = navButton("My Results", "results");
        Button calcBtn = navButton("Calculator", "calc");
        Button cgpaBtn = navButton("CGPA Calculator", "cgpa");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // User info + logout
        HBox userInfo = new HBox(12);
        userInfo.setAlignment(Pos.CENTER);
        Label userLabel = new Label(student.getName() != null && !student.getName().isEmpty()
                ? student.getName() : student.getUsername());
        userLabel.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 13px;");
        Label idBadge = new Label(student.getUsername());
        idBadge.getStyleClass().add("badge");
        Button logoutBtn = new Button("Sign Out");
        logoutBtn.getStyleClass().add("btn-ghost");
        logoutBtn.setOnAction(e -> SceneManager.showLoginView());
        userInfo.getChildren().addAll(userLabel, idBadge, logoutBtn);

        nav.getChildren().addAll(brand, quizBtn, resultsBtn, calcBtn, cgpaBtn, spacer, userInfo);
        HBox.setMargin(userInfo, new Insets(0, 8, 0, 0));

        updateNavStyles(nav, quizBtn, resultsBtn, calcBtn, cgpaBtn);

        quizBtn.setOnAction(e -> { activeTab = "quiz"; updateNavStyles(nav, quizBtn, resultsBtn, calcBtn, cgpaBtn); showQuizTab(); });
        resultsBtn.setOnAction(e -> { activeTab = "results"; updateNavStyles(nav, quizBtn, resultsBtn, calcBtn, cgpaBtn); showResultsTab(); });
        calcBtn.setOnAction(e -> { activeTab = "calc"; updateNavStyles(nav, quizBtn, resultsBtn, calcBtn, cgpaBtn); showCalculatorTab(); });
        cgpaBtn.setOnAction(e -> { activeTab = "cgpa"; updateNavStyles(nav, quizBtn, resultsBtn, calcBtn, cgpaBtn); showCGPATab(); });

        return nav;
    }

    private Button navButton(String text, String id) {
        Button btn = new Button(text);
        btn.setId("nav_" + id);
        btn.getStyleClass().add("nav-item");
        return btn;
    }

    private void updateNavStyles(HBox nav, Button... buttons) {
        for (Button btn : buttons) {
            String id = btn.getId().replace("nav_", "");
            if (id.equals(activeTab)) {
                btn.getStyleClass().setAll("nav-item-active");
            } else {
                btn.getStyleClass().setAll("nav-item");
            }
        }
    }

    private void showQuizTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #0f1117; -fx-background: #0f1117;");

        VBox content = new VBox(24);
        content.setPadding(new Insets(28, 32, 28, 32));
        content.setStyle("-fx-background-color: #0f1117;");

        Label heading = new Label("Take a Quiz");
        heading.getStyleClass().add("heading-lg");
        Label sub = new Label("Select a subject and start your quiz");
        sub.getStyleClass().add("text-secondary");

        VBox header = new VBox(4, heading, sub);

        List<String> subjects = SubjectRepository.getAll();

        if (subjects.isEmpty()) {
            VBox empty = new VBox(12);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(60));
            Label emptyIcon = new Label("📚");
            emptyIcon.setStyle("-fx-font-size: 40px;");
            Label emptyText = new Label("No subjects available yet.");
            emptyText.getStyleClass().add("text-secondary");
            Label emptyHint = new Label("Ask your teacher to add subjects and questions.");
            emptyHint.getStyleClass().add("text-muted");
            empty.getChildren().addAll(emptyIcon, emptyText, emptyHint);
            content.getChildren().addAll(header, empty);
        } else {
            FlowPane subjectGrid = new FlowPane(16, 16);

            for (String subject : subjects) {
                VBox card = new VBox(10);
                card.getStyleClass().add("card-hover");
                card.setPrefWidth(200);
                card.setPadding(new Insets(18));

                Label icon = new Label("📖");
                icon.setStyle("-fx-font-size: 26px;");
                Label subjectName = new Label(subject);
                subjectName.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #e6edf3;");
                subjectName.setWrapText(true);
                Label startLabel = new Label("Click to start →");
                startLabel.getStyleClass().add("text-accent");
                startLabel.setStyle(startLabel.getStyle() + "-fx-font-size: 12px;");

                card.getChildren().addAll(icon, subjectName, startLabel);

                card.setOnMouseClicked(e -> {
                    QuizView quizView = new QuizView(student, subject);
                    contentArea.setCenter(quizView.getRoot());
                });

                subjectGrid.getChildren().add(card);
            }

            content.getChildren().addAll(header, subjectGrid);
        }

        scroll.setContent(content);
        contentArea.setCenter(scroll);
    }

    private void showResultsTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #0f1117; -fx-background: #0f1117;");

        VBox content = new VBox(24);
        content.setPadding(new Insets(28, 32, 28, 32));
        content.setStyle("-fx-background-color: #0f1117;");

        Label heading = new Label("My Results");
        heading.getStyleClass().add("heading-lg");
        Label sub = new Label("Your quiz performance history");
        sub.getStyleClass().add("text-secondary");
        VBox header = new VBox(4, heading, sub);

        List<ResultModel> results = ResultRepository.getByStudent(student.getUsername());

        if (results.isEmpty()) {
            VBox empty = new VBox(12);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(60));
            Label emptyIcon = new Label("📊");
            emptyIcon.setStyle("-fx-font-size: 40px;");
            Label emptyText = new Label("No results yet.");
            emptyText.getStyleClass().add("text-secondary");
            Label emptyHint = new Label("Take a quiz to see your scores here.");
            emptyHint.getStyleClass().add("text-muted");
            empty.getChildren().addAll(emptyIcon, emptyText, emptyHint);
            content.getChildren().addAll(header, empty);
        } else {
            // Summary stats
            int totalQuizzes = results.size();
            double avgPct = results.stream().mapToDouble(ResultModel::getPercentage).average().orElse(0);
            int bestScore = results.stream().mapToInt(r -> (int)(r.getPercentage())).max().orElse(0);

            HBox statsRow = new HBox(16);
            statsRow.getChildren().addAll(
                statCard("Quizzes Taken", String.valueOf(totalQuizzes), "#3b82f6"),
                statCard("Average Score", String.format("%.0f%%", avgPct), "#22c55e"),
                statCard("Best Score", bestScore + "%", "#f59e0b")
            );

            // Table
            TableView<ResultModel> table = new TableView<>();
            table.setStyle("-fx-background-color: #1c2128; -fx-border-color: #30363d; -fx-background-radius: 8px;");
            table.getStyleClass().add("table-view");
            table.setPrefHeight(300);

            TableColumn<ResultModel, String> subjectCol = new TableColumn<>("Subject");
            subjectCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getSubject()));
            subjectCol.setPrefWidth(220);

            TableColumn<ResultModel, String> scoreCol = new TableColumn<>("Score");
            scoreCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                    d.getValue().getScore() + " / " + d.getValue().getTotal()));
            scoreCol.setPrefWidth(140);

            TableColumn<ResultModel, String> pctCol = new TableColumn<>("Percentage");
            pctCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                    String.format("%.0f%%", d.getValue().getPercentage())));
            pctCol.setPrefWidth(140);

            TableColumn<ResultModel, String> gradeCol = new TableColumn<>("Grade");
            gradeCol.setCellValueFactory(d -> {
                double pct = d.getValue().getPercentage();
                String grade = pct >= 80 ? "A+" : pct >= 75 ? "A" : pct >= 70 ? "A-" :
                        pct >= 65 ? "B+" : pct >= 60 ? "B" : pct >= 55 ? "B-" :
                        pct >= 50 ? "C+" : pct >= 45 ? "C" : pct >= 40 ? "D" : "F";
                return new javafx.beans.property.SimpleStringProperty(grade);
            });
            gradeCol.setPrefWidth(120);

            table.getColumns().addAll(subjectCol, scoreCol, pctCol, gradeCol);
            table.getItems().addAll(results);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            content.getChildren().addAll(header, statsRow, table);
        }

        scroll.setContent(content);
        contentArea.setCenter(scroll);
    }

    private VBox statCard(String label, String value, String color) {
        VBox card = new VBox(4);
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(160);

        Label val = new Label(value);
        val.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label lbl = new Label(label);
        lbl.getStyleClass().add("stat-label");

        card.getChildren().addAll(val, lbl);
        return card;
    }

    private void showCalculatorTab() {
        CalculatorView calcView = new CalculatorView();
        contentArea.setCenter(calcView.getRoot());
    }

    private void showCGPATab() {
        CGPACalculatorView cgpaView = new CGPACalculatorView(student.getName() != null ? student.getName() : student.getUsername());
        contentArea.setCenter(cgpaView.getRoot());
    }

    public BorderPane getRoot() { return root; }
}
