package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class GameServer {
    private static final int PORT = 8080;
    private static final Queue<Socket> waitingPlayers = new LinkedList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket newPlayer = serverSocket.accept();
                System.out.println("New player connected: " + newPlayer.getInetAddress());

                synchronized (waitingPlayers) {
                    if (waitingPlayers.isEmpty()) {
                        // No waiting players, so add the current one to the queue
                        waitingPlayers.add(newPlayer);
                        System.out.println("Waiting for a second player...");
                    } else {
                        // Pair the new player with the waiting one
                        Socket player1 = waitingPlayers.poll();
                        System.out.println("Starting a game session with two players.");
                        new ClientHandler(player1, newPlayer).start();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
