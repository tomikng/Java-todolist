package cz.nguyeha.todolist.managers;

import cz.nguyeha.todolist.database.DatabaseHelper;
import cz.nguyeha.todolist.model.Task;

import java.util.List;

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
}
