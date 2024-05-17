package test;

import model.*;
import network.Client;
import network.Server;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Test the connection between the server and the client
 */
public class TestConnection {
    public static void main(String[] args) {
        try {
            // Start a server on port 1234
            Server server = new Server(1234, 2);
            System.out.println("Server started");
            new Thread(() -> {
                try {
                    // Accept a client and receive tiles from it
                    server.acceptClient();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Wait for a bit to ensure the server is ready
            Thread.sleep(2000);

            System.out.println("Creating client...");
            // Connect a client to the server
            Client client1 = new Client("Client1", new java.net.Socket("localhost", 1234));
            Client client2 = new Client("Client2", new java.net.Socket("localhost",1234));
            System.out.println("Client connected");
            // Create the list of players
            ArrayList<Player> players = new ArrayList<Player>();
            players.add(client1);
            players.add(client2);
            @SuppressWarnings("unused")
            Board board = new Board(players);
            // Send a tile to the server
            Tile tile = new Tile(new Quarries(1, 1), new Quarries(1, 1), new Quarries(1, 1));
            client1.sendTile(tile);
            System.out.println("Client 1 successfully sent tile to server: " + tile);
            client2.sendTile(tile);
            System.out.println("Client 2 successfully sent tile to server: " + tile);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
