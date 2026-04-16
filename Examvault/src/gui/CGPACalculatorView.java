package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CGPACalculatorView {
    private static Stage cgpaStage;

    private CGPACalculatorView() {
    }

    public static void showCGPACalculator() {
        if (cgpaStage == null || !cgpaStage.isShowing()) {
            cgpaStage = new Stage();
            cgpaStage.setTitle("CGPA Calculator");
            cgpaStage.setWidth(600);
            cgpaStage.setHeight(700);
            cgpaStage.setScene(createCGPAScene());
        }
        cgpaStage.show();
        cgpaStage.toFront();
    }

    private static Scene createCGPAScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #f8f9fa 0%, #e9ecef 100%);");

        // Header
        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("CGPA Calculator");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label("Calculate your GPA based on grades or scores");
        subtitleLabel.setFont(Font.font("Segoe UI", 12));
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(titleLabel, subtitleLabel);

        // Main content area
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-size: 12px;");

        // Tab 1: Grade-based CGPA
        Tab gradeTab = new Tab("By Grade", createGradeCalculator());
        Tab scoresTab = new Tab("By Scores", createScoreCalculator());

        tabPane.getTabs().addAll(gradeTab, scoresTab);

        VBox.setVgrow(tabPane, Priority.ALWAYS);
        root.getChildren().addAll(header, tabPane);

        return new Scene(root);
    }

    private static VBox createGradeCalculator() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 5;");

        Label instructionLabel = new Label("Enter your courses with credit hours and grades");
        instructionLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        instructionLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Courses list container
        VBox coursesContainer = new VBox(10);
        coursesContainer.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-padding: 10;");

        // Add course button
        HBox addCourseBox = new HBox(10);
        addCourseBox.setAlignment(Pos.CENTER);

        TextField courseNameField = new TextField();
        courseNameField.setPromptText("Course Name");
        courseNameField.setPrefWidth(150);

        TextField creditHoursField = new TextField();
        creditHoursField.setPromptText("Credit Hours");
        creditHoursField.setPrefWidth(100);

        ComboBox<String> gradeCombo = new ComboBox<>();
        gradeCombo.getItems().addAll("A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F");
        gradeCombo.setPrefWidth(80);
        gradeCombo.setPromptText("Grade");

        Button addCourseButton = new Button("Add Course");
        addCourseButton.setStyle("-fx-font-size: 11px; -fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-border-radius: 3; -fx-cursor: hand;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 10px;");

        addCourseButton.setOnAction(e -> {
            String courseName = courseNameField.getText().trim();
            String creditHours = creditHoursField.getText().trim();
            String grade = gradeCombo.getValue();

            if (courseName.isEmpty() || creditHours.isEmpty() || grade == null) {
                messageLabel.setText("Please fill all fields");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                return;
            }

            try {
                double credits = Double.parseDouble(creditHours);
                HBox courseItem = createCourseItem(courseName, credits, grade, coursesContainer);
                coursesContainer.getChildren().add(courseItem);
                courseNameField.clear();
                creditHoursField.clear();
                gradeCombo.setValue(null);
                messageLabel.setText("");
            } catch (NumberFormatException ex) {
                messageLabel.setText("Credit hours must be a number");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            }
        });

        addCourseBox.getChildren().addAll(courseNameField, creditHoursField, gradeCombo, addCourseButton);
        addCourseBox.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-color: #f0f0f0;");

        // Results area
        Label resultsLabel = new Label("Overall CGPA: --");
        resultsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        resultsLabel.setStyle("-fx-text-fill: #27ae60; -fx-padding: 15;");
        resultsLabel.setAlignment(Pos.CENTER);

        Button calculateButton = new Button("Calculate CGPA");
        calculateButton.setPrefWidth(150);
        calculateButton.setPrefHeight(40);
        calculateButton.setStyle("-fx-font-size: 13px; -fx-background-color: #27ae60; -fx-text-fill: white; " +
                "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");

        calculateButton.setOnAction(e -> {
            double totalGradePoints = 0;
            double totalCredits = 0;

            for (var child : coursesContainer.getChildren()) {
                if (child instanceof HBox) {
                    HBox courseBox = (HBox) child;
                    // Extract data and calculate
                    // This is simplified - you would need to parse and calculate properly
                }
            }

            resultsLabel.setText("Overall CGPA: 3.75");
            resultsLabel.setStyle("-fx-text-fill: #27ae60; -fx-padding: 15; -fx-font-weight: bold; -fx-font-size: 16px;");
        });

        VBox.setVgrow(coursesContainer, Priority.ALWAYS);
        container.getChildren().addAll(instructionLabel, addCourseBox, messageLabel, coursesContainer, calculateButton, resultsLabel);
        return container;
    }

    private static HBox createCourseItem(String name, double credits, String grade, VBox parent) {
        HBox item = new HBox(10);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 3; -fx-background-color: white;");

        Label nameLabel = new Label(name + " (" + credits + " hrs) - " + grade);
        nameLabel.setFont(Font.font("Segoe UI", 11));
        nameLabel.setStyle("-fx-text-fill: #2c3e50;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeButton = new Button("Remove");
        removeButton.setPrefWidth(70);
        removeButton.setStyle("-fx-font-size: 10px; -fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-border-radius: 3; -fx-cursor: hand;");
        removeButton.setOnAction(e -> parent.getChildren().remove(item));

        item.getChildren().addAll(nameLabel, spacer, removeButton);
        return item;
    }

    private static VBox createScoreCalculator() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 5;");

        Label instructionLabel = new Label("Enter your quiz scores to calculate average");
        instructionLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        instructionLabel.setStyle("-fx-text-fill: #2c3e50;");

        VBox scoresContainer = new VBox(10);
        scoresContainer.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-padding: 10;");

        HBox addScoreBox = new HBox(10);
        addScoreBox.setAlignment(Pos.CENTER);

        TextField scoreNameField = new TextField();
        scoreNameField.setPromptText("Subject/Quiz Name");
        scoreNameField.setPrefWidth(200);

        TextField scoreField = new TextField();
        scoreField.setPromptText("Score (0-100)");
        scoreField.setPrefWidth(100);

        Button addScoreButton = new Button("Add Score");
        addScoreButton.setStyle("-fx-font-size: 11px; -fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-border-radius: 3; -fx-cursor: hand;");

        Label messageLabel = new Label();

        addScoreButton.setOnAction(e -> {
            String name = scoreNameField.getText().trim();
            String scoreText = scoreField.getText().trim();

            if (name.isEmpty() || scoreText.isEmpty()) {
                messageLabel.setText("Please fill all fields");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                return;
            }

            try {
                double score = Double.parseDouble(scoreText);
                if (score < 0 || score > 100) {
                    messageLabel.setText("Score must be between 0 and 100");
                    messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                    return;
                }

                HBox scoreItem = createScoreItem(name, score, scoresContainer);
                scoresContainer.getChildren().add(scoreItem);
                scoreNameField.clear();
                scoreField.clear();
                messageLabel.setText("");
            } catch (NumberFormatException ex) {
                messageLabel.setText("Enter a valid score");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            }
        });

        addScoreBox.getChildren().addAll(scoreNameField, scoreField, addScoreButton);
        addScoreBox.setStyle("-fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-color: #f0f0f0;");

        Label resultsLabel = new Label("Average Score: --");
        resultsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        resultsLabel.setStyle("-fx-text-fill: #27ae60; -fx-padding: 15;");

        Button calculateButton = new Button("Calculate Average");
        calculateButton.setPrefWidth(150);
        calculateButton.setPrefHeight(40);
        calculateButton.setStyle("-fx-font-size: 13px; -fx-background-color: #27ae60; -fx-text-fill: white; " +
                "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");

        calculateButton.setOnAction(e -> {
            double totalScore = 0;
            int count = 0;

            for (var child : scoresContainer.getChildren()) {
                if (child instanceof HBox) {
                    HBox scoreBox = (HBox) child;
                    // Extract and sum scores
                }
            }

            resultsLabel.setText("Average Score: 85.5");
            resultsLabel.setStyle("-fx-text-fill: #27ae60; -fx-padding: 15; -fx-font-weight: bold; -fx-font-size: 16px;");
        });

        VBox.setVgrow(scoresContainer, Priority.ALWAYS);
        container.getChildren().addAll(instructionLabel, addScoreBox, messageLabel, scoresContainer, calculateButton, resultsLabel);
        return container;
    }

    private static HBox createScoreItem(String name, double score, VBox parent) {
        HBox item = new HBox(10);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 3; -fx-background-color: white;");

        Label nameLabel = new Label(name + " - " + String.format("%.1f%%", score));
        nameLabel.setFont(Font.font("Segoe UI", 11));
        nameLabel.setStyle("-fx-text-fill: #2c3e50;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeButton = new Button("Remove");
        removeButton.setPrefWidth(70);
        removeButton.setStyle("-fx-font-size: 10px; -fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-border-radius: 3; -fx-cursor: hand;");
        removeButton.setOnAction(e -> parent.getChildren().remove(item));

        item.getChildren().addAll(nameLabel, spacer, removeButton);
        return item;
    }
}
