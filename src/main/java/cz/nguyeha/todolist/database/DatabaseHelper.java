package cz.nguyeha.todolist.database;

import cz.nguyeha.todolist.model.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String URL = "jdbc:h2:~/todolist";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS TASKS(" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255)," +
                "dueDate DATE," +
                "description VARCHAR(255)," +
                "completed BOOLEAN" +
                ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTask(Task task) {
        String SQL_INSERT = "INSERT INTO tasks (title, dueDate, description, completed) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setDate(2, java.sql.Date.valueOf(task.getDueDate()));
            pstmt.setString(3, task.getDescription());
            pstmt.setBoolean(4, task.isCompleted());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                        rs.getInt("id")); // Ensure your Task constructor handles this
                taskList.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    public static void updateTask(Task task) {
        String SQL_UPDATE = "UPDATE tasks SET title=?, dueDate=?, description=?, completed=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setDate(2, java.sql.Date.valueOf(task.getDueDate()));
            pstmt.setString(3, task.getDescription());
            pstmt.setBoolean(4, task.isCompleted());
            pstmt.setInt(5, task.getId());
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Task updated successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
