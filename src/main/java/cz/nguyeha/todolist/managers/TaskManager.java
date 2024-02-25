package cz.nguyeha.todolist.managers;

import cz.nguyeha.todolist.database.DatabaseHelper;
import cz.nguyeha.todolist.model.Task;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {

    public List<Task> getAllTasks() {
        return DatabaseHelper.getAllTasks();
    }

    public void createTask(Task task) {
        DatabaseHelper.createTask(task);
    }

    public void updateTask(Task task) {
        DatabaseHelper.updateTask(task);
    }

    public void deleteTask(int taskId) {
        DatabaseHelper.deleteTask(taskId);
    }

    public List<Task> getFilteredAndSortedTasks(String filterValue, String sortValue, boolean isAscending) {
        List<Task> tasks = getAllTasks();

        // Filter based on completion status
        if (!"All".equals(filterValue)) {
            boolean isCompleted = "Completed".equals(filterValue);
            tasks = tasks.stream()
                    .filter(task -> task.isCompleted() == isCompleted)
                    .collect(Collectors.toList());
        }

        // Sort based on the sortValue and isAscending
        Comparator<Task> comparator = null;
        if ("Priority".equals(sortValue)) {
            comparator = Comparator.comparing(Task::getPriority);
        } else if ("Date".equals(sortValue)) {
            comparator = Comparator.comparing(Task::getDueDate);
        }

        if (comparator != null) {
            if (!isAscending) {
                comparator = comparator.reversed();
            }
            tasks = tasks.stream().sorted(comparator).collect(Collectors.toList());
        }

        return tasks;
    }
}
