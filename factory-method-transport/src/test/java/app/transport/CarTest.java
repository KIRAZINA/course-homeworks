package app.transport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Car class.
 */
@DisplayName("Unit tests for Car")
class CarTest {

    @Test
    @DisplayName("Car should print the correct move message")
    void testCarMove() {
        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Create Car and call move()
        Transport car = new Car();
        car.move();

        // Restore original System.out
        System.setOut(originalOut);

        String actualOutput = outputStream.toString().trim();
        assertEquals("The car is driving on the road.", actualOutput);
    }
}