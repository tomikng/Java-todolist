package cz.nguyeha.todolist.managers;

import cz.nguyeha.todolist.database.DatabaseHelper;
import cz.nguyeha.todolist.model.Task;

import java.util.Collections;
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
    public List<Task> getFilteredAndSortedTasks(String filterValue, String sortValue, boolean isAscending, boolean showArchived) {
        List<Task> filteredTasks = getAllTasks();

        // Filter tasks based on completion status and archived status
        if (filterValue.equals("Completed")) {
            filteredTasks = filteredTasks.stream().filter(Task::isCompleted).collect(Collectors.toList());
        } else if (filterValue.equals("Not Completed")) {
            filteredTasks = filteredTasks.stream().filter(task -> !task.isCompleted()).collect(Collectors.toList());
        }

        // Filter archived tasks based on showArchived parameter
        if (!showArchived) {
            filteredTasks = filteredTasks.stream().filter(task -> !task.isArchived()).collect(Collectors.toList());
        } else {
            filteredTasks = filteredTasks.stream().filter(Task::isArchived).collect(Collectors.toList());
        }

        // Sort tasks based on the selected criteria
        if (sortValue.equals("Priority")) {
            filteredTasks.sort(Comparator.comparing(Task::getPriority));
        } else if (sortValue.equals("Date")) {
            filteredTasks.sort(Comparator.comparing(Task::getDueDate));
        }

        // Reverse the order if descending is selected
        if (!isAscending) {
            Collections.reverse(filteredTasks);
        }

        return filteredTasks;
    }
}
