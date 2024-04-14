package cz.nguyeha.todolist.database;

import cz.nguyeha.todolist.model.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String URL = "jdbc:h2:~/todolist";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    /**
     * Establish a connection with the database.
     * @return Connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Creates a new table in the database if one does not already exist.
     * <br>
     * The function is called when the program starts to ensure that there is a table for tasks to be stored in.
     */
    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS TASKS(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255)," +
                "dueDate DATE," +
                "description VARCHAR(255)," +
                "completed BOOLEAN," +
                "priority VARCHAR(50)," +
                "archived VARCHAR(50)," +
                "completedAt DATE" +
                ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes a Task object as an argument and inserts it into the database.
     * @param task Task to be inserted into the database
     */
    public static void createTask(Task task) {
        String SQL_INSERT = "INSERT INTO tasks (title, dueDate, description, completed, priority, archived, completedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT)) {
            createStatement(task, pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function that sets the values of the PreparedStatement
     * object to be used in an SQL query.
     * @throws SQLException if there is an error with the SQL query

     *
     * @param task Set the values of each column in the database
     * @param pstmt Pass the preparedstatement object into the method
     */
    private static void createStatement(Task task, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, task.getTitle());
        pstmt.setDate(2, Date.valueOf(task.getDueDate()));
        pstmt.setString(3, task.getDescription());
        pstmt.setBoolean(4, task.isCompleted());
        pstmt.setString(5, task.getPriority().toString());
        pstmt.setBoolean(6, task.isArchived());
        pstmt.setDate(7, task.getCompletedAt() != null ? Date.valueOf(task.getCompletedAt()) : null);
    }

    /**
     * Retrieves all tasks from the database and returns them as a list of Task objects.
     * @return list of all tasks in the database
     */
    public static List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String SQL_SELECT = "SELECT * FROM tasks";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT)) {
            while (rs.next()) {
                Task task = new Task(
                        rs.getString("title"),
                        rs.getDate("dueDate").toLocalDate(),
                        rs.getString("description"),
                        rs.getBoolean("completed"),
                        rs.getInt("id"),
                        rs.getString("priority"),
                        rs.getBoolean("archived"),
                        rs.getDate("completedAt") != null ? rs.getDate("completedAt").toLocalDate() : null
                );// Ensure your Task constructor handles this
                taskList.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    /**
     * Updates a task in the database.
     * @param task Pass the task object into the function
     */
    public static void updateTask(Task task) {
        String SQL_UPDATE = "UPDATE tasks SET title=?, dueDate=?, description=?, completed=?, priority=?, archived=?, completedAt=? WHERE id=?";
        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {
            createStatement(task, pstmt);
            pstmt.setInt(8, task.getId());
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Task updated successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a task from the database.
     * @param taskId Pass the id of the task to be deleted
     */
    public static void deleteTask(int taskId) {
        String SQL_DELETE = "DELETE FROM tasks WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {
            pstmt.setInt(1, taskId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Task deleted successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
