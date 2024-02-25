package cz.nguyeha.todolist.controller;

import cz.nguyeha.todolist.model.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;

public class TaskController {

    @FXML
    private ListView<Task> taskListView;

    @FXML
    private TextField titleTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField descriptionTextField;

    public void initialize() {
        taskListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Task> call(ListView<Task> listView) {
                return new ListCell<>() {
                    private final HBox hbox = new HBox(10);
                    private final Label nameLabel = new Label();
                    private final Button deleteButton = new Button("Delete");
                    private final Button completeButton = new Button("Complete");
                    private final Region spacer = new Region();

                    {
                        hbox.getChildren().addAll(nameLabel, spacer, deleteButton, completeButton);
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        deleteButton.setOnAction(event -> deleteTask(getItem()));
                        completeButton.setOnAction(event -> markAsComplete(getItem()));
                        nameLabel.setOnMouseClicked(event -> showDetails(getItem()));
                    }

                    @Override
                    protected void updateItem(Task item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            nameLabel.setText(item.getTitle());
                            setGraphic(hbox);
                            // Optionally, handle click events on the whole cell (not just the nameLabel) if needed
                            setOnMouseClicked(event -> {
                                if (event.getClickCount() == 2 && getItem() != null) {
                                    showDetails(getItem());
                                }
                            });
                        }
                    }
                };
            }
        });
    }

    @FXML
    protected void addTask() {
        String title = titleTextField.getText();
        LocalDate dueDate = datePicker.getValue();
        String description = descriptionTextField.getText();

        Task newTask = new Task(title, dueDate, description);
        taskListView.getItems().add(newTask);

        titleTextField.clear();
        datePicker.setValue(null);
        descriptionTextField.clear();
    }

    private void showDetails(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/nguyeha/todolist/task-detail.fxml"));
            Parent root = loader.load();

            TaskDetailController controller = loader.getController();
            controller.setTask(task);

            Stage stage = new Stage();
            stage.setTitle("Task Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTask(Task task) {
        taskListView.getItems().remove(task);
    }

    private void markAsComplete(Task task) {
        task.setCompleted(true);
        // Optionally, refresh or update the ListView to visually indicate the task's completed state
        taskListView.refresh();
    }
}
