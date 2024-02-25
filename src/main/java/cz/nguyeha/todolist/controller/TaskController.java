package cz.nguyeha.todolist.controller;

import cz.nguyeha.todolist.database.DatabaseHelper;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
                    private final Button completeButton = new Button();
                    private final Region spacer = new Region();

                    {
                        hbox.getChildren().addAll(nameLabel, spacer, deleteButton, completeButton);
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        deleteButton.setOnAction(event -> deleteTask(getItem()));
                        completeButton.setOnAction(event -> {
                            if (getItem() != null) {
                                toggleTaskCompletion(getItem());
                            }
                        });
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
                            completeButton.setText(item.isCompleted() ? "Not Done" : "Complete");
                            setGraphic(hbox);
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
        refreshTaskList();
    }

    private void toggleTaskCompletion(Task task) {
        if (task != null) {
            task.setCompleted(!task.isCompleted());
            DatabaseHelper.updateTask(task);
            refreshTaskList();
        }
    }

    private void refreshTaskList() {
        taskListView.getItems().setAll(DatabaseHelper.getAllTasks());
    }

    @FXML
    protected void addTask() {
        String title = titleTextField.getText().trim();
        LocalDate dueDate = datePicker.getValue();
        String description = descriptionTextField.getText().trim();

        StringBuilder errorMessage = new StringBuilder();

        if (title.isEmpty()) {
            errorMessage.append("Task title cannot be empty.\n");
        }

        if (dueDate == null) {
            errorMessage.append("Please select a due date for the task.\n");
        }

        if (errorMessage.length() > 0) {
            showAlertWithHeaderText("Validation Error", errorMessage.toString());
            return;
        }

        Task newTask = new Task(title, dueDate, description);
        DatabaseHelper.createTask(newTask);
        refreshTaskList();
        clearInputFields();
    }

    private void showAlertWithHeaderText(String headerText, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearInputFields() {
        titleTextField.clear();
        datePicker.setValue(null);
        descriptionTextField.clear();
        titleTextField.getStyleClass().remove("error");
        datePicker.getStyleClass().remove("error");
    }

    private void showDetails(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/nguyeha/todolist/task-detail-view.fxml"));
            Parent root = loader.load();

            TaskDetailController controller = loader.getController();
            controller.setTask(task);

            Stage stage = new Stage();
            stage.setTitle("Task Details");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> refreshTaskList());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void deleteTask(Task task) {
        DatabaseHelper.deleteTask(task.getId());
        refreshTaskList();
    }
}
