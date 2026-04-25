package app;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UnitTests {
    @Test
    void shouldGenerateUniqueClientNames() throws Exception {
        Server server = new Server(0);
        String c1 = server.generateClientName();
        String c2 = server.generateClientName();
        assertNotEquals(c1, c2);
    }

    @Test
    void shouldRemoveClientFromActiveConnections() throws Exception {
        Server server = new Server();

        ClientHandler mock = Mockito.mock(ClientHandler.class);

        server.activeConnections.put("client-1", mock);

        server.removeClient("client-1");

        assertFalse(server.activeConnections.containsKey("client-1"));
    }
}
