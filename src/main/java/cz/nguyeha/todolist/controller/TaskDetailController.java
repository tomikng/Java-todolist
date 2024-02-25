package cz.nguyeha.todolist.controller;

import cz.nguyeha.todolist.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TaskDetailController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label descriptionLabel;

    public void setTask(Task task) {
        titleLabel.setText(task.getTitle());
        dateLabel.setText(task.getDueDate().toString());
        descriptionLabel.setText(task.getDescription());
    }
}
