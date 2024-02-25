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

    /**
     * The setTask function is used to set the values of the fields in this controller
     * to match those of a given task. This function is called when a user clicks on
     * an existing task in the main window, and it allows them to edit that task's details.
     *
     * @param task Set the task to be edited
     *
     *
     */
    public void setTask(Task task) {
        this.task = task;
        titleTextField.setText(task.getTitle());
        datePicker.setValue(task.getDueDate());
        descriptionTextField.setText(task.getDescription());
        completedCheckBox.setSelected(task.isCompleted());
        priorityComboBox.setValue(task.getPriority().toString());
    }

    /**
     * Saves the task to the database.
     */
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

    /**
     * Closes the stage that is currently open.
     */
    private void closeStage() {
        Stage stage = (Stage) titleTextField.getScene().getWindow();
        stage.close();
    }
}
