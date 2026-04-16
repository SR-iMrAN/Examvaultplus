package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.QuizResultModel;
import models.StudentModel;
import services.AuthService;
import services.StudentService;
import services.TeacherService;
import java.util.List;

public class StudentDashboard {
    private SceneManager sceneManager;
    private AuthService authService;
    private StudentService studentService;
    private TeacherService teacherService;
    private VBox root;

    public StudentDashboard(SceneManager sceneManager, AuthService authService) {
        this.sceneManager = sceneManager;
        this.authService = authService;
        this.studentService = StudentService.getInstance();
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

        // Take Quiz Tab
        Tab quizTab = new Tab("Take Quiz", createQuizView());
        quizTab.setStyle("-fx-padding: 15px;");

        // Results Tab
        Tab resultsTab = new Tab("My Results", createResultsView());
        resultsTab.setStyle("-fx-padding: 15px;");

        // Tools Tab
        Tab toolsTab = new Tab("Tools", createToolsView());
        toolsTab.setStyle("-fx-padding: 15px;");

        tabPane.getTabs().addAll(quizTab, resultsTab, toolsTab);

        VBox.setVgrow(tabPane, Priority.ALWAYS);
        root.getChildren().addAll(header, tabPane);
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #34495e); -fx-border-color: #bdc3c7; -fx-border-width: 0 0 2 0;");
        header.setAlignment(Pos.CENTER_LEFT);

        StudentModel student = authService.getCurrentStudent();
        
        VBox userInfo = new VBox(5);
        Label welcomeLabel = new Label("Welcome, " + student.getName() + " ✦");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        welcomeLabel.setStyle("-fx-text-fill: white;");
        
        Label idLabel = new Label("ID: " + student.getUsername());
        idLabel.setFont(Font.font("Segoe UI", 12));
        idLabel.setStyle("-fx-text-fill: #ecf0f1;");
        
        userInfo.getChildren().addAll(welcomeLabel, idLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button calculatorButton = createToolButton("Calculator", "#9b59b6");
        Button cgpaButton = createToolButton("CGPA", "#8e44ad");
        Button logoutButton = createLogoutButton();

        header.getChildren().addAll(userInfo, spacer, calculatorButton, cgpaButton, logoutButton);
        return header;
    }

    private VBox createQuizView() {
        VBox quizView = new VBox(15);
        quizView.setPadding(new Insets(20));
        quizView.setStyle("-fx-background-color: white;");

        Label title = new Label("Available Quizzes");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #2c3e50;");

        VBox quizzesContainer = new VBox(10);
        List<String> subjects = teacherService.getAllSubjects();

        if (subjects.isEmpty()) {
            Label noQuizzesLabel = new Label("No quizzes available at the moment");
            noQuizzesLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
            quizzesContainer.getChildren().add(noQuizzesLabel);
        } else {
            for (String subject : subjects) {
                HBox quizCard = createQuizCard(subject);
                quizzesContainer.getChildren().add(quizCard);
            }
        }

        ScrollPane scrollPane = new ScrollPane(quizzesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        quizView.getChildren().addAll(title, scrollPane);
        return quizView;
    }

    private HBox createQuizCard(String subject) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-color: white;");

        VBox subjectInfo = new VBox(5);
        Label subjectLabel = new Label(subject);
        subjectLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        subjectLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label descriptionLabel = new Label("Click 'Start Quiz' to begin");
        descriptionLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
        
        subjectInfo.getChildren().addAll(subjectLabel, descriptionLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button startButton = new Button("Start Quiz");
        startButton.setPrefWidth(120);
        startButton.setPrefHeight(35);
        startButton.setStyle("-fx-font-size: 12px; -fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        startButton.setOnMouseEntered(e -> startButton.setStyle("-fx-font-size: 12px; -fx-background-color: #2980b9; " +
                "-fx-text-fill: white; -fx-border-radius: 5;"));
        startButton.setOnMouseExited(e -> startButton.setStyle("-fx-font-size: 12px; -fx-background-color: #3498db; " +
                "-fx-text-fill: white; -fx-border-radius: 5;"));
        startButton.setOnAction(e -> sceneManager.showQuizView(subject));

        card.getChildren().addAll(subjectInfo, spacer, startButton);
        return card;
    }

    private VBox createResultsView() {
        VBox resultsView = new VBox(15);
        resultsView.setPadding(new Insets(20));
        resultsView.setStyle("-fx-background-color: white;");

        Label title = new Label("Quiz Results");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #2c3e50;");

        StudentModel student = authService.getCurrentStudent();
        List<QuizResultModel> results = studentService.getStudentResults(student.getUsername());

        VBox resultsContainer = new VBox(10);

        if (results.isEmpty()) {
            Label noResultsLabel = new Label("No quiz results yet");
            noResultsLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
            resultsContainer.getChildren().add(noResultsLabel);
        } else {
            for (QuizResultModel result : results) {
                HBox resultCard = createResultCard(result);
                resultsContainer.getChildren().add(resultCard);
            }
        }

        ScrollPane scrollPane = new ScrollPane(resultsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        resultsView.getChildren().addAll(title, scrollPane);
        return resultsView;
    }

    private HBox createResultCard(QuizResultModel result) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-color: white;");

        VBox resultInfo = new VBox(5);
        Label subjectLabel = new Label(result.getSubject());
        subjectLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        subjectLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label scoreLabel = new Label("Score: " + result.getScore() + "/" + result.getTotalQuestions() + 
                " (" + String.format("%.1f%%", result.getPercentage()) + ")");
        scoreLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
        
        resultInfo.getChildren().addAll(subjectLabel, scoreLabel);
        card.getChildren().add(resultInfo);
        return card;
    }

    private VBox createToolsView() {
        VBox toolsView = new VBox(15);
        toolsView.setPadding(new Insets(20));
        toolsView.setStyle("-fx-background-color: white;");

        Label title = new Label("Study Tools");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setStyle("-fx-text-fill: #2c3e50;");

        VBox toolsContainer = new VBox(10);

        HBox calcCard = createToolCard("Calculator", "Basic arithmetic operations", "calculator", "#e74c3c");
        HBox cgpaCard = createToolCard("CGPA Calculator", "Calculate your GPA", "cgpa", "#f39c12");

        toolsContainer.getChildren().addAll(calcCard, cgpaCard);

        VBox.setVgrow(toolsContainer, Priority.ALWAYS);
        toolsView.getChildren().addAll(title, toolsContainer);
        return toolsView;
    }

    private HBox createToolCard(String name, String description, String type, String color) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-border-color: " + color + "; -fx-border-radius: 8; -fx-border-width: 2; -fx-background-color: white;");

        VBox toolInfo = new VBox(5);
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        nameLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
        
        toolInfo.getChildren().addAll(nameLabel, descLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button openButton = new Button("Open");
        openButton.setPrefWidth(100);
        openButton.setPrefHeight(35);
        openButton.setStyle("-fx-font-size: 12px; -fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        openButton.setOnMouseEntered(e -> openButton.setStyle("-fx-font-size: 12px; -fx-background-color: " +
                adjustColor(color, -30) + "; -fx-text-fill: white; -fx-border-radius: 5;"));
        openButton.setOnMouseExited(e -> openButton.setStyle("-fx-font-size: 12px; -fx-background-color: " + color +
                "; -fx-text-fill: white; -fx-border-radius: 5;"));

        openButton.setOnAction(e -> {
            if ("calculator".equals(type)) {
                CalculatorView.showCalculator();
            } else if ("cgpa".equals(type)) {
                CGPACalculatorView.showCGPACalculator();
            }
        });

        card.getChildren().addAll(toolInfo, spacer, openButton);
        return card;
    }

    private Button createToolButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefHeight(35);
        button.setStyle("-fx-font-size: 12px; -fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 12px; -fx-background-color: " +
                adjustColor(color, -30) + "; -fx-text-fill: white; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 12px; -fx-background-color: " + color +
                "; -fx-text-fill: white; -fx-border-radius: 5;"));

        if ("Calculator".equals(text)) {
            button.setOnAction(e -> CalculatorView.showCalculator());
        } else if ("CGPA".equals(text)) {
            button.setOnAction(e -> CGPACalculatorView.showCGPACalculator());
        }

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

    private String adjustColor(String color, int adjustment) {
        // Simple color adjustment helper
        return color;
    }

    public VBox getView() {
        return root;
    }
}
