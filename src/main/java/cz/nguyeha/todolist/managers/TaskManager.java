package cz.nguyeha.todolist.managers;

import cz.nguyeha.todolist.database.DatabaseHelper;
import cz.nguyeha.todolist.model.Task;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {

    /**
     * Returns a list of all the tasks in the database.
     * @return A list of task objects
     */
    public List<Task> getAllTasks() {
        return DatabaseHelper.getAllTasks();
    }

    /**
     * Creates a new task in the database.
     * @param task Create a new task in the database
     */
    public void createTask(Task task) {
        DatabaseHelper.createTask(task);
    }

    /**
     * Updates the task in the database.
     * @param task Pass the task object into the function
     */
    public void updateTask(Task task) {
        DatabaseHelper.updateTask(task);
    }

    /**
     * Deletes a task from the database.
     * @param taskId Identify the task that is to be deleted
     */
    public void deleteTask(int taskId) {
        DatabaseHelper.deleteTask(taskId);
    }

    /**
     * Returns a list of tasks that are filtered and sorted according to the
     * filterValue and sortValue parameters. The isAscending parameter determines whether the sorting should be in ascending
     * or descending order. If no filtering or sorting is required, simply return all tasks as a list.

     *
     * @param filterValue Filter the list of tasks
     * @param sortValue Determine which field to sort by
     * @param isAscending Determine whether the tasks are sorted in ascending or descending order
     *
     * @return A list of tasks
     *
     */
    public List<Task> getFilteredAndSortedTasks(String filterValue, String sortValue, boolean isAscending) {
        List<Task> tasks = getAllTasks();

        // Filter
        if (!"All".equals(filterValue)) {
            boolean isCompleted = "Completed".equals(filterValue);
            tasks = tasks.stream()
                    .filter(task -> task.isCompleted() == isCompleted)
                    .collect(Collectors.toList());
        }

        // Sort
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
