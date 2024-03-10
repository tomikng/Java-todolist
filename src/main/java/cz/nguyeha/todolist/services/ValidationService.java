package cz.nguyeha.todolist.services;

import java.time.LocalDate;

public class ValidationService {
    /**
     * Checks if the title and due date of a task are valid.
     * @param title Check if the title is null or empty
     * @param dueDate Check if the due date is before today's date
     */
    public static boolean isValidTaskInput(String title, LocalDate dueDate) {
        return !(title == null || title.trim().isEmpty()) && dueDate != null && !dueDate.isBefore(LocalDate.now());
    }
    /**
     * Takes in a title and due date, and returns an error message if the
     * title is empty or null, or if the due date is before today.
     * @param title Check if the title is empty or not
     * @param dueDate Check if the due date is in the past
     */
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
