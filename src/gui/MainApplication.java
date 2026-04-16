package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import repositories.StudentRepository;
import repositories.TeacherRepository;
import repositories.SubjectRepository;
import utils.OracleDatabase;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Attempt Oracle DB connection first.
        boolean dbReady = OracleDatabase.init();
        if (!dbReady) {
            System.out.println("Oracle database unavailable. Falling back to local text file storage.");
        }

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
