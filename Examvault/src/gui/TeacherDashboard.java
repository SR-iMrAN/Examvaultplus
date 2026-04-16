package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.QuizResultModel;
import models.TeacherModel;
import services.AuthService;
import services.TeacherService;
import java.util.List;

public class TeacherDashboard {
    private SceneManager sceneManager;
    private AuthService authService;
    private TeacherService teacherService;
    private VBox root;

    public TeacherDashboard(SceneManager sceneManager, AuthService authService) {
        this.sceneManager = sceneManager;
        this.authService = authService;
        this.teacherService = TeacherService.getInstance();
        this.root = new VBox(0);
this.root.getStyleClass().addAll("root-cyber", "glass-panel");

        createUI();
    }

    private void createUI() {
        // Header with user info and logout
        HBox header = createHeader();
        
        // Main content with tabs
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-size: 13px;");

        // Manage Subjects Tab
        Tab subjectsTab = new Tab("Manage Subjects", createSubjectsView());

        // Add Questions Tab
        Tab questionsTab = new Tab("Add Questions", createQuestionsView());

        // View Results Tab
        Tab resultsTab = new Tab("View Results", createResultsView());

        tabPane.getTabs().addAll(subjectsTab, questionsTab, resultsTab);

        VBox.setVgrow(tabPane, Priority.ALWAYS);
        root.getChildren().addAll(header, tabPane);
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #34495e); -fx-border-color: #bdc3c7; -fx-border-width: 0 0 2 0;");
        header.setAlignment(Pos.CENTER_LEFT);

        TeacherModel teacher = authService.getCurrentTeacher();
        
        VBox userInfo = new VBox(5);
        Label welcomeLabel = new Label("Welcome, " + teacher.getName() + " ✦");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        welcomeLabel.setStyle("-fx-text-fill: white;");
        
        Label roleLabel = new Label("Teacher | ID: " + teacher.getUsername());
        roleLabel.setFont(Font.font("Segoe UI", 12));
        roleLabel.setStyle("-fx-text-fill: #ecf0f1;");
        
        userInfo.getChildren().addAll(welcomeLabel, roleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = createLogoutButton();

        header.getChildren().addAll(userInfo, spacer, logoutButton);
        return header;
    }

    private VBox createSubjectsView() {
        VBox subjectsView = new VBox(15);
        subjectsView.setPadding(new Insets(20));
        subjectsView.setStyle("-fx-background-color: white;");

        Label title = new Label("Manage Subjects");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #2c3e50;");

        HBox addSubjectBox = new HBox(10);
        addSubjectBox.setPadding(new Insets(15));
        addSubjectBox.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-color: #f9f9f9;");
        addSubjectBox.setAlignment(Pos.CENTER_LEFT);

        TextField subjectField = new TextField();
        subjectField.setPromptText("Enter subject name");
        subjectField.setStyle("-fx-padding: 10px; -fx-font-size: 12px; -fx-border-radius: 5;");
        subjectField.setPrefWidth(300);

        Button addButton = createPrimaryButton("Add Subject", "#27ae60");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 11px;");

        addButton.setOnAction(e -> {
            String subjectName = subjectField.getText().trim();
            if (subjectName.isEmpty()) {
                messageLabel.setText("Please enter a subject name");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                return;
            }

            if (teacherService.addSubject(subjectName)) {
                messageLabel.setText("Subject added successfully!");
                messageLabel.setStyle("-fx-text-fill: #27ae60;");
                subjectField.clear();
                updateSubjectsList(subjectsView);
            } else {
                messageLabel.setText("Subject already exists");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            }
        });

        addSubjectBox.getChildren().addAll(subjectField, addButton, messageLabel);

        Label allSubjectsLabel = new Label("All Subjects");
        allSubjectsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        allSubjectsLabel.setStyle("-fx-text-fill: #2c3e50;");

        VBox subjectsListContainer = new VBox(8);
        subjectsListContainer.setStyle("-fx-background-color: #f5f5f5;");
        updateSubjectsList(subjectsView);

        for (String subject : teacherService.getAllSubjects()) {
            HBox subjectItem = new HBox(10);
            subjectItem.setPadding(new Insets(10, 15, 10, 15));
            subjectItem.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-color: white;");
            
            Label subjectLabel = new Label("• " + subject);
            subjectLabel.setFont(Font.font("Segoe UI", 12));
            subjectLabel.setStyle("-fx-text-fill: #2c3e50;");
            
            subjectsListContainer.getChildren().add(subjectItem);
            subjectItem.getChildren().add(subjectLabel);
        }

        ScrollPane scrollPane = new ScrollPane(subjectsListContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        subjectsView.getChildren().addAll(title, addSubjectBox, allSubjectsLabel, scrollPane);
        return subjectsView;
    }

    private VBox createQuestionsView() {
        VBox questionsView = new VBox(15);
        questionsView.setPadding(new Insets(20));
        questionsView.setStyle("-fx-background-color: white;");

        Label title = new Label("Add Questions");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #2c3e50;");

        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-color: #f9f9f9;");

        Label subjectLabel = new Label("Select Subject:");
        subjectLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        ComboBox<String> subjectCombo = new ComboBox<>();
        subjectCombo.getItems().addAll(teacherService.getAllSubjects());
        subjectCombo.setStyle("-fx-min-width: 300px;");

        Label questionLabel = new Label("Question Text:");
        questionLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        TextArea questionArea = new TextArea();
        questionArea.setWrapText(true);
        questionArea.setPrefRowCount(3);
        questionArea.setStyle("-fx-control-inner-background: white; -fx-font-size: 12px;");

        Label optionsLabel = new Label("Options (one per line):");
        optionsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        TextArea optionsArea = new TextArea();
        optionsArea.setWrapText(true);
        optionsArea.setPrefRowCount(4);
        optionsArea.setStyle("-fx-control-inner-background: white; -fx-font-size: 12px;");

        Label answerLabel = new Label("Correct Answer (select from options):");
        answerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        ComboBox<String> answerCombo = new ComboBox<>();
        answerCombo.setStyle("-fx-min-width: 300px;");

        optionsArea.textProperty().addListener((obs, oldVal, newVal) -> {
            String[] options = newVal.split("\n");
            answerCombo.getItems().clear();
            for (String option : options) {
                if (!option.trim().isEmpty()) {
                    answerCombo.getItems().add(option.trim());
                }
            }
        });

        Button addQuestionButton = createPrimaryButton("Add Question", "#3498db");
        Label messageLabel = new Label();

        addQuestionButton.setOnAction(e -> {
            String subject = subjectCombo.getValue();
            String question = questionArea.getText().trim();
            String optionsText = optionsArea.getText().trim();
            String answer = answerCombo.getValue();

            if (subject == null || subject.isEmpty() || question.isEmpty() || optionsText.isEmpty() || answer == null) {
                messageLabel.setText("Please fill all fields");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                return;
            }

            String[] options = optionsText.split("\n");
            if (options.length < 2) {
                messageLabel.setText("Question must have at least 2 options");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                return;
            }

            // Trim all options
            for (int i = 0; i < options.length; i++) {
                options[i] = options[i].trim();
            }

            if (teacherService.addQuestion(subject, question, options, answer)) {
                messageLabel.setText("Question added successfully!");
                messageLabel.setStyle("-fx-text-fill: #27ae60;");
                questionArea.clear();
                optionsArea.clear();
                answerCombo.getItems().clear();
            } else {
                messageLabel.setText("Error adding question");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            }
        });

        formContainer.getChildren().addAll(
                subjectLabel, subjectCombo,
                questionLabel, questionArea,
                optionsLabel, optionsArea,
                answerLabel, answerCombo,
                addQuestionButton, messageLabel
        );

        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        questionsView.getChildren().addAll(title, scrollPane);
        return questionsView;
    }

    private VBox createResultsView() {
        VBox resultsView = new VBox(15);
        resultsView.setPadding(new Insets(20));
        resultsView.setStyle("-fx-background-color: white;");

        Label title = new Label("Student Results");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #2c3e50;");

        Label filterLabel = new Label("Filter by Subject:");
        filterLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        ComboBox<String> subjectFilterCombo = new ComboBox<>();
        subjectFilterCombo.getItems().add("All Subjects");
        subjectFilterCombo.getItems().addAll(teacherService.getAllSubjects());
        subjectFilterCombo.setValue("All Subjects");
        subjectFilterCombo.setStyle("-fx-min-width: 300px;");

        VBox resultsContainer = new VBox(8);
        resultsContainer.setStyle("-fx-background-color: #f5f5f5;");

        subjectFilterCombo.setOnAction(e -> {
            resultsContainer.getChildren().clear();
            String selectedSubject = subjectFilterCombo.getValue();

            List<QuizResultModel> results;
            if ("All Subjects".equals(selectedSubject)) {
                results = teacherService.getResultRepository().getResultsBySubject("");
            } else {
                results = teacherService.getResultsBySubject(selectedSubject);
            }

            if (results.isEmpty()) {
                Label noResultsLabel = new Label("No results found");
                noResultsLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
                resultsContainer.getChildren().add(noResultsLabel);
            } else {
                for (QuizResultModel result : results) {
                    HBox resultCard = createResultCard(result);
                    resultsContainer.getChildren().add(resultCard);
                }
            }
        });

        // Initial load
        subjectFilterCombo.getOnAction().handle(null);

        ScrollPane scrollPane = new ScrollPane(resultsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        resultsView.getChildren().addAll(title, filterLabel, subjectFilterCombo, scrollPane);
        return resultsView;
    }

    private HBox createResultCard(QuizResultModel result) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(12, 15, 12, 15));
        card.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-color: white;");

        VBox resultInfo = new VBox(4);
        Label studentLabel = new Label("Student: " + result.getStudentId());
        studentLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        studentLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label subjectLabel = new Label(result.getSubject());
        subjectLabel.setFont(Font.font("Segoe UI", 11));
        subjectLabel.setStyle("-fx-text-fill: #7f8c8d;");
        
        Label scoreLabel = new Label("Score: " + result.getScore() + "/" + result.getTotalQuestions() + 
                " (" + String.format("%.1f%%", result.getPercentage()) + ")");
        scoreLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
        
        resultInfo.getChildren().addAll(studentLabel, subjectLabel, scoreLabel);
        card.getChildren().add(resultInfo);
        return card;
    }

    private void updateSubjectsList(VBox parent) {
        // This method can be called to refresh the subjects list
    }

    private Button createPrimaryButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefHeight(40);
        button.setStyle("-fx-font-size: 13px; -fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold; -fx-padding: 10px 20px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 13px; -fx-background-color: " +
                color + "; -fx-text-fill: white; -fx-border-radius: 5; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-opacity: 0.8;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 13px; -fx-background-color: " + color +
                "; -fx-text-fill: white; -fx-border-radius: 5; -fx-font-weight: bold; -fx-padding: 10px 20px;"));
        return button;
    }

    private Button createLogoutButton() {
        Button logoutButton = new Button("Logout");
        logoutButton.setPrefHeight(35);
        logoutButton.setStyle("-fx-font-size: 12px; -fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-font-size: 12px; -fx-background-color: #c0392b; " +
                "-fx-text-fill: white; -fx-border-radius: 5;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-font-size: 12px; -fx-background-color: #e74c3c; " +
                "-fx-text-fill: white; -fx-border-radius: 5;"));
        logoutButton.setOnAction(e -> {
            authService.logout();
            sceneManager.showLoginView();
        });

        return logoutButton;
    }

    public VBox getView() {
        return root;
    }
}
