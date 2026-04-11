package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import repositories.StudentRepository;
import repositories.TeacherRepository;
import repositories.SubjectRepository;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load all data
        new java.io.File("data").mkdirs();
        new java.io.File("data/subject").mkdirs();

        StudentRepository.load();
        TeacherRepository.load();
        SubjectRepository.load();

        // Launch the app via SceneManager
        SceneManager.init(primaryStage);
        SceneManager.showLoginView();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
