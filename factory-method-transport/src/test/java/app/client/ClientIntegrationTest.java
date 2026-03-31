package app.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for the Client class demonstrating the Factory Method pattern.
 * This test verifies that the client works correctly with different factories
 * and produces the expected output.
 */
@DisplayName("Integration tests for Client (Factory Method pattern)")
class ClientIntegrationTest {

    @Test
    @DisplayName("Client.run() should create correct transports using factories and print proper messages")
    void testClientRun() {
        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Run the client demonstration
        Client client = new Client();
        client.run();

        // Restore original System.out
        System.setOut(originalOut);

        String output = outputStream.toString();

        // Verify that both transport types produced their correct messages
        assertTrue(output.contains("The car is driving on the road."),
                "Car move message should be printed");

        assertTrue(output.contains("The plane is flying in the sky."),
                "Plane move message should be printed");

        assertTrue(output.contains("Factory Method Pattern executed successfully"),
                "Success message should be displayed");
    }
}