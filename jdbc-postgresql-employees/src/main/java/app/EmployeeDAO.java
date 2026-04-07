package app;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDAO class contains CRUD operations for employees table.
 */
public class EmployeeDAO {
    private final DatabaseConnector connector;

    public EmployeeDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    /**
     * Adds a new employee to the database.
     */
    public void addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (name, age, position, salary) VALUES (?, ?, ?, ?) RETURNING id";
        connector.connect();
        try (PreparedStatement pstmt = connector.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPosition());
            pstmt.setFloat(4, employee.getSalary());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    employee.setId(rs.getInt("id"));
                }
            }
        }
        System.out.println("Employee added successfully.");
    }

    /**
     * Updates an existing employee.
     */
    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET name = ?, age = ?, position = ?, salary = ? WHERE id = ?";
        connector.connect();
        try (PreparedStatement pstmt = connector.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPosition());
            pstmt.setFloat(4, employee.getSalary());
            pstmt.setInt(5, employee.getId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        }
    }

    /**
     * Deletes an employee by id.
     */
    public void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        connector.connect();
        try (PreparedStatement pstmt = connector.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        }
    }

    /**
     * Retrieves all employees from the database.
     */
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        connector.connect();
        try (Statement stmt = connector.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("position"),
                        rs.getFloat("salary")
                );
                employees.add(employee);
            }
        }
        return employees;
    }
}