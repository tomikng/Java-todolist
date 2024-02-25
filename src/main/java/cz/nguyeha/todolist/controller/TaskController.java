package cz.nguyeha.todolist.controller;

import cz.nguyeha.todolist.managers.TaskManager;
import cz.nguyeha.todolist.model.Task;
import cz.nguyeha.todolist.services.ValidationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

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

    private TaskManager taskManager;

    public void initialize() {
        this.taskManager = new TaskManager();
        taskListView.setCellFactory(param -> new TaskListCell());
        refreshTaskList();
    }

    private void refreshTaskList() {
        taskListView.getItems().setAll(taskManager.getAllTasks());
    }

    @FXML
    protected void addTask() {
        String title = titleTextField.getText().trim();
        LocalDate dueDate = datePicker.getValue();
        String description = descriptionTextField.getText().trim();

        if (!ValidationService.isValidTaskInput(title, dueDate)) {
            showAlertWithHeaderText(ValidationService.generateErrorMessage(title, dueDate));
            return;
        }

        Task newTask = new Task(title, dueDate, description);
        taskManager.createTask(newTask);
        refreshTaskList();
        clearInputFields();
    }

    private void showAlertWithHeaderText(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Validation Error");
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearInputFields() {
        titleTextField.clear();
        datePicker.setValue(null);
        descriptionTextField.clear();
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

    private class TaskListCell extends ListCell<Task> {
        private final HBox hbox = new HBox(10);
        private final Label nameLabel = new Label();
        private final Button completeButton = new Button();

        TaskListCell() {
            Button deleteButton = new Button("Delete");
            Region spacer = new Region();
            hbox.getChildren().addAll(nameLabel, spacer, deleteButton, completeButton);
            HBox.setHgrow(spacer, Priority.ALWAYS);

            deleteButton.setOnAction(event -> {
                Task task = getItem();
                if (task != null) {
                    taskManager.deleteTask(task.getId());
                    refreshTaskList();
                }
            });

            completeButton.setOnAction(event -> {
                Task task = getItem();
                if (task != null) {
                    task.setCompleted(!task.isCompleted());
                    taskManager.updateTask(task);
                    refreshTaskList();
                }
            });

            this.setOnMouseClicked(event -> {
                System.out.println("Double clicked on " + getItem());
                if (event.getClickCount() == 2 && getItem() != null) {
                    showDetails(getItem());
                }
            });
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
            }
        }
    }
}
