package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User class.
 * Validates that User correctly acts as Information Expert for its own address (GRASP Expert principle).
 */
class UserTest {

    @Test
    void testConstructorAndName() {
        // Given
        User user = new User("Alice");

        // Then
        assertEquals("Alice", user.getName(), "User name should be set correctly via constructor");
    }

    @Test
    void testSetAndGetAddress() {
        // Given
        User user = new User("Bob");
        Address address = new Address("456 Oak Ave", "Dnipro", "Ukraine");

        // When
        user.setAddress(address);

        // Then
        assertNotNull(user.getAddress(), "Address should not be null after setting");
        assertEquals(address, user.getAddress(), "getAddress() should return the same Address object");
        assertEquals("456 Oak Ave, Dnipro, Ukraine",
                user.getAddress().toString(),
                "Address should be correctly accessible through User");
    }

    @Test
    void testToString() {
        // Given
        User user = new User("Charlie");
        Address address = new Address("789 Pine St", "Odesa", "Ukraine");
        user.setAddress(address);

        String expected = "User: Charlie, Address: 789 Pine St, Odesa, Ukraine";

        // Then
        assertEquals(expected, user.toString(), "toString() should include both name and full address");
    }

    @Test
    void testAddressCanBeNullInitially() {
        // Given
        User user = new User("David");

        // Then
        assertNull(user.getAddress(), "New User should have null address until explicitly set");
    }
}