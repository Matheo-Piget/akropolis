package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import model.Tile;

/**
 * Represents the server in the game.
 * It can accept multiple clients and receive tiles from them.
 * UNFINISHED AND UNUSED
 */
public class Server {
    private ServerSocket serverSocket;
    private int capacity;
    private ArrayList<ClientHandler> clients = new ArrayList<>(); // List of connected clients

    public Server(int port, int capacity) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.capacity = capacity;
    }

    // Method to accept a new client connection
    public void acceptClient() throws IOException {
        System.out.println("Waiting for client to connect in ServerPlayer...");
        Socket clientSocket = serverSocket.accept();
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        clients.add(clientHandler);
        capacity--;
        clientHandler.start();
        System.out.println("Client connected in ServerPlayer");
        if(capacity > 0){
            acceptClient();
        }
    }

    // Method to close the server
    public void close() throws IOException {
        for (ClientHandler client : clients) {
            client.close();
        }
        serverSocket.close();
    }

    // Inner class to handle communication with a client
    public class ClientHandler extends Thread {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("Waiting for tile from client...");
                    Tile tile = receiveTile();
                    System.out.println("Received tile from client: " + tile);
                    // Update all the boards except the one who has send the tile
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Method to receive a tile from the client
        public Tile receiveTile() throws IOException, ClassNotFoundException {
            Tile tile = (Tile) in.readObject();
            return tile;
        }

        // Method to close the connection
        public void close() throws IOException {
            in.close();
            out.close();
            socket.close();
        }
    }
}
