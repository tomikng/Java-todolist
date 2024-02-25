package cz.nguyeha.todolist;

import cz.nguyeha.todolist.database.DatabaseHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/cz/nguyeha/todolist/task-view.fxml")));
        primaryStage.setTitle("Todo List");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        DatabaseHelper.initializeDatabase();
        launch(args);
    }
}
