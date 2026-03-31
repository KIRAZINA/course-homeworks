package app.transport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Plane class.
 */
@DisplayName("Unit tests for Plane")
class PlaneTest {

    @Test
    @DisplayName("Plane should print the correct move message")
    void testPlaneMove() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        Transport plane = new Plane();
        plane.move();

        System.setOut(originalOut);

        String actualOutput = outputStream.toString().trim();
        assertEquals("The plane is flying in the sky.", actualOutput);
    }
}