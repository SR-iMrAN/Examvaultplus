package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.QuestionModel;
import models.StudentModel;
import repositories.QuestionRepository;
import repositories.ResultRepository;

import java.util.List;

public class QuizView {

    private BorderPane root;
    private StudentModel student;
    private String subject;
    private List<QuestionModel> questions;
    private int currentIndex = 0;
    private String[] selectedAnswers;
    private boolean quizFinished = false;

    // UI refs
    private Label questionLabel;
    private VBox optionsBox;
    private Label progressLabel;
    private ProgressBar progressBar;
    private Button prevBtn;
    private Button nextBtn;
    private Button submitBtn;
    private StackPane centerPane;

    public QuizView(StudentModel student, String subject) {
        this.student = student;
        this.subject = subject;
        this.questions = QuestionRepository.loadForSubject(subject);
        if (!questions.isEmpty()) {
            selectedAnswers = new String[questions.size()];
        }
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0f1117;");

        if (questions.isEmpty()) {
            root.setCenter(buildEmptyState());
        } else {
            buildQuizUI();
        }
    }

    private VBox buildEmptyState() {
        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(60));
        Label icon = new Label("❓");
        icon.setStyle("-fx-font-size: 48px;");
        Label msg = new Label("No questions found for: " + subject);
        msg.getStyleClass().add("heading-md");
        Label hint = new Label("Ask your teacher to add questions for this subject.");
        hint.getStyleClass().add("text-secondary");
        Button back = new Button("← Go Back");
        back.getStyleClass().add("btn-secondary");
        back.setOnAction(e -> SceneManager.showStudentDashboard(student));
        box.getChildren().addAll(icon, msg, hint, back);
        return box;
    }

    private void buildQuizUI() {
        // Top bar
        HBox topBar = new HBox(16);
        topBar.setStyle("-fx-background-color: #161b22; -fx-border-color: transparent transparent #30363d transparent; -fx-padding: 12px 28px;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("← Dashboard");
        backBtn.getStyleClass().add("btn-ghost");
        backBtn.setOnAction(e -> {
            if (!quizFinished) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Quit Quiz");
                alert.setHeaderText("Are you sure you want to quit?");
                alert.setContentText("Your progress will be lost.");
                styleDialog(alert.getDialogPane());
                alert.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.OK) SceneManager.showStudentDashboard(student);
                });
            } else {
                SceneManager.showStudentDashboard(student);
            }
        });

        VBox titleBox = new VBox(2);
        Label subjectLabel = new Label(subject);
        subjectLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #e6edf3;");
        progressLabel = new Label("Question 1 of " + questions.size());
        progressLabel.getStyleClass().add("text-secondary");
        titleBox.getChildren().addAll(subjectLabel, progressLabel);

        HBox spacer = new HBox(); HBox.setHgrow(spacer, Priority.ALWAYS);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(200);
        progressBar.setPrefHeight(6);
        progressBar.getStyleClass().add("progress-bar");

        topBar.getChildren().addAll(backBtn, titleBox, spacer, progressBar);

        // Center: question card
        centerPane = new StackPane();
        centerPane.setStyle("-fx-background-color: #0f1117;");
        centerPane.setPadding(new Insets(32));
        centerPane.setAlignment(Pos.CENTER);

        buildQuestionCard();
        root.setTop(topBar);
        root.setCenter(centerPane);
    }

    private void buildQuestionCard() {
        VBox outerBox = new VBox(20);
        outerBox.setAlignment(Pos.CENTER);
        outerBox.setMaxWidth(640);

        // Question number badge
        HBox badgeRow = new HBox(8);
        badgeRow.setAlignment(Pos.CENTER_LEFT);
        Label qNumBadge = new Label("Q" + (currentIndex + 1));
        qNumBadge.getStyleClass().add("badge");
        Label ofTotal = new Label("of " + questions.size());
        ofTotal.getStyleClass().add("text-muted");
        badgeRow.getChildren().addAll(qNumBadge, ofTotal);

        // Question card
        VBox card = new VBox(20);
        card.getStyleClass().add("quiz-question-card");

        QuestionModel q = questions.get(currentIndex);

        questionLabel = new Label(q.getQuestionText());
        questionLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #e6edf3;");
        questionLabel.setWrapText(true);

        optionsBox = new VBox(10);
        String[] options = q.getOptions();
        for (int i = 0; i < options.length; i++) {
            final int idx = i;
            String optText = (char)('A' + i) + ".  " + options[i].trim();
            Button optBtn = new Button(optText);
            optBtn.setMaxWidth(Double.MAX_VALUE);

            // Restore selection if already answered
            if (selectedAnswers[currentIndex] != null &&
                    options[i].trim().equalsIgnoreCase(selectedAnswers[currentIndex].trim())) {
                optBtn.getStyleClass().add("quiz-option-selected");
            } else {
                optBtn.getStyleClass().add("quiz-option-btn");
            }

            optBtn.setOnAction(e -> {
                selectedAnswers[currentIndex] = options[idx].trim();
                refreshOptionStyles(options);
                updateNavButtons();
            });
            optionsBox.getChildren().add(optBtn);
        }

        card.getChildren().addAll(questionLabel, optionsBox);

        // Navigation row
        HBox navRow = new HBox(12);
        navRow.setAlignment(Pos.CENTER_RIGHT);

        prevBtn = new Button("← Previous");
        prevBtn.getStyleClass().add("btn-secondary");
        prevBtn.setDisable(currentIndex == 0);

        nextBtn = new Button("Next →");
        nextBtn.getStyleClass().add("btn-primary");
        nextBtn.setVisible(currentIndex < questions.size() - 1);
        nextBtn.setManaged(currentIndex < questions.size() - 1);

        submitBtn = new Button("Submit Quiz ✓");
        submitBtn.getStyleClass().add("btn-success");
        submitBtn.setVisible(currentIndex == questions.size() - 1);
        submitBtn.setManaged(currentIndex == questions.size() - 1);

        Button calcBtn = new Button("🧮 Calculator");
        calcBtn.getStyleClass().add("btn-secondary");
        calcBtn.setOnAction(e -> openCalculator());

        HBox spacer = new HBox(); HBox.setHgrow(spacer, Priority.ALWAYS);
        navRow.getChildren().addAll(prevBtn, spacer, calcBtn, nextBtn, submitBtn);

        prevBtn.setOnAction(e -> { if (currentIndex > 0) { currentIndex--; refreshCard(); } });
        nextBtn.setOnAction(e -> { if (currentIndex < questions.size() - 1) { currentIndex++; refreshCard(); } });
        submitBtn.setOnAction(e -> submitQuiz());

        outerBox.getChildren().addAll(badgeRow, card, navRow);
        updateProgress();

        centerPane.getChildren().setAll(outerBox);
    }

    private void refreshCard() {
        buildQuestionCard();
    }

    private void refreshOptionStyles(String[] options) {
        for (int i = 0; i < optionsBox.getChildren().size(); i++) {
            Button btn = (Button) optionsBox.getChildren().get(i);
            btn.getStyleClass().clear();
            if (selectedAnswers[currentIndex] != null &&
                    options[i].trim().equalsIgnoreCase(selectedAnswers[currentIndex].trim())) {
                btn.getStyleClass().add("quiz-option-selected");
            } else {
                btn.getStyleClass().add("quiz-option-btn");
            }
        }
    }

    private void updateNavButtons() {
        // nothing extra needed
    }

    private void updateProgress() {
        int answered = 0;
        for (String s : selectedAnswers) if (s != null) answered++;
        double progress = (double)(currentIndex + 1) / questions.size();
        progressBar.setProgress(progress);
        progressLabel.setText("Question " + (currentIndex + 1) + " of " + questions.size()
                + "   |   Answered: " + answered + "/" + questions.size());
    }

    private void submitQuiz() {
        // Count unanswered
        int unanswered = 0;
        for (String s : selectedAnswers) if (s == null) unanswered++;

        if (unanswered > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unanswered Questions");
            alert.setHeaderText(unanswered + " question(s) unanswered");
            alert.setContentText("Do you want to submit anyway? Unanswered questions count as wrong.");
            styleDialog(alert.getDialogPane());
            alert.showAndWait().ifPresent(r -> {
                if (r == ButtonType.OK) finishQuiz();
            });
        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        quizFinished = true;
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (selectedAnswers[i] != null &&
                    selectedAnswers[i].trim().equalsIgnoreCase(questions.get(i).getAnswer().trim())) {
                score++;
            }
        }

        ResultRepository.save(student.getUsername(), subject, score, questions.size());
        showResultScreen(score);
    }

    private void showResultScreen(int score) {
        int total = questions.size();
        double pct = total > 0 ? (score * 100.0 / total) : 0;
        String grade = pct >= 80 ? "A+" : pct >= 75 ? "A" : pct >= 70 ? "A-" :
                pct >= 65 ? "B+" : pct >= 60 ? "B" : pct >= 55 ? "B-" :
                pct >= 50 ? "C+" : pct >= 45 ? "C" : pct >= 40 ? "D" : "F";
        String remark = pct >= 80 ? "Outstanding!" : pct >= 70 ? "Excellent!" : pct >= 60 ? "Good work!" :
                pct >= 50 ? "Satisfactory" : pct >= 40 ? "Passed" : "Keep practicing";

        VBox resultBox = new VBox(24);
        resultBox.setAlignment(Pos.CENTER);
        resultBox.setMaxWidth(560);

        // Score card
        VBox scoreCard = new VBox(16);
        scoreCard.getStyleClass().add("card");
        scoreCard.setAlignment(Pos.CENTER);
        scoreCard.setPadding(new Insets(36, 48, 36, 48));

        Label emoji = new Label(pct >= 80 ? "🎉" : pct >= 60 ? "👍" : "📚");
        emoji.setStyle("-fx-font-size: 48px;");

        Label title = new Label("Quiz Complete!");
        title.getStyleClass().add("heading-lg");

        Label subjectLbl = new Label(subject);
        subjectLbl.getStyleClass().add("text-secondary");

        Label scoreLabel = new Label(score + " / " + total);
        scoreLabel.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: " +
                (pct >= 60 ? "#22c55e" : pct >= 40 ? "#f59e0b" : "#ef4444") + ";");

        Label pctLabel = new Label(String.format("%.0f%%", pct));
        pctLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #8b949e;");

        HBox gradeRow = new HBox(12);
        gradeRow.setAlignment(Pos.CENTER);
        Label gradeBadge = new Label("Grade: " + grade);
        gradeBadge.getStyleClass().add("badge");
        gradeBadge.setStyle(gradeBadge.getStyle() + "-fx-font-size: 14px; -fx-padding: 6px 16px;");
        Label remarkLbl = new Label(remark);
        remarkLbl.getStyleClass().add("badge-success");
        remarkLbl.setStyle(remarkLbl.getStyle() + "-fx-font-size: 14px; -fx-padding: 6px 16px;");
        gradeRow.getChildren().addAll(gradeBadge, remarkLbl);

        scoreCard.getChildren().addAll(emoji, title, subjectLbl, scoreLabel, pctLabel, gradeRow);

        // Review section
        VBox reviewCard = new VBox(12);
        reviewCard.getStyleClass().add("card");
        Label reviewTitle = new Label("Answer Review");
        reviewTitle.getStyleClass().add("heading-md");
        reviewCard.getChildren().add(reviewTitle);

        ScrollPane reviewScroll = new ScrollPane();
        reviewScroll.setFitToWidth(true);
        reviewScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        reviewScroll.setPrefHeight(220);

        VBox reviewList = new VBox(10);
        reviewList.setStyle("-fx-background-color: transparent;");
        for (int i = 0; i < questions.size(); i++) {
            QuestionModel q = questions.get(i);
            boolean correct = selectedAnswers[i] != null &&
                    selectedAnswers[i].trim().equalsIgnoreCase(q.getAnswer().trim());

            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(8, 12, 8, 12));
            row.setStyle("-fx-background-color: " + (correct ? "rgba(34,197,94,0.08)" : "rgba(239,68,68,0.08)") +
                    "; -fx-background-radius: 6px;");

            Label num = new Label((i + 1) + ".");
            num.setStyle("-fx-text-fill: #8b949e; -fx-min-width: 24px;");
            Label icon = new Label(correct ? "✓" : "✗");
            icon.setStyle("-fx-text-fill: " + (correct ? "#22c55e" : "#ef4444") + "; -fx-font-weight: bold; -fx-min-width: 20px;");
            Label qText = new Label(q.getQuestionText().length() > 60 ? q.getQuestionText().substring(0, 60) + "..." : q.getQuestionText());
            qText.setStyle("-fx-text-fill: #e6edf3; -fx-font-size: 12px;");
            qText.setMaxWidth(300);
            HBox spacer2 = new HBox(); HBox.setHgrow(spacer2, Priority.ALWAYS);
            Label correctAns = new Label("→ " + q.getAnswer().trim());
            correctAns.setStyle("-fx-text-fill: #22c55e; -fx-font-size: 12px;");

            row.getChildren().addAll(num, icon, qText, spacer2, correctAns);
            reviewList.getChildren().add(row);
        }
        reviewScroll.setContent(reviewList);
        reviewCard.getChildren().add(reviewScroll);

        // Buttons
        HBox btnRow = new HBox(16);
        btnRow.setAlignment(Pos.CENTER);
        Button dashBtn = new Button("Back to Dashboard");
        dashBtn.getStyleClass().add("btn-primary");
        dashBtn.setOnAction(e -> SceneManager.showStudentDashboard(student));
        btnRow.getChildren().add(dashBtn);

        resultBox.getChildren().addAll(scoreCard, reviewCard, btnRow);

        ScrollPane resultScroll = new ScrollPane(resultBox);
        resultScroll.setFitToWidth(true);
        resultScroll.setStyle("-fx-background-color: #0f1117; -fx-background: #0f1117;");
        resultScroll.setPadding(new Insets(32));

        centerPane.getChildren().setAll(resultScroll);
    }

    private void styleDialog(DialogPane pane) {
        pane.setStyle("-fx-background-color: #1c2128;");
        pane.lookupAll(".label").forEach(n -> n.setStyle("-fx-text-fill: #e6edf3;"));
        pane.lookupAll(".button").forEach(n -> n.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-background-radius: 6px;"));
    }

    private void openCalculator() {
        Stage calcStage = new Stage();
        calcStage.setTitle("Calculator - Use during exam, then close");
        CalculatorView calcView = new CalculatorView();
        Scene calcScene = new Scene(calcView.getRoot(), 400, 600);
        
        // Load CSS
        java.net.URL css = getClass().getResource("/css/style.css");
        if (css != null) {
            calcScene.getStylesheets().add(css.toExternalForm());
        } else {
            java.io.File f = new java.io.File("css/style.css");
            if (f.exists()) calcScene.getStylesheets().add(f.toURI().toString());
        }
        
        calcStage.setScene(calcScene);
        calcStage.show();
    }

    public BorderPane getRoot() { return root; }
}
