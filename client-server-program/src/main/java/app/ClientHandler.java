package app;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * Thread-safe ClientHandler with proper lifecycle management.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final String clientName;
    private final Server server;
    private final PrintWriter out;
    private final LocalDateTime connectionTime;

    public ClientHandler(Socket socket, String clientName, Server server) throws IOException {
        this.socket = socket;
        this.clientName = clientName;
        this.server = server;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.connectionTime = LocalDateTime.now();
    }

    public LocalDateTime getConnectionTime() {
        return connectionTime;
    }

    public String getClientName() {
        return clientName;
    }

    /**
     * Thread-safe send (checks connection state)
     */
    public void sendMessage(String message) {
        if (out != null && !socket.isClosed() && socket.isConnected()) {
            out.println(message);
        }
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            sendMessage("Welcome " + clientName + "! Type messages or 'exit' to disconnect.");

            String message;
            while ((message = in.readLine()) != null) {
                message = message.trim();

                if (message.equalsIgnoreCase("exit")) {
                    server.log(clientName + " requested to disconnect.");
                    break;
                }

                if (!message.isEmpty()) {
                    server.log("Received from " + clientName + ": " + message);
                    sendMessage("[YOU] " + message);           // echo to sender
                    server.broadcast(clientName, message);     // to others
                }
            }

        } catch (IOException e) {
            if (!socket.isClosed()) {
                server.log("Connection error with " + clientName + ": " + e.getMessage());
            }
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {}
        server.removeClient(clientName);
    }
}