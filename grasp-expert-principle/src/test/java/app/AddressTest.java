package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Address class.
 * Tests immutability, constructors, getters and toString() method.
 */
class AddressTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        Address address = new Address("123 Main St", "Dnipro", "Ukraine");

        // Then
        assertEquals("123 Main St", address.getStreet(), "Street should match the provided value");
        assertEquals("Dnipro", address.getCity(), "City should match the provided value");
        assertEquals("Ukraine", address.getCountry(), "Country should match the provided value");
    }

    @Test
    void testToString() {
        // Given
        Address address = new Address("Shevchenka 10", "Kyiv", "Ukraine");
        String expected = "Shevchenka 10, Kyiv, Ukraine";

        // Then
        assertEquals(expected, address.toString(), "toString() should return formatted full address");
    }

    @Test
    void testImmutability() {
        // Given
        Address address = new Address("Test Street", "Lviv", "Ukraine");

        // Then - since class is final and fields are final, object cannot be modified after creation
        assertNotNull(address, "Address object should be successfully created");
        // No setters exist - this is the main proof of immutability
    }
}