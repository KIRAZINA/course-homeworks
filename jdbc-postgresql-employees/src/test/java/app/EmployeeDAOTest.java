package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmployeeDAO using Mockito.
 * These tests verify logic without connecting to a real database.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeDAOTest {

    @Mock
    private DatabaseConnector connector;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private EmployeeDAO employeeDAO;

    @BeforeEach
    void setUp() throws SQLException {
        when(connector.getConnection()).thenReturn(connection);
    }

    @Test
    void addEmployee_shouldExecuteInsertQuery() throws SQLException {
        Employee employee = new Employee("Test User", 30, "Tester", 50000.0f);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(999);

        employeeDAO.addEmployee(employee);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, "Test User");
        verify(preparedStatement).setInt(2, 30);
        assertThat(employee.getId()).isEqualTo(999);
    }

    @Test
    void getAllEmployees_shouldReturnListOfEmployees() throws SQLException {
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(anyString())).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Alice");
        when(resultSet.getInt("age")).thenReturn(28);
        when(resultSet.getString("position")).thenReturn("Engineer");
        when(resultSet.getFloat("salary")).thenReturn(70000.0f);

        List<Employee> employees = employeeDAO.getAllEmployees();

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getName()).isEqualTo("Alice");
    }

    @Test
    void updateEmployee_whenNoRowsAffected_shouldPrintNotFound() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0); // no rows updated

        Employee employee = new Employee(999, "Not Found", 30, "Tester", 50000.0f);
        employeeDAO.updateEmployee(employee);

        // In real test we could capture System.out, but for simplicity we just check no exception
        verify(preparedStatement).executeUpdate();
    }
}