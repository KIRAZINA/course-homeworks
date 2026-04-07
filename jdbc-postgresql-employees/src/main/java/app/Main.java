package app;

import java.sql.SQLException;
import java.util.List;

/**
 * Main class to test all CRUD operations and display results.
 */
public class Main {
    public static void main(String[] args) {
        DatabaseConnector connector = new DatabaseConnector();
        EmployeeDAO employeeDAO = new EmployeeDAO(connector);

        try {
            // Connect and create table
            connector.connect();
            connector.createTable();

            System.out.println("\n--- Adding employees ---");
            Employee emp1 = new Employee("Alice Smith", 28, "Software Engineer", 70000.0f);
            employeeDAO.addEmployee(emp1);

            Employee emp2 = new Employee("Bob Johnson", 35, "Manager", 90000.0f);
            employeeDAO.addEmployee(emp2);

            // Show all employees
            System.out.println("\n--- All employees ---");
            List<Employee> employees = employeeDAO.getAllEmployees();
            for (Employee emp : employees) {
                System.out.println(emp);
            }

            // Update first employee
            System.out.println("\n--- Updating employee ---");
            if (!employees.isEmpty()) {
                Employee empToUpdate = employees.get(0);
                empToUpdate.setSalary(75000.0f);
                employeeDAO.updateEmployee(empToUpdate);
            }

            // Delete second employee
            System.out.println("\n--- Deleting employee ---");
            if (employees.size() >= 2) {
                int idToDelete = employees.get(1).getId();
                employeeDAO.deleteEmployee(idToDelete);
            }

            // Show final state
            System.out.println("\n--- All employees after update and delete ---");
            employees = employeeDAO.getAllEmployees();
            for (Employee emp : employees) {
                System.out.println(emp);
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connector.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}