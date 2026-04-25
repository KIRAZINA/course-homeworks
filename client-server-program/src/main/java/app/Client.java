package app;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Simple TCP Client that can connect to the server and send commands.
 */
public class Client {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 55555;

    public static void main(String[] args) {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket(HOST, PORT);
            System.out.println("[CLIENT] Successfully connected to the server!");

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Thread for receiving messages from server
            final BufferedReader finalIn = in;

            Thread receiveThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = finalIn.readLine()) != null) {
                        System.out.println("[SERVER MESSAGE] " + serverMessage);
                    }
                } catch (IOException e) {
                    // Server closed the connection or error occurred
                }
            });
            receiveThread.setDaemon(true);
            receiveThread.start();

            System.out.println("Enter commands (type 'exit' to disconnect):");

            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine().trim();

                if (command.isEmpty()) continue;

                out.println(command);

                if (command.equalsIgnoreCase("exit")) {
                    System.out.println("[CLIENT] Disconnecting from server...");
                    break;
                }
            }

        } catch (ConnectException e) {
            System.out.println("[CLIENT] Error: Could not connect to server. Is the server running?");
        } catch (IOException e) {
            System.out.println("[CLIENT] Error: " + e.getMessage());
        } finally {
            // Close resources safely
            try {
                if (out != null) out.close();
                if (in != null) in.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                // Ignore closing errors
            }
            scanner.close();
            System.out.println("[CLIENT] Connection closed.");
        }
    }
}