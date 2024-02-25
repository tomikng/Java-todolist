package cz.nguyeha.todolist;

import cz.nguyeha.todolist.database.DatabaseHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    /**
     * The start function is the main entry point for all JavaFX applications.
     * <br>
     * The start method is called after the init method has returned, and after
     * <br>
     * the system is ready for the application to begin running.
     *
     * @param primaryStage Create a new window
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/cz/nguyeha/todolist/task-view.fxml")));
        primaryStage.setTitle("Todo List");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    /**
     * The main function of the program.
     * @param args Pass arguments to the application
     */
    public static void main(String[] args) {
        DatabaseHelper.initializeDatabase();
        launch(args);
    }
}
