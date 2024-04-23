package test;

import model.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestConnection {
    public static void main(String[] args) {
        try {
            // Start a server on port 1234
            ServerPlayer server = new ServerPlayer("Server", 1234);
            System.out.println("Server started");
            new Thread(() -> {
                try {
                    // Accept a client and receive a tile from it
                    System.out.println("Waiting for client to connect...");
                    server.acceptClient();
                    System.out.println("Client connected");

                    // Create a list of players
                    List<Player> players = new ArrayList<>();
                    players.add(server);
                    players.add(new Player("Client"));
                    Board board = new Board(players);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Wait for a bit to ensure the server is ready
            Thread.sleep(2000);

            System.out.println("Creating client...");
            // Connect a client to the server
            Client client = new Client("Client", new java.net.Socket("localhost", 1234));
            System.out.println("Client connected");

            // Send a tile to the server
            Tile tile = new Tile(new Quarries(1, 1), new Quarries(1, 1), new Quarries(1, 1));
            client.sendTile(tile);
            System.out.println("Sent tile to server: " + tile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
