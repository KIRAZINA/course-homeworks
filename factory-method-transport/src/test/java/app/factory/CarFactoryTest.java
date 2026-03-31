package app.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import app.transport.Car;
import app.transport.Transport;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the CarFactory class.
 */
@DisplayName("Unit tests for CarFactory")
class CarFactoryTest {

    @Test
    @DisplayName("CarFactory should create a Car object")
    void testCreateTransportReturnsCar() {
        TransportFactory factory = new CarFactory();
        Transport transport = factory.createTransport();

        assertNotNull(transport, "Created transport should not be null");
        assertInstanceOf(Car.class, transport, "Factory should return instance of Car");
    }
}