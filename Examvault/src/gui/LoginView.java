package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import services.AuthService;

public class LoginView {
    private SceneManager sceneManager;
    private AuthService authService;
    private VBox root;
    private TabPane tabPane;

    public LoginView(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.authService = AuthService.getInstance();
        this.root = new VBox(15);
        this.root.setPadding(new Insets(30));
this.root.getStyleClass().addAll("root-cyber", "glass-panel");
        this.root.setAlignment(Pos.CENTER);

        createUI();
    }

    private void createUI() {
        // Header
        VBox header = createHeader();
        
        // Tab Pane for Login/Register
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-size: 12px;");

        // Login Tab
        Tab loginTab = new Tab("Login", createLoginForm());
        loginTab.setStyle("-fx-padding: 10px;");

        // Register Tab
        Tab registerTab = new Tab("Register", createRegisterForm());
        registerTab.setStyle("-fx-padding: 10px;");

        tabPane.getTabs().addAll(loginTab, registerTab);

        root.getChildren().addAll(header, tabPane);
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("ExamVaultPlus");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 40));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label("Smart Online Assessment Platform");
        subtitleLabel.setFont(Font.font("Segoe UI", 14));
        subtitleLabel.setStyle("-fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }

    private VBox createLoginForm() {
        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-color: white;");

        Label typeLabel = new Label("Login as:");
        typeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        ComboBox<String> userTypeCombo = new ComboBox<>();
        userTypeCombo.getItems().addAll("Student", "Teacher");
        userTypeCombo.setValue("Student");
        userTypeCombo.setStyle("-fx-min-width: 100%;");

        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-padding: 10px; -fx-font-size: 12px;");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-padding: 10px; -fx-font-size: 12px;");

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(350);
        loginButton.setPrefHeight(45);
        loginButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #3498db; " +
                "-fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10px; -fx-cursor: hand;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-color: #2980b9; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10px;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10px;"));

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String userType = userTypeCombo.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter username and password");
                return;
            }

            if ("Student".equals(userType)) {
                if (authService.loginStudent(username, password) != null) {
                    sceneManager.showStudentDashboard();
                } else {
                    messageLabel.setText("Invalid student credentials");
                }
            } else {
                if (authService.loginTeacher(username, password) != null) {
                    sceneManager.showTeacherDashboard();
                } else {
                    messageLabel.setText("Invalid teacher credentials");
                }
            }
        });

        form.getChildren().addAll(typeLabel, userTypeCombo, usernameLabel, usernameField, 
                passwordLabel, passwordField, loginButton, messageLabel);
        return form;
    }

    private VBox createRegisterForm() {
        VBox form = new VBox(15);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-color: white;");

        Label typeLabel = new Label("Register as:");
        typeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

        ComboBox<String> userTypeCombo = new ComboBox<>();
        userTypeCombo.getItems().addAll("Student", "Teacher");
        userTypeCombo.setValue("Student");
        userTypeCombo.setStyle("-fx-min-width: 100%;");

        Label nameLabel = new Label("Full Name:");
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setStyle("-fx-padding: 10px; -fx-font-size: 12px;");

        Label contactLabel = new Label("Contact Number:");
        contactLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        TextField contactField = new TextField();
        contactField.setPromptText("Enter your contact number");
        contactField.setStyle("-fx-padding: 10px; -fx-font-size: 12px;");

        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a username");
        usernameField.setStyle("-fx-padding: 10px; -fx-font-size: 12px;");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Choose a password");
        passwordField.setStyle("-fx-padding: 10px; -fx-font-size: 12px;");

        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 11));
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm password");
        confirmPasswordField.setStyle("-fx-padding: 10px; -fx-font-size: 12px;");

        Button registerButton = new Button("Register");
        registerButton.setPrefWidth(350);
        registerButton.setPrefHeight(45);
        registerButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #27ae60; " +
                "-fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10px; -fx-cursor: hand;");
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-color: #229954; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10px;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 10px;"));

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");

        registerButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String userType = userTypeCombo.getValue();

            if (name.isEmpty() || contact.isEmpty() || username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please fill all fields");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                return;
            }

            if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                return;
            }

            boolean success;
            if ("Student".equals(userType)) {
                success = authService.registerStudent(name, username, contact, password);
            } else {
                success = authService.registerTeacher(name, contact, username, password);
            }

            if (success) {
                messageLabel.setText("Registration successful! Please login.");
                messageLabel.setStyle("-fx-text-fill: #27ae60;");
                nameField.clear();
                contactField.clear();
                usernameField.clear();
                passwordField.clear();
                confirmPasswordField.clear();
            } else {
                messageLabel.setText("Username already exists or registration failed");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            }
        });

        form.getChildren().addAll(typeLabel, userTypeCombo, nameLabel, nameField,
                contactLabel, contactField, usernameLabel, usernameField,
                passwordLabel, passwordField, confirmPasswordLabel, confirmPasswordField,
                registerButton, messageLabel);
        return form;
    }

    public VBox getView() {
        return root;
    }
}
