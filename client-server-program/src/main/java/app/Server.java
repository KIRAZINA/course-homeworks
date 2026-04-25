package app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Improved multi-client TCP Server with fixed thread pool and graceful shutdown.
 */
public class Server {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 55555;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ServerSocket serverSocket;
    final Map<String, ClientHandler> activeConnections = new ConcurrentHashMap<>();
    private final AtomicInteger clientCounter = new AtomicInteger(0);
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port, 50, InetAddress.getByName(HOST));
        log("Server started on " + HOST + ":" + getPort());
    }

    public Server() throws IOException {
        this(PORT);
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public String generateClientName() {
        return "client-" + clientCounter.incrementAndGet();
    }

    protected void log(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println("[" + timestamp + " SERVER] " + message);
    }

    public void start() {
        log("Waiting for client connections...");

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                String clientName = generateClientName();

                ClientHandler handler = new ClientHandler(clientSocket, clientName, this);
                activeConnections.put(clientName, handler);

                log(clientName + " successfully connected");
                executorService.submit(handler);

            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    log("Error accepting client: " + e.getMessage());
                }
            }
        }
    }

    public void removeClient(String clientName) {
        activeConnections.remove(clientName);
        log(clientName + " has been removed from active connections");
    }

    /**
     * Safe broadcast using snapshot to avoid ConcurrentModificationException
     */
    public void broadcast(String senderName, String message) {
        String formattedMsg = "[" + senderName + "] " + message;
        List<ClientHandler> handlersSnapshot = new ArrayList<>(activeConnections.values());

        for (ClientHandler handler : handlersSnapshot) {
            if (!handler.getClientName().equals(senderName)) {
                handler.sendMessage(formattedMsg);
            }
        }
        log("Broadcast from " + senderName + ": " + message);
    }

    /**
     * Graceful shutdown
     */
    public void shutdown() {
        log("Shutting down server...");
        try {
            serverSocket.close();
        } catch (IOException ignored) {}

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log("Server shutdown complete.");
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            // Shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

            server.start();
        } catch (IOException e) {
            System.err.println("[SERVER] Failed to start: " + e.getMessage());
        }
    }
}