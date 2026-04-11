package gui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.StudentModel;
import models.TeacherModel;

public class SceneManager {
    private static Stage primaryStage;
    private static final double WIDTH = 1100;
    private static final double HEIGHT = 720;

    public static void init(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("ExamVaultPlus");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
    }

    public static void showLoginView() {
        LoginView view = new LoginView();
        Scene scene = new Scene(view.getRoot(), WIDTH, HEIGHT);
        applyStylesheet(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showStudentDashboard(StudentModel student) {
        StudentDashboard dashboard = new StudentDashboard(student);
        Scene scene = new Scene(dashboard.getRoot(), WIDTH, HEIGHT);
        applyStylesheet(scene);
        primaryStage.setScene(scene);
    }

    public static void showTeacherDashboard(TeacherModel teacher) {
        TeacherDashboard dashboard = new TeacherDashboard(teacher);
        Scene scene = new Scene(dashboard.getRoot(), WIDTH, HEIGHT);
        applyStylesheet(scene);
        primaryStage.setScene(scene);
    }

    private static void applyStylesheet(Scene scene) {
        java.net.URL css = SceneManager.class.getResource("/css/style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            // fallback: try relative path
            java.io.File f = new java.io.File("css/style.css");
            if (f.exists()) scene.getStylesheets().add(f.toURI().toString());
        }
    }

    public static Stage getStage() { return primaryStage; }
}
