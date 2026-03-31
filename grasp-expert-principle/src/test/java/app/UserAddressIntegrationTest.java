package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for User and Address classes working together.
 * Demonstrates the GRASP Expert principle: User is responsible for managing its own address.
 */
class UserAddressIntegrationTest {

    @Test
    void testUserManagesItsOwnAddress() {
        // Given
        User user = new User("Alice");
        Address address = new Address("123 Main St", "Dnipro", "Ukraine");

        // When - User takes responsibility for the address
        user.setAddress(address);

        // Then
        assertNotNull(user.getAddress(), "User should successfully store the address");
        assertEquals("Alice", user.getName(), "User name should remain correct");
        assertEquals("123 Main St, Dnipro, Ukraine",
                user.getAddress().toString(),
                "Address should be correctly stored and retrievable");

        // Verify full integration through toString()
        String fullInfo = user.toString();
        assertTrue(fullInfo.contains("Alice"), "User info should contain name");
        assertTrue(fullInfo.contains("123 Main St"), "User info should contain street");
        assertTrue(fullInfo.contains("Dnipro"), "User info should contain city");
        assertTrue(fullInfo.contains("Ukraine"), "User info should contain country");
    }

    @Test
    void testChangingAddress() {
        // Given
        User user = new User("Bob");
        Address oldAddress = new Address("Old St", "Kyiv", "Ukraine");
        Address newAddress = new Address("New St", "Lviv", "Ukraine");

        // When
        user.setAddress(oldAddress);
        assertEquals("Old St, Kyiv, Ukraine", user.getAddress().toString(), "Old address should be set correctly");

        user.setAddress(newAddress);   // change address

        // Then
        assertEquals("New St, Lviv, Ukraine",
                user.getAddress().toString(),
                "User should allow updating the address");
    }
}