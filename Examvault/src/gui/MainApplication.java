package gui;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.AuthService;

public class MainApplication extends Application {
    private Stage primaryStage;
    private AuthService authService;
    private SceneManager sceneManager;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.authService = AuthService.getInstance();
        this.sceneManager = new SceneManager(primaryStage, authService);

        primaryStage.setTitle("✦ ExamVaultPlus ✦");
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);
        primaryStage.setOnCloseRequest(e -> {
            authService.logout();
        });

        // Load and apply CSS styling
        URL cssUrl = this.getClass().getResource("/css/style.css");
        if (cssUrl != null) {
            sceneManager.getScene().getStylesheets().add(cssUrl.toExternalForm());
        } else {
            Path fallbackCss = Paths.get("Examvault/src/css/style.css");
            if (Files.exists(fallbackCss)) {
                sceneManager.getScene().getStylesheets().add(fallbackCss.toUri().toString());
            } else {
                System.err.println("Warning: style.css not found on classpath or fallback path.");
            }
        }

        sceneManager.showLoginView();
        primaryStage.setScene(sceneManager.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
