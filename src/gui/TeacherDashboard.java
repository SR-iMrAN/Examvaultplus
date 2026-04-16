package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.ResultModel;
import models.TeacherModel;
import repositories.QuestionRepository;
import repositories.ResultRepository;
import repositories.SubjectRepository;

import java.util.List;

public class TeacherDashboard {

    private BorderPane root;
    private TeacherModel teacher;
    private String activeTab = "overview";
    private BorderPane contentArea;

    public TeacherDashboard(TeacherModel teacher) {
        this.teacher = teacher;
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0f1117;");
        root.setTop(buildNavBar());
        contentArea = new BorderPane();
        root.setCenter(contentArea);
        showOverviewTab();
    }

    private HBox buildNavBar() {
        HBox nav = new HBox(0);
        nav.getStyleClass().add("nav-bar");
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setPrefHeight(50);

        HBox brand = new HBox(4);
        brand.setAlignment(Pos.CENTER);
        brand.setPadding(new Insets(0, 24, 0, 4));
        Label vault = new Label("ExamVault");
        vault.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #e6edf3;");
        Label plus = new Label("Plus");
        plus.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #3b82f6;");
        brand.getChildren().addAll(vault, plus);

        Button overviewBtn = navButton("Overview", "overview");
        Button subjectsBtn = navButton("Subjects", "subjects");
        Button questionsBtn = navButton("Questions", "questions");
        Button resultsBtn = navButton("Results", "results");

        HBox spacer = new HBox(); HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox userInfo = new HBox(12);
        userInfo.setAlignment(Pos.CENTER);
        String teacherName = (teacher.getName() != null && !teacher.getName().isEmpty()) ? teacher.getName() : teacher.getUsername();
        Label userLabel = new Label(teacherName);
        userLabel.setStyle("-fx-text-fill: #8b949e; -fx-font-size: 13px;");
        Label roleBadge = new Label("Teacher");
        roleBadge.setStyle("-fx-background-color: rgba(245,158,11,0.15); -fx-text-fill: #f59e0b; " +
                "-fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 3px 8px; -fx-background-radius: 20px;");
        Button logoutBtn = new Button("Sign Out");
        logoutBtn.getStyleClass().add("btn-ghost");
        logoutBtn.setOnAction(e -> SceneManager.showLoginView());
        userInfo.getChildren().addAll(userLabel, roleBadge, logoutBtn);

        nav.getChildren().addAll(brand, overviewBtn, subjectsBtn, questionsBtn, resultsBtn, spacer, userInfo);
        HBox.setMargin(userInfo, new Insets(0, 8, 0, 0));

        updateNavStyles(overviewBtn, subjectsBtn, questionsBtn, resultsBtn);

        overviewBtn.setOnAction(e -> { activeTab = "overview"; updateNavStyles(overviewBtn, subjectsBtn, questionsBtn, resultsBtn); showOverviewTab(); });
        subjectsBtn.setOnAction(e -> { activeTab = "subjects"; updateNavStyles(overviewBtn, subjectsBtn, questionsBtn, resultsBtn); showSubjectsTab(); });
        questionsBtn.setOnAction(e -> { activeTab = "questions"; updateNavStyles(overviewBtn, subjectsBtn, questionsBtn, resultsBtn); showQuestionsTab(); });
        resultsBtn.setOnAction(e -> { activeTab = "results"; updateNavStyles(overviewBtn, subjectsBtn, questionsBtn, resultsBtn); showResultsTab(); });

        return nav;
    }

    private Button navButton(String text, String id) {
        Button btn = new Button(text);
        btn.setId("nav_" + id);
        btn.getStyleClass().add("nav-item");
        return btn;
    }

    private void updateNavStyles(Button... buttons) {
        for (Button btn : buttons) {
            String id = btn.getId().replace("nav_", "");
            btn.getStyleClass().setAll(id.equals(activeTab) ? "nav-item-active" : "nav-item");
        }
    }

    // ===== OVERVIEW =====
    private void showOverviewTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #0f1117; -fx-background: #0f1117;");

        VBox content = new VBox(28);
        content.setPadding(new Insets(28, 32, 28, 32));
        content.setStyle("-fx-background-color: #0f1117;");

        String teacherName = (teacher.getName() != null && !teacher.getName().isEmpty()) ? teacher.getName() : teacher.getUsername();
        Label heading = new Label("Welcome, " + teacherName);
        heading.getStyleClass().add("heading-xl");
        Label sub = new Label("Manage your subjects, questions, and view student results.");
        sub.getStyleClass().add("text-secondary");
        VBox header = new VBox(6, heading, sub);

        List<String> subjects = SubjectRepository.getAll();
        List<ResultModel> allResults = ResultRepository.getAll();

        HBox statsRow = new HBox(16);
        statsRow.getChildren().addAll(
            statCard("Subjects", String.valueOf(subjects.size()), "#3b82f6"),
            statCard("Total Quizzes Taken", String.valueOf(allResults.size()), "#22c55e"),
            statCard("Avg Score", allResults.isEmpty() ? "–" :
                String.format("%.0f%%", allResults.stream().mapToDouble(ResultModel::getPercentage).average().orElse(0)), "#f59e0b")
        );

        // Recent results snippet
        VBox recentCard = new VBox(12);
        recentCard.getStyleClass().add("card");
        Label recentTitle = new Label("Recent Activity");
        recentTitle.getStyleClass().add("heading-md");
        recentCard.getChildren().add(recentTitle);

        if (allResults.isEmpty()) {
            Label empty = new Label("No quiz results yet.");
            empty.getStyleClass().add("text-secondary");
            recentCard.getChildren().add(empty);
        } else {
            int show = Math.min(5, allResults.size());
            for (int i = allResults.size() - show; i < allResults.size(); i++) {
                ResultModel r = allResults.get(i);
                HBox row = new HBox(12);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(8, 10, 8, 10));
                row.setStyle("-fx-background-color: #161b22; -fx-background-radius: 6px;");
                Label studentLbl = new Label(r.getStudentId());
                studentLbl.setStyle("-fx-text-fill: #e6edf3; -fx-font-size: 13px; -fx-min-width: 140px;");
                Label subjLbl = new Label(r.getSubject());
                subjLbl.getStyleClass().add("badge");
                HBox sp = new HBox(); HBox.setHgrow(sp, Priority.ALWAYS);
                Label scoreLbl = new Label(r.getScore() + "/" + r.getTotal() + "  (" + String.format("%.0f%%", r.getPercentage()) + ")");
                scoreLbl.setStyle("-fx-text-fill: " + (r.getPercentage() >= 60 ? "#22c55e" : "#ef4444") + "; -fx-font-size: 13px;");
                row.getChildren().addAll(studentLbl, subjLbl, sp, scoreLbl);
                recentCard.getChildren().add(row);
            }
        }

        content.getChildren().addAll(header, statsRow, recentCard);
        scroll.setContent(content);
        contentArea.setCenter(scroll);
    }

    // ===== SUBJECTS =====
    private void showSubjectsTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #0f1117; -fx-background: #0f1117;");

        VBox content = new VBox(24);
        content.setPadding(new Insets(28, 32, 28, 32));
        content.setStyle("-fx-background-color: #0f1117;");

        Label heading = new Label("Manage Subjects");
        heading.getStyleClass().add("heading-lg");
        Label sub = new Label("Add and view subjects for quiz management");
        sub.getStyleClass().add("text-secondary");

        // Add subject card
        VBox addCard = new VBox(14);
        addCard.getStyleClass().add("card");
        addCard.setMaxWidth(480);
        Label addTitle = new Label("Add New Subject");
        addTitle.getStyleClass().add("heading-md");

        HBox addRow = new HBox(10);
        addRow.setAlignment(Pos.CENTER_LEFT);
        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject name (e.g. Mathematics)");
        subjectField.setPrefWidth(260);
        Button addBtn = new Button("Add Subject");
        addBtn.getStyleClass().add("btn-primary");

        Label addMsg = new Label("");
        addMsg.setVisible(false);

        addBtn.setOnAction(e -> {
            String name = subjectField.getText().trim();
            if (name.isEmpty()) { addMsg.setText("Subject name cannot be empty."); addMsg.getStyleClass().setAll("text-danger"); addMsg.setVisible(true); return; }
            boolean added = SubjectRepository.addSubject(name);
            if (added) {
                addMsg.setText("✓ Subject \"" + name + "\" added successfully.");
                addMsg.getStyleClass().setAll("text-success");
                subjectField.clear();
                showSubjectsTab(); // refresh
            } else {
                addMsg.setText("Subject already exists.");
                addMsg.getStyleClass().setAll("text-danger");
            }
            addMsg.setVisible(true);
        });

        addRow.getChildren().addAll(subjectField, addBtn);
        addCard.getChildren().addAll(addTitle, addRow, addMsg);

        // Subject list
        List<String> subjects = SubjectRepository.getAll();

        VBox listCard = new VBox(10);
        listCard.getStyleClass().add("card");
        Label listTitle = new Label("All Subjects  ·  " + subjects.size() + " total");
        listTitle.getStyleClass().add("heading-md");
        listCard.getChildren().add(listTitle);

        if (subjects.isEmpty()) {
            Label empty = new Label("No subjects added yet. Add one above.");
            empty.getStyleClass().add("text-secondary");
            listCard.getChildren().add(empty);
        } else {
            for (int i = 0; i < subjects.size(); i++) {
                String sname = subjects.get(i);
                HBox row = new HBox(12);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(10, 14, 10, 14));
                row.setStyle("-fx-background-color: #161b22; -fx-background-radius: 6px;");
                Label numLbl = new Label((i + 1) + ".");
                numLbl.setStyle("-fx-text-fill: #484f58; -fx-min-width: 24px;");
                Label nameLbl = new Label(sname);
                nameLbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #e6edf3;");
                HBox sp = new HBox(); HBox.setHgrow(sp, Priority.ALWAYS);
                // Question count
                int qCount = QuestionRepository.loadForSubject(sname).size();
                Label qBadge = new Label(qCount + " questions");
                qBadge.getStyleClass().add("badge");
                row.getChildren().addAll(numLbl, nameLbl, sp, qBadge);
                listCard.getChildren().add(row);
            }
        }

        content.getChildren().addAll(new VBox(4, heading, sub), addCard, listCard);
        scroll.setContent(content);
        contentArea.setCenter(scroll);
    }

    // ===== QUESTIONS =====
    private void showQuestionsTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #0f1117; -fx-background: #0f1117;");

        VBox content = new VBox(24);
        content.setPadding(new Insets(28, 32, 28, 32));
        content.setStyle("-fx-background-color: #0f1117;");

        Label heading = new Label("Manage Questions");
        heading.getStyleClass().add("heading-lg");
        Label sub = new Label("Add MCQ questions to subjects");
        sub.getStyleClass().add("text-secondary");

        List<String> subjects = SubjectRepository.getAll();

        if (subjects.isEmpty()) {
            VBox empty = new VBox(12);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(40));
            Label emptyIcon = new Label("📝");
            emptyIcon.setStyle("-fx-font-size: 40px;");
            Label emptyText = new Label("Add subjects first before adding questions.");
            emptyText.getStyleClass().add("text-secondary");
            Button goSubjects = new Button("Go to Subjects");
            goSubjects.getStyleClass().add("btn-primary");
            goSubjects.setOnAction(e -> { activeTab = "subjects"; showSubjectsTab(); });
            empty.getChildren().addAll(emptyIcon, emptyText, goSubjects);
            content.getChildren().addAll(new VBox(4, heading, sub), empty);
        } else {
            // Subject selector
            VBox formCard = new VBox(16);
            formCard.getStyleClass().add("card");
            formCard.setMaxWidth(600);

            Label formTitle = new Label("Add MCQ Question");
            formTitle.getStyleClass().add("heading-md");

            Label subjectLbl = new Label("Subject");
            subjectLbl.getStyleClass().add("text-secondary");
            ComboBox<String> subjectCombo = new ComboBox<>();
            subjectCombo.getItems().addAll(subjects);
            subjectCombo.setValue(subjects.get(0));
            subjectCombo.setMaxWidth(Double.MAX_VALUE);

            Label qLbl = new Label("Question Text");
            qLbl.getStyleClass().add("text-secondary");
            TextArea qArea = new TextArea();
            qArea.setPromptText("Enter the question here...");
            qArea.setPrefRowCount(3);
            qArea.setWrapText(true);
            qArea.setStyle("-fx-control-inner-background: #161b22; -fx-text-fill: #e6edf3; -fx-prompt-text-fill: #484f58; -fx-background-color: #161b22; -fx-background-insets: 0;");

            Label optionsLbl = new Label("Options (4 choices)");
            optionsLbl.getStyleClass().add("text-secondary");

            GridPane optGrid = new GridPane();
            optGrid.setHgap(10); optGrid.setVgap(8);
            TextField[] optFields = new TextField[4];
            for (int i = 0; i < 4; i++) {
                Label optLbl = new Label((char)('A' + i) + ".");
                optLbl.setStyle("-fx-text-fill: #8b949e; -fx-min-width: 20px;");
                optFields[i] = new TextField();
                optFields[i].setPromptText("Option " + (char)('A' + i));
                optFields[i].setStyle("-fx-control-inner-background: #161b22; -fx-text-fill: #e6edf3; -fx-prompt-text-fill: #484f58; -fx-background-color: #161b22; -fx-background-insets: 0;");
                optGrid.add(optLbl, 0, i);
                optGrid.add(optFields[i], 1, i);
                ColumnConstraints cc = new ColumnConstraints();
                if (i == 0) { cc.setMinWidth(26); optGrid.getColumnConstraints().add(cc); }
                if (i == 0) { ColumnConstraints cc2 = new ColumnConstraints(); cc2.setHgrow(Priority.ALWAYS); optGrid.getColumnConstraints().add(cc2); }
            }

            Label ansLbl = new Label("Correct Answer (exact text of correct option)");
            ansLbl.getStyleClass().add("text-secondary");
            TextField ansField = new TextField();
            ansField.setPromptText("Type the exact text of the correct option");
            ansField.setStyle("-fx-control-inner-background: #161b22; -fx-text-fill: #e6edf3; -fx-prompt-text-fill: #484f58; -fx-background-color: #161b22; -fx-background-insets: 0;");

            Label msgLbl = new Label(""); msgLbl.setVisible(false);

            Button addBtn = new Button("Add Question");
            addBtn.getStyleClass().add("btn-primary");
            addBtn.setPadding(new Insets(10, 24, 10, 24));

            addBtn.setOnAction(e -> {
                String subject = subjectCombo.getValue();
                String qText = qArea.getText().trim();
                String[] opts = new String[4];
                for (int i = 0; i < 4; i++) opts[i] = optFields[i].getText().trim();
                String ans = ansField.getText().trim();
                if (qText.isEmpty() || ans.isEmpty()) { showMsg(msgLbl, "Question and answer are required.", false); return; }
                for (int i = 0; i < 4; i++) { if (opts[i].isEmpty()) { showMsg(msgLbl, "All 4 options are required.", false); return; } }
                boolean found = false;
                for (String opt : opts) if (opt.equalsIgnoreCase(ans)) { found = true; break; }
                if (!found) { showMsg(msgLbl, "Correct answer must match one of the options exactly.", false); return; }
                boolean ok = QuestionRepository.addQuestion(subject, qText, opts, ans);
                if (ok) {
                    showMsg(msgLbl, "✓ Question added to \"" + subject + "\" successfully!", true);
                    qArea.clear(); ansField.clear();
                    for (TextField f : optFields) f.clear();
                } else {
                    showMsg(msgLbl, "Error saving question.", false);
                }
            });

            // Import section
            VBox importCard = new VBox(12);
            importCard.getStyleClass().add("card");
            importCard.setMaxWidth(600);
            Label importTitle = new Label("Import Questions from File");
            importTitle.getStyleClass().add("heading-md");
            Label importHint = new Label("File format: one question per line — Question;Option1,Option2,Option3,Option4;CorrectAnswer");
            importHint.getStyleClass().add("text-muted");
            importHint.setWrapText(true);

            Label impSubLbl = new Label("Subject");
            impSubLbl.getStyleClass().add("text-secondary");
            ComboBox<String> impSubCombo = new ComboBox<>();
            impSubCombo.getItems().addAll(subjects);
            impSubCombo.setValue(subjects.get(0));
            impSubCombo.setMaxWidth(Double.MAX_VALUE);

            Label pathLbl = new Label("File Path");
            pathLbl.getStyleClass().add("text-secondary");
            TextField pathField = new TextField();
            pathField.setPromptText("Full path to .txt file e.g. C:\\questions.txt");
            Label impMsgLbl = new Label(""); impMsgLbl.setVisible(false);

            Button importBtn = new Button("Import Questions");
            importBtn.getStyleClass().add("btn-secondary");
            importBtn.setOnAction(ev -> {
                String path = pathField.getText().trim();
                String subject = impSubCombo.getValue();
                if (path.isEmpty()) { showMsg(impMsgLbl, "Please enter a file path.", false); return; }
                boolean ok = QuestionRepository.importFromFile(subject, path);
                if (ok) { showMsg(impMsgLbl, "✓ Questions imported to \"" + subject + "\" successfully!", true); pathField.clear(); }
                else { showMsg(impMsgLbl, "Import failed. Check file path and format.", false); }
            });

            importCard.getChildren().addAll(importTitle, importHint, impSubLbl, impSubCombo, pathLbl, pathField, impMsgLbl, importBtn);

            formCard.getChildren().addAll(formTitle, subjectLbl, subjectCombo, qLbl, qArea,
                    optionsLbl, optGrid, ansLbl, ansField, msgLbl, addBtn);

            content.getChildren().addAll(new VBox(4, heading, sub), formCard, importCard);
        }

        scroll.setContent(content);
        contentArea.setCenter(scroll);
    }

    // ===== RESULTS =====
    private void showResultsTab() {
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #0f1117; -fx-background: #0f1117;");

        VBox content = new VBox(24);
        content.setPadding(new Insets(28, 32, 28, 32));
        content.setStyle("-fx-background-color: #0f1117;");

        Label heading = new Label("Quiz Results");
        heading.getStyleClass().add("heading-lg");
        Label sub = new Label("View and filter student quiz results");
        sub.getStyleClass().add("text-secondary");

        List<String> subjects = SubjectRepository.getAll();

        // Filter row
        HBox filterRow = new HBox(12);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        Label filterLbl = new Label("Filter by subject:");
        filterLbl.getStyleClass().add("text-secondary");
        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().add("All Subjects");
        filterCombo.getItems().addAll(subjects);
        filterCombo.setValue("All Subjects");

        filterRow.getChildren().addAll(filterLbl, filterCombo);

        // Table
        TableView<ResultModel> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setPrefHeight(400);

        TableColumn<ResultModel, String> studentCol = new TableColumn<>("Student ID");
        studentCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getStudentId()));
        studentCol.setPrefWidth(180);

        TableColumn<ResultModel, String> subjCol = new TableColumn<>("Subject");
        subjCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getSubject()));
        subjCol.setPrefWidth(200);

        TableColumn<ResultModel, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getScore() + " / " + d.getValue().getTotal()));
        scoreCol.setPrefWidth(120);

        TableColumn<ResultModel, String> pctCol = new TableColumn<>("Percentage");
        pctCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                String.format("%.0f%%", d.getValue().getPercentage())));
        pctCol.setPrefWidth(120);

        TableColumn<ResultModel, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(d -> {
            double pct = d.getValue().getPercentage();
            String grade = pct >= 80 ? "A+" : pct >= 75 ? "A" : pct >= 70 ? "A-" :
                    pct >= 65 ? "B+" : pct >= 60 ? "B" : pct >= 55 ? "B-" :
                    pct >= 50 ? "C+" : pct >= 45 ? "C" : pct >= 40 ? "D" : "F";
            return new javafx.beans.property.SimpleStringProperty(grade);
        });
        gradeCol.setPrefWidth(100);

        table.getColumns().addAll(studentCol, subjCol, scoreCol, pctCol, gradeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Runnable refreshTable = () -> {
            table.getItems().clear();
            String filter = filterCombo.getValue();
            List<ResultModel> results = "All Subjects".equals(filter)
                    ? ResultRepository.getAll()
                    : ResultRepository.getBySubject(filter);
            table.getItems().addAll(results);
        };
        refreshTable.run();
        filterCombo.setOnAction(e -> refreshTable.run());

        // Summary below table
        Label totalLbl = new Label();
        table.getItems().addListener((javafx.collections.ListChangeListener<ResultModel>) c -> {
            totalLbl.setText(table.getItems().size() + " result(s) shown");
        });
        totalLbl.setText(table.getItems().size() + " result(s) shown");
        totalLbl.getStyleClass().add("text-muted");

        content.getChildren().addAll(new VBox(4, heading, sub), filterRow, table, totalLbl);
        scroll.setContent(content);
        contentArea.setCenter(scroll);
    }

    private VBox statCard(String label, String value, String color) {
        VBox card = new VBox(4);
        card.getStyleClass().add("stat-card");
        card.setPrefWidth(180);
        Label val = new Label(value);
        val.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label lbl = new Label(label);
        lbl.getStyleClass().add("stat-label");
        card.getChildren().addAll(val, lbl);
        return card;
    }

    private void showMsg(Label lbl, String text, boolean success) {
        lbl.setText(text);
        lbl.getStyleClass().setAll(success ? "text-success" : "text-danger");
        lbl.setVisible(true);
    }

    public BorderPane getRoot() { return root; }
}
