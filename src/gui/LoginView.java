package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.StudentModel;
import models.TeacherModel;
import services.AuthService;

public class LoginView {

    private BorderPane root;

    public LoginView() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0f1117;");
        root.setCenter(buildCenter());
    }

    private StackPane buildCenter() {
        StackPane container = new StackPane();
        container.setStyle("-fx-background-color: #0f1117;");

        VBox card = new VBox(0);
        card.getStyleClass().add("login-card");
        card.setMaxWidth(420);
        card.setMinWidth(380);

        // Logo
        HBox logoBox = new HBox(4);
        logoBox.setAlignment(Pos.CENTER);
        Label vault = new Label("ExamVault");
        vault.getStyleClass().add("app-logo");
        Label plus = new Label("Plus");
        plus.getStyleClass().add("app-logo-accent");
        logoBox.getChildren().addAll(vault, plus);

        Label subtitle = new Label("Academic Management System");
        subtitle.getStyleClass().add("text-secondary");
        subtitle.setPadding(new Insets(4, 0, 24, 0));

        // Tab bar for Student/Teacher
        HBox tabBar = new HBox(0);
        tabBar.setStyle("-fx-background-color: #161b22; -fx-background-radius: 6px; -fx-padding: 4px;");
        tabBar.setAlignment(Pos.CENTER);

        ToggleButton studentTab = new ToggleButton("Student");
        ToggleButton teacherTab = new ToggleButton("Teacher");

        String activeTabStyle = "-fx-background-color: #1c2128; -fx-text-fill: #3b82f6; -fx-font-weight: bold; " +
                "-fx-font-size: 13px; -fx-padding: 7px 28px; -fx-background-radius: 4px; -fx-cursor: hand; " +
                "-fx-border-color: transparent;";
        String inactiveTabStyle = "-fx-background-color: transparent; -fx-text-fill: #8b949e; -fx-font-size: 13px; " +
                "-fx-padding: 7px 28px; -fx-background-radius: 4px; -fx-cursor: hand; -fx-border-color: transparent;";

        studentTab.setStyle(activeTabStyle);
        teacherTab.setStyle(inactiveTabStyle);

        ToggleGroup tabGroup = new ToggleGroup();
        studentTab.setToggleGroup(tabGroup);
        teacherTab.setToggleGroup(tabGroup);
        studentTab.setSelected(true);

        tabBar.getChildren().addAll(studentTab, teacherTab);
        tabBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(studentTab, Priority.ALWAYS);
        HBox.setHgrow(teacherTab, Priority.ALWAYS);
        studentTab.setMaxWidth(Double.MAX_VALUE);
        teacherTab.setMaxWidth(Double.MAX_VALUE);

        // Form area (switches between login and register)
        StackPane formArea = new StackPane();
        VBox studentLoginForm = buildStudentLoginForm();
        VBox teacherLoginForm = buildTeacherLoginForm();
        teacherLoginForm.setVisible(false);
        teacherLoginForm.setManaged(false);
        formArea.getChildren().addAll(studentLoginForm, teacherLoginForm);

        studentTab.setOnAction(e -> {
            studentTab.setStyle(activeTabStyle);
            teacherTab.setStyle(inactiveTabStyle);
            studentLoginForm.setVisible(true);
            studentLoginForm.setManaged(true);
            teacherLoginForm.setVisible(false);
            teacherLoginForm.setManaged(false);
        });

        teacherTab.setOnAction(e -> {
            teacherTab.setStyle(activeTabStyle);
            studentTab.setStyle(inactiveTabStyle);
            studentLoginForm.setVisible(false);
            studentLoginForm.setManaged(false);
            teacherLoginForm.setVisible(true);
            teacherLoginForm.setManaged(true);
        });

        card.getChildren().addAll(logoBox, subtitle, tabBar, formArea);
        container.getChildren().add(card);
        StackPane.setAlignment(card, Pos.CENTER);

        return container;
    }

    private VBox buildStudentLoginForm() {
        VBox form = new VBox(0);
        form.setPadding(new Insets(24, 0, 0, 0));

        // Login section
        VBox loginSection = new VBox(12);
        Label loginTitle = new Label("Sign In");
        loginTitle.getStyleClass().add("heading-md");
        loginTitle.setPadding(new Insets(0, 0, 4, 0));

        Label idLabel = new Label("Student ID");
        idLabel.getStyleClass().add("text-secondary");
        TextField idField = new TextField();
        idField.setPromptText("Enter your student ID");

        Label passLabel = new Label("Password");
        passLabel.getStyleClass().add("text-secondary");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Your password (default: Student ID)");

        Label errorLabel = new Label("");
        errorLabel.getStyleClass().add("text-danger");
        errorLabel.setVisible(false);

        Button loginBtn = new Button("Sign In");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPadding(new Insets(10, 0, 10, 0));

        loginSection.getChildren().addAll(loginTitle, idLabel, idField, passLabel, passField, errorLabel, loginBtn);

        // Separator + toggle
        Separator sep = new Separator();
        sep.setPadding(new Insets(16, 0, 16, 0));

        // Register section
        VBox registerSection = new VBox(10);
        registerSection.setVisible(false);
        registerSection.setManaged(false);

        Label regTitle = new Label("Create Account");
        regTitle.getStyleClass().add("heading-md");
        regTitle.setPadding(new Insets(0, 0, 4, 0));

        Label nameLabel = new Label("Full Name");
        nameLabel.getStyleClass().add("text-secondary");
        TextField nameField = new TextField();
        nameField.setPromptText("Your full name");

        Label regIdLabel = new Label("Student ID");
        regIdLabel.getStyleClass().add("text-secondary");
        TextField regIdField = new TextField();
        regIdField.setPromptText("Your student ID (becomes password)");

        Label contactLabel = new Label("Contact Number");
        contactLabel.getStyleClass().add("text-secondary");
        TextField contactField = new TextField();
        contactField.setPromptText("Phone number");

        Label regErrorLabel = new Label("");
        regErrorLabel.getStyleClass().add("text-danger");
        regErrorLabel.setVisible(false);

        Label regSuccessLabel = new Label("");
        regSuccessLabel.getStyleClass().add("text-success");
        regSuccessLabel.setVisible(false);

        Button registerBtn = new Button("Create Account");
        registerBtn.getStyleClass().add("btn-success");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setPadding(new Insets(10, 0, 10, 0));

        registerSection.getChildren().addAll(regTitle, nameLabel, nameField, regIdLabel, regIdField,
                contactLabel, contactField, regErrorLabel, regSuccessLabel, registerBtn);

        // Toggle link
        HBox toggleBox = new HBox();
        toggleBox.setAlignment(Pos.CENTER);
        toggleBox.setPadding(new Insets(8, 0, 0, 0));
        Label toggleLink = new Label("Don't have an account? Register");
        toggleLink.getStyleClass().add("text-accent");
        toggleLink.setStyle("-fx-cursor: hand; -fx-underline: true;");
        toggleBox.getChildren().add(toggleLink);

        final boolean[] showRegister = {false};
        toggleLink.setOnMouseClicked(e -> {
            showRegister[0] = !showRegister[0];
            loginSection.setVisible(!showRegister[0]);
            loginSection.setManaged(!showRegister[0]);
            registerSection.setVisible(showRegister[0]);
            registerSection.setManaged(showRegister[0]);
            toggleLink.setText(showRegister[0] ? "Already have an account? Sign In" : "Don't have an account? Register");
        });

        loginBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String pass = passField.getText().trim();
            if (id.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                errorLabel.setVisible(true);
                return;
            }
            StudentModel student = AuthService.loginStudent(id, pass);
            if (student == null) {
                errorLabel.setText("Invalid ID or password.");
                errorLabel.setVisible(true);
            } else {
                SceneManager.showStudentDashboard(student);
            }
        });

        registerBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String id = regIdField.getText().trim();
            String contact = contactField.getText().trim();
            if (name.isEmpty() || id.isEmpty() || contact.isEmpty()) {
                regErrorLabel.setText("All fields are required.");
                regErrorLabel.setVisible(true);
                regSuccessLabel.setVisible(false);
                return;
            }
            boolean ok = AuthService.registerStudent(name, id, contact);
            if (ok) {
                regSuccessLabel.setText("Registered! Sign in with your ID.");
                regSuccessLabel.setVisible(true);
                regErrorLabel.setVisible(false);
                nameField.clear(); regIdField.clear(); contactField.clear();
            } else {
                regErrorLabel.setText("ID already exists. Choose another.");
                regErrorLabel.setVisible(true);
                regSuccessLabel.setVisible(false);
            }
        });

        form.getChildren().addAll(loginSection, sep, registerSection, toggleBox);
        return form;
    }

    private VBox buildTeacherLoginForm() {
        VBox form = new VBox(0);
        form.setPadding(new Insets(24, 0, 0, 0));

        VBox loginSection = new VBox(12);
        Label loginTitle = new Label("Teacher Sign In");
        loginTitle.getStyleClass().add("heading-md");
        loginTitle.setPadding(new Insets(0, 0, 4, 0));

        Label usernameLabel = new Label("Contact / Username");
        usernameLabel.getStyleClass().add("text-secondary");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Your registered contact number");

        Label passLabel = new Label("Password");
        passLabel.getStyleClass().add("text-secondary");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Your password");

        Label errorLabel = new Label("");
        errorLabel.getStyleClass().add("text-danger");
        errorLabel.setVisible(false);

        Button loginBtn = new Button("Sign In");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPadding(new Insets(10, 0, 10, 0));

        loginSection.getChildren().addAll(loginTitle, usernameLabel, usernameField, passLabel, passField, errorLabel, loginBtn);

        Separator sep = new Separator();
        sep.setPadding(new Insets(16, 0, 16, 0));

        // Register section
        VBox registerSection = new VBox(10);
        registerSection.setVisible(false);
        registerSection.setManaged(false);

        Label regTitle = new Label("Register as Teacher");
        regTitle.getStyleClass().add("heading-md");
        regTitle.setPadding(new Insets(0, 0, 4, 0));

        Label nameLabel = new Label("Full Name");
        nameLabel.getStyleClass().add("text-secondary");
        TextField nameField = new TextField();
        nameField.setPromptText("Your full name");

        Label idLabel = new Label("Teacher ID");
        idLabel.getStyleClass().add("text-secondary");
        TextField idField = new TextField();
        idField.setPromptText("Teacher ID");

        Label contactLabel = new Label("Contact (Username for login)");
        contactLabel.getStyleClass().add("text-secondary");
        TextField contactField = new TextField();
        contactField.setPromptText("Phone / contact number");

        Label regPassLabel = new Label("Password");
        regPassLabel.getStyleClass().add("text-secondary");
        PasswordField regPassField = new PasswordField();
        regPassField.setPromptText("Create a password");

        Label confirmPassLabel = new Label("Confirm Password");
        confirmPassLabel.getStyleClass().add("text-secondary");
        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Repeat your password");

        Label regErrorLabel = new Label("");
        regErrorLabel.getStyleClass().add("text-danger");
        regErrorLabel.setVisible(false);

        Label regSuccessLabel = new Label("");
        regSuccessLabel.getStyleClass().add("text-success");
        regSuccessLabel.setVisible(false);

        Button registerBtn = new Button("Register");
        registerBtn.getStyleClass().add("btn-success");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setPadding(new Insets(10, 0, 10, 0));

        registerSection.getChildren().addAll(regTitle, nameLabel, nameField, idLabel, idField,
                contactLabel, contactField, regPassLabel, regPassField, confirmPassLabel, confirmPassField,
                regErrorLabel, regSuccessLabel, registerBtn);

        HBox toggleBox = new HBox();
        toggleBox.setAlignment(Pos.CENTER);
        toggleBox.setPadding(new Insets(8, 0, 0, 0));
        Label toggleLink = new Label("No account? Register here");
        toggleLink.getStyleClass().add("text-accent");
        toggleLink.setStyle("-fx-cursor: hand; -fx-underline: true;");
        toggleBox.getChildren().add(toggleLink);

        final boolean[] showReg = {false};
        toggleLink.setOnMouseClicked(e -> {
            showReg[0] = !showReg[0];
            loginSection.setVisible(!showReg[0]);
            loginSection.setManaged(!showReg[0]);
            registerSection.setVisible(showReg[0]);
            registerSection.setManaged(showReg[0]);
            toggleLink.setText(showReg[0] ? "Back to Sign In" : "No account? Register here");
        });

        loginBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passField.getText().trim();
            if (user.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                errorLabel.setVisible(true);
                return;
            }
            TeacherModel teacher = AuthService.loginTeacher(user, pass);
            if (teacher == null) {
                errorLabel.setText("Invalid credentials.");
                errorLabel.setVisible(true);
            } else {
                SceneManager.showTeacherDashboard(teacher);
            }
        });

        registerBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String contact = contactField.getText().trim();
            String pass = regPassField.getText();
            String confirm = confirmPassField.getText();
            if (name.isEmpty() || id.isEmpty() || contact.isEmpty() || pass.isEmpty()) {
                regErrorLabel.setText("All fields are required.");
                regErrorLabel.setVisible(true);
                regSuccessLabel.setVisible(false);
                return;
            }
            if (!pass.equals(confirm)) {
                regErrorLabel.setText("Passwords do not match.");
                regErrorLabel.setVisible(true);
                regSuccessLabel.setVisible(false);
                return;
            }
            boolean ok = AuthService.registerTeacher(name, id, contact, pass);
            if (ok) {
                regSuccessLabel.setText("Registered! Sign in with your contact and password.");
                regSuccessLabel.setVisible(true);
                regErrorLabel.setVisible(false);
                nameField.clear(); idField.clear(); contactField.clear(); regPassField.clear(); confirmPassField.clear();
            } else {
                regErrorLabel.setText("Contact already registered.");
                regErrorLabel.setVisible(true);
                regSuccessLabel.setVisible(false);
            }
        });

        form.getChildren().addAll(loginSection, sep, registerSection, toggleBox);
        return form;
    }

    public BorderPane getRoot() { return root; }
}
