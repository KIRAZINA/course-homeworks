package app.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import app.transport.Plane;
import app.transport.Transport;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the PlaneFactory class.
 */
@DisplayName("Unit tests for PlaneFactory")
class PlaneFactoryTest {

    @Test
    @DisplayName("PlaneFactory should create a Plane object")
    void testCreateTransportReturnsPlane() {
        TransportFactory factory = new PlaneFactory();
        Transport transport = factory.createTransport();

        assertNotNull(transport, "Created transport should not be null");
        assertInstanceOf(Plane.class, transport, "Factory should return instance of Plane");
    }
}