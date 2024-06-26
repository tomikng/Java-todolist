package cz.nguyeha.todolist.controller;

import cz.nguyeha.todolist.enums.Priority;
import cz.nguyeha.todolist.managers.TaskManager;
import cz.nguyeha.todolist.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphController {
    @FXML private PieChart completedTasksChart;
    @FXML private LineChart<String, Number> dueDateLineChart;
    @FXML private LineChart<String, Number> completedTasksLineChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private CategoryAxis completedXAxis;
    @FXML private NumberAxis completedYAxis;
    @FXML private ComboBox<String> timePeriodComboBox;

    private TaskManager taskManager;

    public void initialize() {
        this.taskManager = new TaskManager();
        timePeriodComboBox.setItems(FXCollections.observableArrayList("Month", "Week", "Day"));
        timePeriodComboBox.setValue("Month");
        timePeriodComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> refreshGraphs());

        refreshGraphs();
    }

    @FXML
    private void refreshGraphs() {
        populateCompletedTasksChart();
        populateDueDateLineChart();
        populateCompletedTasksLineChart();
    }

    private void populateCompletedTasksChart() {
        List<Task> completedTasks = taskManager.getCompletedTasks();
        int highPriorityCount = (int) completedTasks.stream().filter(task -> task.getPriority() == Priority.HIGH).count();
        int mediumPriorityCount = (int) completedTasks.stream().filter(task -> task.getPriority() == Priority.MEDIUM).count();
        int lowPriorityCount = (int) completedTasks.stream().filter(task -> task.getPriority() == Priority.LOW).count();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("High Priority", highPriorityCount),
                new PieChart.Data("Medium Priority", mediumPriorityCount),
                new PieChart.Data("Low Priority", lowPriorityCount)
        );

        completedTasksChart.setData(pieChartData);
    }

    private void populateDueDateLineChart() {
        List<Task> allTasks = taskManager.getAllTasks();
        String timePeriod = timePeriodComboBox.getValue();

        Map<String, Long> taskCountByDueDate;
        DateTimeFormatter formatter;

        switch (timePeriod) {
            case "Month" -> {
                formatter = DateTimeFormatter.ofPattern("MMM yyyy");
                taskCountByDueDate = allTasks.stream()
                        .collect(Collectors.groupingBy(task -> task.getDueDate().format(formatter), Collectors.counting()));
            }
            case "Week" -> {
                formatter = DateTimeFormatter.ofPattern("'Week' w, yyyy");
                taskCountByDueDate = allTasks.stream()
                        .collect(Collectors.groupingBy(task -> "Week " + task.getDueDate().format(formatter), Collectors.counting()));
            }
            case "Day" -> {
                formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                taskCountByDueDate = allTasks.stream()
                        .collect(Collectors.groupingBy(task -> task.getDueDate().format(formatter), Collectors.counting()));
            }
            default -> throw new IllegalStateException("Unexpected value: " + timePeriod);
        }

        xAxis.setLabel(timePeriod);
        yAxis.setLabel("Tasks");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Long> entry : taskCountByDueDate.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        dueDateLineChart.getData().clear();
        dueDateLineChart.getData().add(series);
    }

    private void populateCompletedTasksLineChart() {
        List<Task> completedTasks = taskManager.getCompletedTasks();
        String timePeriod = timePeriodComboBox.getValue();

        Map<String, Long> completedTasksCount;
        DateTimeFormatter formatter;

        switch (timePeriod) {
            case "Month" -> {
                formatter = DateTimeFormatter.ofPattern("MMM yyyy");
                completedTasksCount = completedTasks.stream()
                        .collect(Collectors.groupingBy(task -> task.getCompletedAt().format(formatter), Collectors.counting()));
            }
            case "Week" -> {
                formatter = DateTimeFormatter.ofPattern("'Week' w, yyyy");
                completedTasksCount = completedTasks.stream()
                        .collect(Collectors.groupingBy(task -> "Week " + task.getCompletedAt().format(formatter), Collectors.counting()));
            }
            case "Day" -> {
                formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                completedTasksCount = completedTasks.stream()
                        .collect(Collectors.groupingBy(task -> task.getCompletedAt().format(formatter), Collectors.counting()));
            }
            default -> throw new IllegalStateException("Unexpected value: " + timePeriod);
        }

        completedXAxis.setLabel(timePeriod);
        completedYAxis.setLabel("Completed Tasks");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Long> entry : completedTasksCount.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        completedTasksLineChart.getData().clear();
        completedTasksLineChart.getData().add(series);
    }
}