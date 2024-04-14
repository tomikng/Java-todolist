package cz.nguyeha.todolist.model;

import cz.nguyeha.todolist.enums.Priority;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String title;
    private LocalDate dueDate;
    private String description;
    private boolean completed;

    private Priority priority;
    private boolean archived;
    private LocalDate completedAt;

    /**
     * The Task function is a constructor that creates a new Task object.
     *
     * @param title Set the title of a task
     * @param dueDate Set the duedate field
     * @param description Set the description of the task
     * @param priority Set the priority of a task
     */
    public Task(String title, LocalDate dueDate, String description, Priority priority) {
        this.title = title;
        this.dueDate = dueDate;
        this.description = description;
        this.completed = false;
        this.priority = priority;
        this.archived = false;
        this.completedAt = null;
    }

    /**
     * The Task function is a constructor that creates a new Task object with an <strong>id</strong> and <strong>completed</strong> parameter.
     *
     * @param title Set the title of the task
     * @param dueDate Set the duedate variable
     * @param description Set the description of a task
     * @param completed Set the completed property of the task object
     * @param id Set the id of the task
     * @param priority Set the priority of a task
     */
    public Task(String title, LocalDate dueDate, String description, boolean completed, int id, Priority priority, boolean archived, LocalDate completedAt) {
        this(title, dueDate, description, priority);
        this.completed = completed;
        this.id = id;
        this.archived = archived;
        this.completedAt = completedAt;
    }

    /**
     * The Task function is a constructor that creates a new Task object and <strong>priority as string</strong>.
     * @param title Set the title of a task
     * @param dueDate Set the duedate field
     * @param description Set the description of the task
     * @param completed Determine whether the task is completed or not
     * @param id Set the id of the task
     * @param priority Set the priority of a task
     */
    public Task(String title, LocalDate dueDate, String description, boolean completed, int id, String priority, boolean archived, LocalDate completedAt) {
        this(title, dueDate, description, completed, id, Priority.valueOf(priority), archived, completedAt);
    }

    // Getters and setters
    public int getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    public LocalDate getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDate completedAt) {
        this.completedAt = completedAt;
    }

    /**
     * The toString function is used to print out the title, due date, and completion status of a task.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return String.format("%s (Due: %s) %s",
                title,
                dueDate != null ? dueDate.format(formatter) : "No date",
                completed ? "[Completed]" : "[Pending]");
    }
}

