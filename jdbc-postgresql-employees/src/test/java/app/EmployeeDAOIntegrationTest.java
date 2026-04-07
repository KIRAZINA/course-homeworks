package app;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for EmployeeDAO using Testcontainers.
 * One PostgreSQL container is shared, but we clean the table before each test.
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeDAOIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("company_test")
            .withUsername("postgres")
            .withPassword("postgres");

    private DatabaseConnector connector;
    private EmployeeDAO employeeDAO;

    @BeforeEach
    void setUp() throws SQLException {
        connector = new DatabaseConnector();

        // Connect to Testcontainers PostgreSQL
        connector.connection = java.sql.DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        System.out.println("Connected to Testcontainers PostgreSQL for integration tests.");

        connector.createTable();

        cleanTable();

        employeeDAO = new EmployeeDAO(connector);
    }

    /**
     * Deletes all rows and resets the serial sequence so IDs start from 1 again.
     */
    private void cleanTable() throws SQLException {
        try (Statement stmt = connector.connection.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE employees RESTART IDENTITY CASCADE");
            System.out.println("Table 'employees' has been cleaned for the next test.");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connector != null) {
            connector.close();
        }
    }

    @Test
    @Order(1)
    void shouldAddAndRetrieveEmployee() throws SQLException {
        Employee emp = new Employee("Integration Test User", 25, "Developer", 65000.0f);
        employeeDAO.addEmployee(emp);

        List<Employee> employees = employeeDAO.getAllEmployees();

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getName()).isEqualTo("Integration Test User");
        assertThat(employees.get(0).getSalary()).isEqualTo(65000.0f);
    }

    @Test
    @Order(2)
    void shouldUpdateEmployeeSalary() throws SQLException {
        Employee emp = new Employee("Update Test", 40, "Manager", 80000.0f);
        employeeDAO.addEmployee(emp);

        emp.setSalary(95000.0f);
        employeeDAO.updateEmployee(emp);

        List<Employee> employees = employeeDAO.getAllEmployees();
        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getSalary()).isEqualTo(95000.0f);
    }

    @Test
    @Order(3)
    void shouldDeleteEmployee() throws SQLException {
        Employee emp = new Employee("To Delete", 30, "Tester", 50000.0f);
        employeeDAO.addEmployee(emp);

        employeeDAO.deleteEmployee(emp.getId());

        List<Employee> employees = employeeDAO.getAllEmployees();
        assertThat(employees).isEmpty();
    }
}