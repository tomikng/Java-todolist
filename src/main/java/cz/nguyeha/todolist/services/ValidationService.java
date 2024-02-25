package cz.nguyeha.todolist.services;

import java.time.LocalDate;

public class ValidationService {
    public static boolean isValidTaskInput(String title, LocalDate dueDate) {
        return !(title == null || title.trim().isEmpty() || dueDate == null);
    }

    public static String generateErrorMessage(String title, LocalDate dueDate) {
        StringBuilder errorMessage = new StringBuilder();
        if (title == null || title.trim().isEmpty()) {
            errorMessage.append("Task title cannot be empty.\n");
        }
        if (dueDate == null) {
            errorMessage.append("Please select a due date for the task.\n");
        }
        return errorMessage.toString();
    }
}
