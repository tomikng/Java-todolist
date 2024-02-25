package cz.nguyeha.todolist.controller;

import cz.nguyeha.todolist.database.DatabaseHelper;
import cz.nguyeha.todolist.enums.Priority;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import cz.nguyeha.todolist.model.Task;

public class TaskDetailController {
    @FXML
    private TextField titleTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private CheckBox completedCheckBox;

    @FXML
    private ComboBox<String> priorityComboBox;



    private Task task;

    public void setTask(Task task) {
        this.task = task;
        titleTextField.setText(task.getTitle());
        datePicker.setValue(task.getDueDate());
        descriptionTextField.setText(task.getDescription());
        completedCheckBox.setSelected(task.isCompleted());
        priorityComboBox.setValue(task.getPriority().toString());
    }

    @FXML
    protected void saveTask() {
        task.setTitle(titleTextField.getText());
        task.setDueDate(datePicker.getValue());
        task.setDescription(descriptionTextField.getText());
        task.setCompleted(completedCheckBox.isSelected());
        task.setPriority(Priority.valueOf(priorityComboBox.getValue()));

        DatabaseHelper.updateTask(task);

        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) titleTextField.getScene().getWindow();
        stage.close();
    }
}
