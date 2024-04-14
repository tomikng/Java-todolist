package cz.nguyeha.todolist.controller;

import cz.nguyeha.todolist.enums.Priority;
import cz.nguyeha.todolist.managers.TaskManager;
import cz.nguyeha.todolist.model.Task;
import cz.nguyeha.todolist.services.ValidationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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

    @FXML
    private ComboBox<Priority> priorityComboBox;

    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private ToggleButton ascendingSwitch;

    @FXML
    private CheckBox showArchivedCheckBox;

    private Boolean isAscending = false;

    private TaskManager taskManager;

    /**
     * The initialize function is called when the FXML file is loaded.
     * It sets up the taskListView, priorityComboBox, filterComboBox and sortComboBox.
     * <p>
     */
    public void initialize() {
        this.taskManager = new TaskManager();
        taskListView.setCellFactory(param -> new TaskListCell());

        priorityComboBox.setItems(FXCollections.observableArrayList(Priority.values()));
        priorityComboBox.setValue(Priority.valueOf("LOW"));

        // Setup filter and sort ComboBoxes
        filterComboBox.setItems(FXCollections.observableArrayList("All", "Completed", "Not Completed"));
        filterComboBox.setValue("All");
        filterComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> refreshTaskList());

        sortComboBox.setItems(FXCollections.observableArrayList("Priority", "Date"));
        sortComboBox.setValue("Priority");
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> refreshTaskList());

        showArchivedCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> refreshTaskList());

        refreshTaskList();
    }


    /**
     * The refreshTaskList function is used to update the taskListView with the current list of tasks.
     * It also updates the list based on the filter and sort ComboBoxes.
     */
    private void refreshTaskList() {
        String filterValue = filterComboBox.getValue();
        String sortValue = sortComboBox.getValue();
        boolean showArchived = showArchivedCheckBox.isSelected();
        taskListView.getItems().setAll(taskManager.getFilteredAndSortedTasks(filterValue, sortValue, isAscending, showArchived));
    }

    /**
     * The addTask function is called when the user clicks on the &quot;Add Task&quot; button.
     * <p>
     * It takes in all the input from each of the text fields and combo box, and
     * validates that they are not empty or invalid.
     * <ul>
     *     <li>If any of them are, it will display an alert to notify the user that they need to fill out all fields before adding a task.</li>
     *     <li>If everything is filled out correctly, then it will create a new task object with those values as parameters for its constructor function.</li>
     *     <li>Then it calls refreshTaskList() which updates our list view with our newly added task (which was added by calling createTask())</li>
     * </ul>
     */
    @FXML
    protected void addTask() {
        String title = titleTextField.getText().trim();
        LocalDate dueDate = datePicker.getValue();
        String description = descriptionTextField.getText().trim();
        Priority priority = priorityComboBox.getValue();

        if (!ValidationService.isValidTaskInput(title, dueDate)) {
            showAlertWithHeaderText(ValidationService.generateErrorMessage(title, dueDate));
            return;
        }

        Task newTask = new Task(title, dueDate, description, priority);
        taskManager.createTask(newTask);
        refreshTaskList();
        clearInputFields();
    }

    /**
     * Used to display an error message when the user enters invalid input.
     * <p>
     *
     * @param content Display the error message
     */
    private void showAlertWithHeaderText(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Incorrect input");
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Clears all the input fields in the GUI.
     * <p>
     */
    private void clearInputFields() {
        titleTextField.clear();
        datePicker.setValue(null);
        descriptionTextField.clear();
        priorityComboBox.setValue(Priority.LOW); // Reset to default
    }


    /**
     * Used to display the details of a task in a new window.
     * <p>
     *
     * @param task which will be displayed in the new window
     */
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

    /**
     * Is called when the user clicks on the ascendingSwitch button.
     * <p>
     * It toggles whether tasks are sorted in ascending order, and updates the text of
     * the button to reflect this change.
     * <p>
     * Finally, it calls refreshTaskList() so that any changes made by this function will be reflected in the task list view.
     */
    @FXML
    private void ascendingSwitch() {
        isAscending = !isAscending;
        ascendingSwitch.setText(isAscending ? "Ascending" : "Descending"); // Update button text based on current state
        refreshTaskList();
    }

    private class TaskListCell extends ListCell<Task> {
        private final HBox hbox = new HBox(10);
        private final Label nameLabel = new Label();
        private final Label dateLabel = new Label();
        private final Label priorityLabel = new Label();
        private final Button completeButton = new Button();
        private final Button deleteButton = new Button("Delete");
        private final Region spacer = new Region();
        private final Button archiveButton = new Button("Archive");
        private final Button unArchiveButton = new Button("Unarchive");

        /**
         * The TaskListCell is a subclass of ListCell that allows for the creation of a cell in the task list.
         * <p></p>
         * The function contains two buttons, one to delete and one to complete tasks. It also has three labels,
         * which display the <strong>name</strong>, <strong>name</strong> and <strong>priority</strong> of each task. The TaskListCell class also contains an onMouseClicked event handler
         * that displays details about a selected task when it is double-clicked by the user.
         * <p>
         */
        TaskListCell() {
            hbox.setSpacing(10);
            hbox.getChildren().addAll(nameLabel, dateLabel, priorityLabel, spacer, completeButton, deleteButton);
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
//          Delete Button
            deleteButton.setOnAction(event -> {
                Task task = getItem();
                if (task != null) {
                    taskManager.deleteTask(task.getId());
                    refreshTaskList();
                }
            });
//          Complete Button
            completeButton.setOnAction(event -> {
                Task task = getItem();
                if (task != null) {
                    task.setCompleted(!task.isCompleted());
                    if (task.isCompleted()) {
                        task.setCompletedAt(LocalDate.now());
                    } else {
                        task.setCompletedAt(null);
                    }
                    taskManager.updateTask(task);
                    refreshTaskList();
                }
            });
//          Double Click Event
            this.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && getItem() != null) {
                    showDetails(getItem());
                }
            });

            archiveButton.setOnAction(event -> {
                Task task = getItem();
                if (task != null) {
                    task.setArchived(true);
                    taskManager.updateTask(task);
                    refreshTaskList();
                }
            });

            unArchiveButton.setOnAction(event -> {
                Task task = getItem();
                if (task != null) {
                    task.setArchived(false);
                    taskManager.updateTask(task);
                    refreshTaskList();
                }
            });
        }

        /**
         * The updateItem function is called whenever the item in a cell changes.
         * If the item is null, then it means that there are no more items to show, and so we set all of our labels to null.
         * Otherwise, we update each label with information from the Task object and add them to an HBox which will be displayed as our cell's graphic.
         *
         * @param item Get the title, due date and priority of the task
         * @param empty Determine if the cell is empty or not
         *
         */
        @Override
        protected void updateItem(Task item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                nameLabel.setText(item.getTitle());
                dateLabel.setText(item.getDueDate().toString());
                priorityLabel.setText(item.getPriority().toString());
                completeButton.setText(item.isCompleted() ? "Completed" : "Mark Complete");
                String priorityStyle = getPriorityStyle(item.getPriority());
                priorityLabel.setStyle(priorityStyle);

                if (item.isArchived()) {
                    hbox.getChildren().setAll(nameLabel, dateLabel, priorityLabel, spacer, completeButton, unArchiveButton, deleteButton);
                } else {
                    hbox.getChildren().setAll(nameLabel, dateLabel, priorityLabel, spacer, completeButton, archiveButton, deleteButton);
                }

                setGraphic(hbox);
            }
        }

        /**
         * Helper function to determine style of priority tag
         *
         * @param priority Determine which style to return
         *
         * @return String
         *
         */
        private String getPriorityStyle(Priority priority) {
            return switch (priority) {
                case HIGH -> "-fx-background-color: red; -fx-text-fill: white; -fx-padding: 3px;";
                case MEDIUM -> "-fx-background-color: orange; -fx-text-fill: white; -fx-padding: 3px;";
                case LOW -> "-fx-background-color: green; -fx-text-fill: white; -fx-padding: 3px;";
            };
        }

    }


}
