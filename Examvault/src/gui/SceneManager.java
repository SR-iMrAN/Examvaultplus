package gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import services.AuthService;

public class SceneManager {
    private Stage primaryStage;
    private Scene scene;
    private BorderPane root;
    private AuthService authService;

    public SceneManager(Stage stage, AuthService authService) {
        this.primaryStage = stage;
        this.authService = authService;
        this.root = new BorderPane();
        this.scene = new Scene(root, 900, 700);
    }

    public void showLoginView() {
        root.setCenter(new LoginView(this).getView());
    }

    public void showStudentDashboard() {
        root.setCenter(new StudentDashboard(this, authService).getView());
    }

    public void showTeacherDashboard() {
        root.setCenter(new TeacherDashboard(this, authService).getView());
    }

    public void showQuizView(String subject) {
        root.setCenter(new QuizView(this, authService, subject).getView());
    }

    public Scene getScene() {
        return scene;
    }

    public AuthService getAuthService() {
        return authService;
    }
}
