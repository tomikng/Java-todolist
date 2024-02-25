package cz.nguyeha.todolist.services;

import java.time.LocalDate;

public class ValidationService {
    public static boolean isValidTaskInput(String title, LocalDate dueDate) {
        return !(title == null || title.trim().isEmpty()) && dueDate != null && !dueDate.isBefore(LocalDate.now());
    }
    public static String generateErrorMessage(String title, LocalDate dueDate) {
        StringBuilder errorMessage = new StringBuilder();
        if (title == null || title.trim().isEmpty()) {
            errorMessage.append("Task title cannot be empty.\n");
        }
        if (dueDate == null) {
            errorMessage.append("Please select a due date for the task.\n");
        } else if (dueDate.isBefore(LocalDate.now())) {
            errorMessage.append("The due date cannot be in the past.\n");
        }
        return errorMessage.toString();
    }

}
