package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import models.QuestionModel;
import models.StudentModel;
import services.AuthService;
import services.QuizService;
import services.StudentService;
import java.util.ArrayList;
import java.util.List;

public class QuizView {
    private SceneManager sceneManager;
    private AuthService authService;
    private QuizService quizService;
    private StudentService studentService;
    private String subject;
    private List<QuestionModel> questions;
    private List<Integer> userAnswers;
    private int currentQuestionIndex = 0;
    private VBox root;

    public QuizView(SceneManager sceneManager, AuthService authService, String subject) {
        this.sceneManager = sceneManager;
        this.authService = authService;
        this.quizService = QuizService.getInstance();
        this.studentService = StudentService.getInstance();
        this.subject = subject;
        this.questions = quizService.getQuestionsBySubject(subject);
        this.userAnswers = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            userAnswers.add(0);
        }
        this.root = new VBox(0);
this.root.getStyleClass().addAll("root-cyber", "glass-panel");

        createUI();
    }

    private void createUI() {
        // Header
        HBox header = createHeader();
        
        // Main quiz content
        VBox quizContent = new VBox(15);
        quizContent.setPadding(new Insets(20));
        quizContent.setStyle("-fx-background-color: white;");

        if (questions.isEmpty()) {
            Label noQuestionsLabel = new Label("No questions available for this subject");
            noQuestionsLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px;");
            quizContent.getChildren().add(noQuestionsLabel);
        } else {
            VBox questionContainer = createQuestionView();
            VBox.setVgrow(questionContainer, Priority.ALWAYS);
            quizContent.getChildren().add(questionContainer);
        }

        VBox.setVgrow(quizContent, Priority.ALWAYS);
        root.getChildren().addAll(header, quizContent);
    }

    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: linear-gradient(to right, #3498db, #2980b9); -fx-border-color: #bdc3c7; -fx-border-width: 0 0 2 0;");
        header.setAlignment(Pos.CENTER_LEFT);

        VBox quizInfo = new VBox(5);
        Label quizTitle = new Label(subject + " Quiz");
        quizTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        quizTitle.setStyle("-fx-text-fill: white;");
        
        Label countLabel = new Label("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
        countLabel.setFont(Font.font("Segoe UI", 12));
        countLabel.setStyle("-fx-text-fill: #ecf0f1;");
        
        quizInfo.getChildren().addAll(quizTitle, countLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exitButton = new Button("Exit Quiz");
        exitButton.setPrefHeight(35);
        exitButton.setStyle("-fx-font-size: 12px; -fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-font-size: 12px; -fx-background-color: #c0392b; " +
                "-fx-text-fill: white; -fx-border-radius: 5;"));
        exitButton.setOnMouseExited(e -> exitButton.setStyle("-fx-font-size: 12px; -fx-background-color: #e74c3c; " +
                "-fx-text-fill: white; -fx-border-radius: 5;"));
        exitButton.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Exit Quiz");
            confirm.setHeaderText("Exit Quiz");
            confirm.setContentText("Are you sure you want to exit this quiz? Your progress will be lost.");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                sceneManager.showStudentDashboard();
            }
        });

        header.getChildren().addAll(quizInfo, spacer, exitButton);
        return header;
    }

    private VBox createQuestionView() {
        VBox questionView = new VBox(20);
        questionView.setPadding(new Insets(20));
        questionView.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8;");

        QuestionModel currentQuestion = questions.get(currentQuestionIndex);

        // Progress bar
        ProgressBar progressBar = new ProgressBar((double) (currentQuestionIndex + 1) / questions.size());
        progressBar.setPrefHeight(8);
        progressBar.setStyle("-fx-control-inner-background: #ecf0f1;");

        // Question text
        Label questionLabel = new Label("Q" + (currentQuestionIndex + 1) + ": " + currentQuestion.getQuestionText());
        questionLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        questionLabel.setStyle("-fx-text-fill: #2c3e50; -fx-wrap-text: true;");
        questionLabel.setWrapText(true);

        // Options
        VBox optionsContainer = new VBox(10);
        optionsContainer.setStyle("-fx-padding: 15; -fx-border-color: #ecf0f1; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

        ToggleGroup answerGroup = new ToggleGroup();
        
        for (int i = 0; i < currentQuestion.getOptions().length; i++) {
            final int optionIndex = i + 1;
            HBox optionBox = new HBox(10);
            optionBox.setAlignment(Pos.CENTER_LEFT);
            optionBox.setPadding(new Insets(10));
            optionBox.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 5; -fx-background-color: white;");

            RadioButton radioButton = new RadioButton(currentQuestion.getOptions()[i]);
            radioButton.setToggleGroup(answerGroup);
            radioButton.setStyle("-fx-font-size: 12px;");
            
            if (userAnswers.get(currentQuestionIndex) == optionIndex) {
                radioButton.setSelected(true);
            }

            radioButton.setOnAction(e -> userAnswers.set(currentQuestionIndex, optionIndex));

            optionBox.getChildren().add(radioButton);
            optionsContainer.getChildren().add(optionBox);
        }

        // Navigation buttons
        HBox navigationBox = new HBox(10);
        navigationBox.setAlignment(Pos.CENTER);

        Button prevButton = new Button("← Previous");
        prevButton.setPrefWidth(120);
        prevButton.setPrefHeight(40);
        prevButton.setStyle("-fx-font-size: 12px; -fx-background-color: #95a5a6; -fx-text-fill: white; " +
                "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        prevButton.setDisable(currentQuestionIndex == 0);
        prevButton.setOnMouseEntered(e -> {
            if (!prevButton.isDisabled()) {
                prevButton.setStyle("-fx-font-size: 12px; -fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-border-radius: 5;");
            }
        });
        prevButton.setOnMouseExited(e -> {
            if (!prevButton.isDisabled()) {
                prevButton.setStyle("-fx-font-size: 12px; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-border-radius: 5;");
            }
        });
        prevButton.setOnAction(e -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                updateQuizView();
            }
        });

        Button nextButton = new Button("Next →");
        nextButton.setPrefWidth(120);
        nextButton.setPrefHeight(40);
        nextButton.setStyle("-fx-font-size: 12px; -fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        nextButton.setOnMouseEntered(e -> nextButton.setStyle("-fx-font-size: 12px; -fx-background-color: #2980b9; " +
                "-fx-text-fill: white; -fx-border-radius: 5;"));
        nextButton.setOnMouseExited(e -> nextButton.setStyle("-fx-font-size: 12px; -fx-background-color: #3498db; " +
                "-fx-text-fill: white; -fx-border-radius: 5;"));
        nextButton.setOnAction(e -> {
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                updateQuizView();
            } else {
                finishQuiz();
            }
        });

        if (currentQuestionIndex == questions.size() - 1) {
            nextButton.setText("Finish Quiz");
            nextButton.setStyle("-fx-font-size: 12px; -fx-background-color: #27ae60; -fx-text-fill: white; " +
                    "-fx-border-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        }

        navigationBox.getChildren().addAll(prevButton, nextButton);

        questionView.getChildren().addAll(progressBar, questionLabel, optionsContainer, navigationBox);
        return questionView;
    }

    private void updateQuizView() {
        root.getChildren().clear();
        createUI();
    }

    private void finishQuiz() {
        int score = quizService.calculateScore(questions, userAnswers);
        StudentModel student = authService.getCurrentStudent();
        studentService.saveQuizResult(student.getUsername(), subject, score, questions.size());

        // Show results dialog
        Alert resultsAlert = new Alert(Alert.AlertType.INFORMATION);
        resultsAlert.setTitle("Quiz Complete");
        resultsAlert.setHeaderText("Quiz Results");
        resultsAlert.setContentText("Your Score: " + score + "/" + questions.size() + 
                " (" + String.format("%.1f%%", (double)score/questions.size()*100) + ")");
        resultsAlert.showAndWait();

        sceneManager.showStudentDashboard();
    }

    public VBox getView() {
        return root;
    }
}
