package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseConnector class handles connection to the PostgreSQL database,
 * creating the table, and closing the connection.
 */
public class DatabaseConnector {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/company";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    Connection connection;

    /**
     * Connects to the database.
     */
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully.");
        }
    }

    /**
     * Returns the current database connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Creates the employees table if it does not exist.
     * Uses SERIAL for auto-increment (PostgreSQL equivalent of AUTO_INCREMENT).
     */
    public void createTable() throws SQLException {
        connect();
        String sql = "CREATE TABLE IF NOT EXISTS employees (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "age INTEGER, " +
                "position VARCHAR(255), " +
                "salary FLOAT" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table 'employees' has been created (or already exists).");
        }
    }

    /**
     * Closes the database connection.
     */
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed.");
        }
    }
}