package cz.nguyeha.todolist.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String title;
    private LocalDate dueDate;
    private String description;
    private boolean completed;

    public Task(String title, LocalDate dueDate, String description) {
        this.title = title;
        this.dueDate = dueDate;
        this.description = description;
        this.completed = false;
    }

    public Task(String title, LocalDate dueDate, String description, boolean completed, int id) {
        this(title, dueDate, description);
        this.completed = completed;
        this.id = id;
    }

    public int getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return String.format("%s (Due: %s) %s",
                title,
                dueDate != null ? dueDate.format(formatter) : "No date",
                completed ? "[Completed]" : "[Pending]");
    }
}

